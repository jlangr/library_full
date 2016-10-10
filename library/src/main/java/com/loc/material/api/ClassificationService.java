package com.loc.material.api;

// TODO stand up mock classification service
public class ClassificationService implements ClassificationApi {
   @Override
   public MaterialDetails getMaterialDetails(String sourceId) {
      switch (sourceId) {
         case "123": return new MaterialDetails("123", "Kafka", "The Trial", "123", "");
      }
      throw new RuntimeException("unable to connect");
   }
}
