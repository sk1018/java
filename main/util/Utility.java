package main.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Utility {

 public static String APP_ID = "dj0zaiZpPUZGYjNBNHMxcERJZyZzPWNvbnN1bWVyc2VjcmV0Jng9NDY-";

 public static void main(String[] args) {
  String keyQuery = "YUUBLOG";
  System.out.println(keyQuery+"で検索ヒットしたページ数："+getHitPageCount(keyQuery));
 }
 
 public static Double getHitPageCount(String keyQuery) {
  URL url;
  try {
   url = new URL(
     "http://search.yahooapis.jp/WebSearchService/V1/webSearch" 
     + "?appid=" + APP_ID 
     + "&query=" + keyQuery
     + "&results=1");
   HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
   urlconnection.setRequestMethod("GET");
   
   Scanner sc = new Scanner(urlconnection.getInputStream());
   String nextStr = null;
   Double result = null;
   
   while(sc.hasNext()){
    nextStr = sc.next();
    if(nextStr.contains("totalResultsAvailable")){
     result = new Double(nextStr
                         .replace("totalResultsAvailable=\"", "")
                         .replace("\"", ""));
     return result;
    }
   }
   
   sc.close();
  } catch (Exception e) {
   e.printStackTrace();
  }
  return Double.MIN_VALUE;
 }
}