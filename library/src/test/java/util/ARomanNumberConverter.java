package util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ARomanNumberConverter {
    @Test
    public void convertsLotsOfNumbers() {
        assertThat(RomanConverter.convert(1), is(equalTo("I")));
        assertThat(RomanConverter.convert(2), is(equalTo("II")));
        assertThat(RomanConverter.convert(3), is(equalTo("III")));
        assertThat(RomanConverter.convert(10), is(equalTo("X")));
        assertThat(RomanConverter.convert(11), is(equalTo("XI")));
        assertThat(RomanConverter.convert(20), is(equalTo("XX")));
    }

}
