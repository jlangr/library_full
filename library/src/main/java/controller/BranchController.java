package controller;

import java.util.List;
import static java.util.stream.Collectors.toList;
import org.springframework.web.bind.annotation.*;
import api.library.BranchService;

@RestController
@RequestMapping("/branches")
public class BranchController {
   private BranchService service = new BranchService();

   @PostMapping
   public String add(@RequestBody BranchRequest branchRequest) {
      // TODO [x] on duplicate/error
       return service.add(branchRequest.getName());
   }

   @GetMapping
   public List<BranchRequest> retrieveAll() {
       return service.allBranches().stream()
          .map(branch -> new BranchRequest(branch))
          .collect(toList());
   }
}
