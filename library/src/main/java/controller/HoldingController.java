package controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import api.library.HoldingService;
import domain.core.*;

@RestController
@RequestMapping("holdings")
public class HoldingController {
   private HoldingService service = new HoldingService();

   @PostMapping
   public String addHolding(@RequestBody AddHoldingRequest request) {
      // TODO need material type also
      // Does API format change?
      return service.add(request.getSourceId(), request.getBranchScanCode());
   }

   @PostMapping(value = "/checkout")
   public void checkout(@RequestBody CheckoutRequest request, HttpServletResponse response) {
      try {
         service.checkOut(request.getPatronId(), request.getHoldingBarcode(), request.getCheckoutDate());
      }
      catch (HoldingAlreadyCheckedOutException exception) {
         response.setStatus(409);
      }
   }

   @PostMapping(value = "/checkin")
   public void checkin(@RequestBody CheckinRequest request) {
      service.checkIn(request.getHoldingBarcode(), request.getCheckinDate(), request.getBranchScanCode());
   }

   // TODO missing a leading slash? Used at all?
   @GetMapping(value = "{holdingBarcode}")
   public HoldingResponse retrieve(@PathVariable("holdingBarcode") String holdingBarcode) {
      return new HoldingResponse(service.find(holdingBarcode));
   }

   // TODO use query params instead!
   @GetMapping(value = "/branches/{scanCode}") // WRONG
   public List<HoldingResponse> retrieveHoldings(@PathVariable String scanCode) {
      System.out.println("scan code = " + scanCode);
      List<Holding> holdings = service.findByBranch(scanCode);
      System.out.println("holdings:" + holdings);
      return holdings.stream()
         .map(holding -> new HoldingResponse(holding))
         .collect(Collectors.toList());
   }
}