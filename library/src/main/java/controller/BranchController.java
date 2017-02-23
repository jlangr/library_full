package controller;

import api.library.BranchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/branches")
public class BranchController {
    private BranchService service = new BranchService();

    @PostMapping
    public String add(@RequestBody BranchRequest branchRequest) {
        return service.add(branchRequest.getName());
    }

    @GetMapping
    public List<BranchRequest> retrieveAll() {
        return service.allBranches().stream()
                .map(branch -> new BranchRequest(branch))
                .collect(toList());
    }
}
