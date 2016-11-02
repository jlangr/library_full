package controller;

import org.springframework.web.bind.annotation.*;
import com.loc.material.api.*;
import api.library.LibraryData;
import domain.core.ClassificationApiFactory;

@RestController
public class LibraryController {
   @PostMapping(value="/clear")
   public void clear() {
      LibraryData.deleteAll();
   }

   @PostMapping(value="/use_local_classification")
   public void useLocalClassificationService() {
      ClassificationApiFactory.setService(new LocalClassificationService());
   }

   @PostMapping(value="/materials")
   public void addMaterial(@RequestBody MaterialRequest materialRequest) {
      LocalClassificationService service = (LocalClassificationService)ClassificationApiFactory.getService();
      service.addBook(createMaterial(materialRequest));
   }

   private Material createMaterial(MaterialRequest materialRequest) {
      Material material = new Material();
      material.setSourceId(materialRequest.getSourceId());
      material.setClassification(materialRequest.getClassification());
      material.setTitle(materialRequest.getTitle());
      material.setYear(materialRequest.getYear());
      material.setFormat(MaterialType.valueOf(materialRequest.getFormat()));
      material.setAuthor(materialRequest.getAuthor());
      return material;
   }
}
