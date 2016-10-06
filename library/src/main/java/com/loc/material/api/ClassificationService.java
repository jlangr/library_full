package com.loc.material.api;

import java.util.Collection;

// TODO stand up mock classification service
public class ClassificationService implements ClassificationApi {
   @Override
   public MaterialDetails getMaterialDetails(String sourceId) {
      switch (sourceId) {
         case "123": return new MaterialDetails("123", "Kafka", "The Trial", "123", "");
      }
      throw new RuntimeException("unable to connect");
   }

   @Override
   public Collection<MaterialDetails> allMaterials() {
      throw new RuntimeException("unable to connect");
   }
}
