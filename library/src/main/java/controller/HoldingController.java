package controller;

import org.springframework.web.bind.annotation.*;
import api.library.HoldingService;

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
   public void checkout(@RequestBody CheckoutRequest request) {
      service.checkOut(request.getPatronId(), request.getHoldingBarcode(), request.getCheckoutDate());
   }

   @PostMapping(value = "/checkin")
   public void checkin(@RequestBody CheckinRequest request) {
      service.checkIn(request.getHoldingBarcode(), request.getCheckinDate(), request.getBranchScanCode());
   }

   @GetMapping(value = "{holdingBarcode}")
   public HoldingResponse retrieve(@PathVariable("holdingBarcode") String holdingBarcode) {
      return new HoldingResponse(service.find(holdingBarcode));
   }
}