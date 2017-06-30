package reporting;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class InventoryReportTest {

    private InventoryReport report;
    private StringBuilder buffer;

    @Before
    public void initialize() {
        report = new InventoryReport(null);
        buffer = new StringBuilder();
    }

    @Test
    public void appendFooterForUpTo100Records() {
        report.appendToFooter(buffer, 50);

        assertThat(buffer.toString(),
                is(equalTo("Total # of records: 50\\n")));
    }

    @Test
    public void appendFooterForOver100Records() {
        report.appendToFooter(buffer, 105);

        assertThat(buffer.toString(),
                is(equalTo("Total # of records (100 shown): 105\\n")));
    }
}
