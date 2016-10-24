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
   public void addMaterial(@RequestBody MaterialDetails material) {
      LocalClassificationService service = (LocalClassificationService)ClassificationApiFactory.getService();
      service.addBook(material);
   }
}
