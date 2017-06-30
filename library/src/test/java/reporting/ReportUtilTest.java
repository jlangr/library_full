package reporting;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ReportUtilTest {
    @Test
    public void transformReturnsEmptyWhenStuffEmpty() {
        String result =
            ReportUtil.transform("",
                    0,
                    0,
                    ReportUtil.StringOp.under);

        assertThat(result, is(equalTo("")));
    }

    @Test
    public void transformCreatesNumberOfDashes() {
        int number = 7;
        String result =
                ReportUtil.transform("abc",
                        number,
                        0,
                        ReportUtil.StringOp.under);

        assertThat(result, is(equalTo("-------")));
    }

    @Test
    public void transformCreatesDashesFollowedBySpaces() {
        int numberOfDashes = 2;
        int numberOfFollowingSpaces = 3;
        String result =
                ReportUtil.transform("abc",
                        numberOfDashes,
                        numberOfFollowingSpaces,
                        ReportUtil.StringOp.under);

        assertThat(result, is(equalTo("--   ")));
    }
}
