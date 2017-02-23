package util;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ListUtilTest {
    @Test
    public void mapTransformsUsingGetter() {
        List<String> s = asList("1", "22", "333");

        List<Integer> mappedToLengths = new ListUtil().map(s, "length", String.class, Integer.class);

        assertThat(mappedToLengths, equalTo(asList(1, 2, 3)));
    }
}
