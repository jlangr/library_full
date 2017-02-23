package com.loc.material.api;

public class Material {
    private String sourceId;
    private String title;
    private String author;
    private String year;
    private MaterialType format;
    private String classification;

    public Material() {
    }

    public Material(String sourceId,
                    String author,
                    String title,
                    String classification,
                    MaterialType format,
                    String year) {
        this.sourceId = sourceId;
        this.author = author;
        this.title = title;
        this.classification = classification;
        this.format = format;
        this.year = year;
    }

    public Material(String sourceId, String author, String title, String classification,
                    String year) {
        this(sourceId, author, title, classification, MaterialType.Book, year);
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public MaterialType getFormat() {
        return format;
    }

    public void setFormat(MaterialType format) {
        this.format = format;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return getFormat() + ": " + getClassification() + " " + getSourceId() + " " + getTitle() + " (" + getAuthor()
               + ")";
    }
}
