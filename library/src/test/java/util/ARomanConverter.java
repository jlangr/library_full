package util;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ARomanConverter {
    @Test
    public void convertsArabicNumbers() {
        assertThat(RomanConverter.convert(1),
            is(equalTo("I")));
    }

    @Test
    public void convertsArabicNumbers2() {
        assertThat(RomanConverter.convert(2),
                is(equalTo("II")));
    }

    @Test
    public void convertsArabicNumbers3() {
        assertThat(RomanConverter.convert(3),
                is(equalTo("III")));
    }

    @Test
    public void convertsArabicNumbers10() {
        assertThat(RomanConverter.convert(10),
                is(equalTo("X")));
    }

    @Test
    public void convertsArabicNumbers11() {
        assertThat(RomanConverter.convert(11),
                is(equalTo("XI")));
    }
    @Test
    public void convertsArabicNumbers20() {
        assertThat(RomanConverter.convert(20),
                is(equalTo("XX")));
    }

    @Test
    public void convertsArabicNumbers15() {
        assertThat(RomanConverter.convert(15),
                is(equalTo("XV")));
    }

    @Test
    public void convertsArabicNumbers4() {
        assertThat(RomanConverter.convert(4),
                is(equalTo("IV")));
    }

    @Test
    public void restOfNumbers() {
       assertThat(RomanConverter.convert(50),
               is(equalTo("L")));
        assertThat(RomanConverter.convert(100),
                is(equalTo("C")));
        assertThat(RomanConverter.convert(500),
                is(equalTo("D")));
        assertThat(RomanConverter.convert(1000),
                is(equalTo("M")));
        assertThat(RomanConverter.convert(9),
                is(equalTo("IX")));
        assertThat(RomanConverter.convert(40),
                is(equalTo("XL")));
        assertThat(RomanConverter.convert(90),
                is(equalTo("XC")));
        assertThat(RomanConverter.convert(400),
                is(equalTo("CD")));
        assertThat(RomanConverter.convert(900),
                is(equalTo("CM")));

        assertThat(RomanConverter.convert(3999),
                is(equalTo("MMMCMXCIX")));

        assertThat(RomanConverter.convert(1666),
                is(equalTo("MDCLXVI")));
    }
}
