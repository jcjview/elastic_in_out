package com.tinno.elastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirInstance {

  private static Logger logger = LoggerFactory.getLogger(DirInstance.class);

  List<Map<String, Object>> getJson() {
    List<Map<String, Object>> ret = new ArrayList<>();
    return ret;

  }

  static List<Map<String, Object>> getJson(String path) {
    List<Map<String, Object>> ret = new ArrayList<>();
    BufferedReader br = null;
    try (InputStream inputStream = new BufferedInputStream(new FileInputStream(path))) {
      br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
      String line;
      ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
      while ((line = br.readLine()) != null) {
        Map<String, Object> result = mapper
            .readValue(line, new TypeReference<Map<String, Object>>() {
            });
        ret.add(result);
      }
    } catch (IOException e) {
      logger.error("", e);
    } finally {
      try {
        if (br != null) {
          br.close();
        }
      } catch (IOException ex) {
        logger.error("", ex);
      }
    }
    return ret;
  }

  static boolean save(List<String> data, String filePath) {
    OutputStreamWriter fw = null;
    PrintWriter out = null;
    boolean flag = true;
    try {
      fw = new OutputStreamWriter(new FileOutputStream(filePath, false), "utf-8");
      out = new PrintWriter(fw);
      ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
      for (String d : data) {
        out.write(d + "\n");
      }
    } catch (Exception ex) {
      logger.error("", ex);
      flag = false;
    } finally {
      if (out != null) {
        out.close();
      }
    }
    return flag;
  }

}
