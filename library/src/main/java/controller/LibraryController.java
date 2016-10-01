package controller;

import org.springframework.web.bind.annotation.*;
import api.library.LibraryData;

@RestController
public class LibraryController {
   @PostMapping(value="/clear")
   public void clear() {
      LibraryData.deleteAll();
   }
}
