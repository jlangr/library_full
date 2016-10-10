package com.loc.material.api;

import java.util.*;

public class LocalClassificationService implements ClassificationApi {
   private Map<String,MaterialDetails> materials = new HashMap<>();

   @Override
   public MaterialDetails getMaterialDetails(String sourceId) {
      return materials.get(sourceId);
   }

   public void addBook(MaterialDetails material) {
      materials.put(material.getSourceId(), material);
   }

   @Override
   public String toString() {
      return materials.toString();
   }
}
