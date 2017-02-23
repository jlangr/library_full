package controller;

import com.loc.material.api.Material;
import domain.core.Holding;

import java.io.Serializable;
import java.util.Date;

public class HoldingResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String author;
    private String title;
    private String year;
    private String format;
    private Date dateDue;
    private String barcode;
    private Integer copyNumber;
    private Date dateCheckedOut;
    private Date dateLastCheckedIn;
    private boolean isAvailable;

    public HoldingResponse() {
    }

    public HoldingResponse(Holding holding) {
        barcode = holding.getBarcode();
        copyNumber = holding.getCopyNumber();
        dateDue = holding.dateDue();
        isAvailable = holding.isAvailable();
        dateCheckedOut = holding.dateCheckedOut();
        dateLastCheckedIn = holding.dateLastCheckedIn();

        Material material = holding.getMaterial();
        author = material.getAuthor();
        title = material.getTitle();
        year = material.getYear();
        format = material.getFormat().toString();
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public void setDateLastCheckedIn(Date dateLastCheckedIn) {
        this.dateLastCheckedIn = dateLastCheckedIn;
    }

    public void setDateCheckedOut(Date dateCheckedOut) {
        this.dateCheckedOut = dateCheckedOut;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setCopyNumber(Integer copyNumber) {
        this.copyNumber = copyNumber;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getFormat() {
        return format;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public Date getDateCheckedOut() {
        return dateCheckedOut;
    }

    public Date getDateLastCheckedIn() {
        return dateLastCheckedIn;
    }

    public String getBarcode() {
        return barcode;
    }

    public Integer getCopyNumber() {
        return copyNumber;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }
}
