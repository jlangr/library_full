package util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

public class ARomanConverter {
	@Test
	public void convertsStuff() {
		assertThat(Roman.convert(1), is(equalTo("I")));
		assertThat(Roman.convert(2), is(equalTo("II")));
		assertThat(Roman.convert(3), is(equalTo("III")));
		assertThat(Roman.convert(4), is(equalTo("IV")));
		assertThat(Roman.convert(5), is(equalTo("V")));
		assertThat(Roman.convert(9), is(equalTo("IX")));
		assertThat(Roman.convert(10), is(equalTo("X")));
		assertThat(Roman.convert(11), is(equalTo("XI")));
		assertThat(Roman.convert(20), is(equalTo("XX")));
		assertThat(Roman.convert(40), is(equalTo("XL")));
		assertThat(Roman.convert(49), is(equalTo("XLIX")));
		assertThat(Roman.convert(50), is(equalTo("L")));
		assertThat(Roman.convert(90), is(equalTo("XC")));
		assertThat(Roman.convert(100), is(equalTo("C")));
		assertThat(Roman.convert(333), is(equalTo("CCCXXXIII")));
		assertThat(Roman.convert(400), is(equalTo("CD")));
		assertThat(Roman.convert(500), is(equalTo("D")));
		assertThat(Roman.convert(900), is(equalTo("CM")));
		assertThat(Roman.convert(1000), is(equalTo("M")));
		assertThat(Roman.convert(3999), is(equalTo("MMMCMXCIX")));
	}
}
