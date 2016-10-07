package controller;

import java.io.Serializable;
import java.util.Date;

// TODO implement serializable--necessary?
public class CheckinRequest implements Serializable {
   private static final long serialVersionUID = 1L;
   private String holdingBarcode;
   private Date checkinDate;
   private String branchScanCode;

   public CheckinRequest() {
   }

   public void setHoldingBarcode(String holdingBarcode) {
      this.holdingBarcode = holdingBarcode;
   }

   public String getHoldingBarcode() {
      return holdingBarcode;
   }

   public void setCheckinDate(Date checkinDate) {
      this.checkinDate = checkinDate;
   }

   public Date getCheckinDate() {
      return checkinDate;
   }

   public void setBranchScanCode(String branchScanCode) {
      this.branchScanCode = branchScanCode;
   }

   public String getBranchScanCode() {
      return branchScanCode;
   }
}
