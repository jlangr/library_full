package controller;

import java.util.List;
import static java.util.stream.Collectors.toList;
import org.springframework.web.bind.annotation.*;
import api.library.PatronService;

@RestController
@RequestMapping("/patrons")
public class PatronController {
   private PatronService service = new PatronService();

   @PostMapping
   public String add(@RequestBody PatronRequest patronRequest) {
      System.out.println("add patron" + patronRequest.getName());
      return service.add(patronRequest.getName());
   }

   @GetMapping
   public List<PatronRequest> retrieveAll() {
      List<PatronRequest> collect = service.allPatrons().stream()
         .map(patron -> new PatronRequest(patron))
         .collect(toList());
      System.out.println("collection:" + collect);
      return collect;
   }
}
