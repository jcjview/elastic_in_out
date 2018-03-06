package com.tinno.elastic;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class GetPropertyValues {
  private String ip;
  private String port;
  private String input;
  private String output;
  private String flag;
  private String index;
  private String type;
  public GetPropertyValues(){

  }

  public GetPropertyValues(String [] args){
    ip = "localhost";
    port = "9300";
    input="input";
    output="output";
    flag="in";
    index="test";
    type="type";


    int count=0;
    for (int i = 0; i < args.length-1; i++) {
      String s = args[i];
      if(Objects.equals(s, "-ip")){
        ip=args[i+1];
        i++;
        count++;
        continue;
      }

      if(Objects.equals(s, "-port")){
        port=args[i+1];
        i++;
        count++;
        continue;
      }
      if(Objects.equals(s, "-flag")){
        flag=args[i+1];
        i++;
        count++;
        continue;
      }


      if(Objects.equals(s, "-index")){
        index=args[i+1];
        i++;
        count++;
        continue;
      }

      if(Objects.equals(s, "-type")){
        type=args[i+1];
        i++;
        count++;
        continue;
      }

      if(Objects.equals(s, "-output")){
        output=args[i+1];
        i++;
        count++;
        continue;
      }

      if(Objects.equals(s, "-input")){
        input=args[i+1];
        i++;
        count++;
        continue;
      }
    }



  }



  public String getIp() {
    return ip;
  }

  public Integer getPort() {
    return Integer.parseInt(port);
  }


  public String getinput() {
    return input;
  }
  public String getoutput() {
    return output;
  }

  public String getFlag(){return flag;}

  public String getIndex() {
    return index;
  }

  public String getType() {
    return type;
  }
}