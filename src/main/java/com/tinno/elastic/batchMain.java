package com.tinno.elastic;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class batchMain {

  private static Logger logger = LoggerFactory.getLogger(batchMain.class);


  public static void bulkInput(String indexName, String type, List<Map<String, Object>> docData,
      TransportClient client) {
// /批量添加：

    BulkRequestBuilder bulkRequest = client.prepareBulk();

    for (int i = 0; i < docData.size(); i++) {
      Map<String, Object> temp = docData.get(i);
      bulkRequest.add(
          client.prepareIndex(indexName, type)
              .setSource(
                  temp
              ));
      if (i % 1000 == 0) {
        bulkRequest.execute().actionGet();
        bulkRequest = client.prepareBulk();
      }
    }
    BulkResponse bulkResponse = bulkRequest.execute().actionGet();
    bulkRequest.request().requests().clear();
    if (bulkResponse.hasFailures()) {
      logger.error("bulkResponse.hasFailures()");
    }
  }


  public static List<String> bulkOutput(String indexName, String type,
      TransportClient client) {
// /批量添加：
    List<String> docData = new ArrayList<>();

    SearchRequestBuilder
        requestBuilder = client.prepareSearch(indexName).setTypes(type)
        .setQuery(QueryBuilders.matchAllQuery());
    SearchResponse scrollResp = requestBuilder.setSize(5000).setScroll(new TimeValue(60000)).get();
    do {
      for (SearchHit hit : scrollResp.getHits().getHits()) {
        docData.add(hit.getSourceAsString());
      }
      scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
          .setScroll(new TimeValue(60000)).execute().actionGet();
    } while (scrollResp.getHits().getHits().length != 0);
    return docData;
  }

  public static void main(String[] args) throws UnknownHostException {
    GetPropertyValues getProperty = new GetPropertyValues(args);

    TransportClient client;
    //设置集群名称
    Settings settings = Settings.builder().put("cluster.name", "tinno-sematic-application")
        .build();
    //创建client
    client = new PreBuiltTransportClient(settings)
        .addTransportAddress(
            new InetSocketTransportAddress(InetAddress.getByName(getProperty.getIp()),
                getProperty.getPort()));

    if (Objects.equals(getProperty.getFlag(), "in")) {
      bulkInput(getProperty.getIndex(), getProperty.getType(),
          DirInstance.getJson(getProperty.getinput()), client);
    } else {
      DirInstance.save(bulkOutput(getProperty.getIndex(), getProperty.getType(), client),
          getProperty.getoutput());
    }

  }


}
