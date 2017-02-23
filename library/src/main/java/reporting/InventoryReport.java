package reporting;

import com.loc.material.api.MaterialType;
import domain.core.Catalog;
import domain.core.Holding;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryReport {
    private static final String NEWLINE = System.getProperty("line.separator");
    private static final int SPACING = 2;
    private static final int TITLE_LENGTH = 20;
    private static final int BRANCH_LENGTH = 20;
    private static final int AUTHOR_LENGTH = 30;
    private static final int YEAR_LENGTH = 6;
    private static final int ISBN_LENGTH = 10;
    private static final String OUTPUT_FILENAME = "./InventoryReport.txt";
    private Catalog catalog;
    private LibraryOfCongress congress;
    private static final int RECORD_LIMIT = 100;

    public InventoryReport(Catalog catalog) {
        this.catalog = catalog;
        this.congress = new LibraryOfCongress();
    }

    public void allBooks() throws IOException {
        List<Record> records = new ArrayList<Record>();
        for (Holding holding : catalog) {
            if (holding.getMaterial().getFormat() == MaterialType.Book) {
                records.add(new Record(holding));
            }
        }

        Collections.sort(records);

        StringBuffer buffer = new StringBuffer();
        buffer.append(ReportUtil.transform("Title",
                TITLE_LENGTH + SPACING, TITLE_LENGTH + SPACING - "Title".length(), ReportUtil.StringOp.pad));
        buffer.append(ReportUtil.transform("Branch",
                BRANCH_LENGTH + SPACING, BRANCH_LENGTH + SPACING - "Branch".length(), ReportUtil.StringOp.pad));
        buffer.append(ReportUtil.transform("Author",
                AUTHOR_LENGTH + SPACING, AUTHOR_LENGTH + SPACING - "Author".length(), ReportUtil.StringOp.pad));
        buffer.append(ReportUtil.transform("Year", YEAR_LENGTH,
                YEAR_LENGTH - "Year".length(), ReportUtil.StringOp.pad));
        buffer.append(ReportUtil.transform("ISBN", ISBN_LENGTH,
                ISBN_LENGTH - "ISBN".length(), ReportUtil.StringOp.pad));
        buffer.append(NEWLINE);

        buffer.append(ReportUtil.transform("", TITLE_LENGTH, SPACING, ReportUtil.StringOp.under));
        buffer.append(ReportUtil.transform("", BRANCH_LENGTH, SPACING, ReportUtil.StringOp.under));
        buffer.append(ReportUtil.transform("", AUTHOR_LENGTH, SPACING, ReportUtil.StringOp.under));
        buffer.append(ReportUtil.transform("", YEAR_LENGTH, SPACING, ReportUtil.StringOp.under));
        buffer.append(NEWLINE);

        int i = 0;
        for (Record record : records) {
            if (i == RECORD_LIMIT) {
                buffer.append("... (only 1st " + RECORD_LIMIT + " records shown) ...");
                break;
            }
            buffer.append(ReportUtil.transform(record.title, TITLE_LENGTH,
                    TITLE_LENGTH - record.title.length(), ReportUtil.StringOp.pad));
            buffer.append(ReportUtil.transform("", SPACING, SPACING - "".length(), ReportUtil.StringOp.pad));
            buffer.append(ReportUtil.transform(record.branch, BRANCH_LENGTH,
                    BRANCH_LENGTH - record.branch.length(), ReportUtil.StringOp.pad));
            buffer.append(ReportUtil.transform("", SPACING, SPACING - "".length(), ReportUtil.StringOp.pad));
            buffer.append(ReportUtil.transform(record.author, AUTHOR_LENGTH,
                    AUTHOR_LENGTH - record.author.length(), ReportUtil.StringOp.pad));
            buffer.append(ReportUtil.transform("", SPACING, SPACING - "".length(), ReportUtil.StringOp.pad));
            buffer.append(ReportUtil.transform(record.year, YEAR_LENGTH,
                    YEAR_LENGTH - record.year.length(), ReportUtil.StringOp.pad));
            buffer.append(ReportUtil.transform("", SPACING, SPACING - "".length(), ReportUtil.StringOp.pad));
            buffer.append(ReportUtil.transform(record.isbn, ISBN_LENGTH,
                    ISBN_LENGTH - record.isbn.length(), ReportUtil.StringOp.pad));
            buffer.append(NEWLINE);
            i++;
        }

        String result = buffer.toString();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(OUTPUT_FILENAME));
            writer.write("Inventory Report");
            writer.write(NEWLINE);
            writer.write(result, 0, result.length());
        } finally {
            writer.close();
        }
    }

    class Record implements Comparable<Record> {
        String title;
        String branch;
        String author;
        String year;
        String isbn;

        public Record(Holding holding) {
            this.title = holding.getMaterial().getTitle();
            this.branch = holding.getBranch().getName();
            this.author = holding.getMaterial().getAuthor();
            this.year = holding.getMaterial().getYear();
            this.isbn = congress.getISBN(holding.getMaterial().getClassification());
        }

        @Override
        public String toString() {
            return this.title + " " + this.isbn;
        }

        @Override
        public int compareTo(Record that) {
            return this.title.compareTo(that.title);
        }
    }
}
