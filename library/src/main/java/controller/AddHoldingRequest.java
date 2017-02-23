package controller;

public class AddHoldingRequest {
    private String sourceId;
    private String branchScanCode;

    public String getSourceId() {
        return sourceId;
    }

    public String getBranchScanCode() {
        return branchScanCode;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setBranchScanCode(String branchScanCode) {
        this.branchScanCode = branchScanCode;
    }
}
