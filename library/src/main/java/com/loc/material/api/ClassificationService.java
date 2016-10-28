package com.loc.material.api;

import java.util.Map;
import org.springframework.web.client.RestTemplate;
import util.RestUtil;

public class ClassificationService implements ClassificationApi {
   public static final String SERVER = "http://openlibrary.org";
   private RestTemplate template;

   public ClassificationService() {
      template = RestUtil.createRestTemplate();
   }

   public ClassificationService(RestTemplate restTemplate) {
      template = restTemplate;
   }

   @Override
   public Material retrieveMaterial(String sourceId) {
      Map<String, Object> response = retrieve(sourceId);
      System.out.println("in GMD;" + response);

      Material material = new Material();
      material.setTitle(getString(response, "title"));
      material.setYear(getString(response, "publish_date"));
//      MAP<OBJECT,OBJECT>[] AUTHORS = (MAP<OBJECT, OBJECT>[])RESPONSE.GET("AUTHORS");
      return material;
   }

   private String getString(Map<String, Object> map, String key) {
      return (String)map.get(key);
   }

   private String url(String doc) {
      return String.format(SERVER + doc);
   }

   @SuppressWarnings("unchecked")
   public Map<String, Object> retrieve(String sourceId) {
      Map<String, Object> response = template.getForObject(url(findByDoc(isbnKey(sourceId))), Map.class);
   //      System.out.println(entry.get("authors"));
   //      Map<String, Object> classifications = (Map<String, Object>)entry.get("classifications");
   //      String[] lcClassifications = (String[])classifications.get("lc_classifications");
   //      System.out.println(lcClassifications[0]);

      return (Map<String, Object>)response.get(isbnKey(sourceId));
   }

   private String findByDoc(String bibKey) {
      return "/api/books?bibkeys=" + bibKey + "&jscmd=data&format=json";
   }

   String isbnKey(String sourceId) {
      return "ISBN:" + sourceId;
   }
}