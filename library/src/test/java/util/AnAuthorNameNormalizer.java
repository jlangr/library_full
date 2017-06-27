package util;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

// 1. Un-ignore the next commented-out test method.
// 2. Run JUnit against *all* tests in the project.
// 3. Did the current test fail? If not: You built too much code in a prior step. Undo work for prior tests and try again.
// 4. Make sure you are clear on why the test failed.
// 5. Write only enough code to make that failing test pass (and not break any other tests).
//    Did you write too much? Is there a simple way to get that test to pass???
// 6. If there is a commented-out assertion, uncomment it. It should fail. If not, return to step 5.
// 7. Return to step 1.

public class AnAuthorNameNormalizer {
    private AuthorNameNormalizer normalizer;

    @Before
    public void create() {
        normalizer = new AuthorNameNormalizer();
    }

    @Test
    public void returnsEmptyStringWhenEmpty() {
        assertThat(normalizer.normalizeName(""), equalTo(""));
    }

    @Test
    public void returnsSingleWordName() {
        assertThat(normalizer.normalizeName("Plato"), equalTo("Plato"));
    }

    @Test
    public void returnsLastFirstWhenFirstLastProvided() {
      assertThat(normalizer.normalizeName("Haruki Murakami"), equalTo("Murakami, Haruki"));
    }

    @Test
    public void trimsLeadingAndTrailingWhitespace() {
        assertThat(normalizer.normalizeName("  Big Boi   "), equalTo("Boi, Big"));
    }

    @Test
    public void initializesMiddleName() {
        assertThat(normalizer.normalizeName("Henry David Thoreau"), equalTo("Thoreau, Henry D."));
    }

    @Test
    public void doesNotInitializeOneLetterMiddleName() {
        assertThat(normalizer.normalizeName("Harry S Truman"), equalTo("Truman, Harry S"));
    }

    @Test
    public void initializesEachOfMultipleMiddleNames() {
        assertThat(normalizer.normalizeName("Julia Scarlett Elizabeth Louis-Dreyfus"), equalTo("Louis-Dreyfus, Julia S. E."));
    }

    @Test
    public void appendsSuffixesToEnd() {
        assertThat(normalizer.normalizeName("Martin Luther King, Jr."), equalTo("King, Martin L., Jr."));
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void throwsWhenNameContainsTwoCommas() {
        normalizer.normalizeName("Thurston, Howell, III");
    }


    @Test
    public void countOfNamesIsZeroWhenCreated() {
        assertThat(normalizer.countOfNames(), equalTo(0));
    }

    @Test
    public void countOfNamesIsIncrementOnAdd() {
        normalizer.normalizeName("Thurston, Howell, III");

        assertThat(normalizer.countOfNames(), equalTo(1));
    }

    @Test
    public void countOfNamesIncrementsForEachNewName() {
        normalizer.normalizeName("Thurston, Howell, III");
        normalizer.normalizeName("xyz");

        assertThat(normalizer.countOfNames(), equalTo(2));
    }

    @Test
    public void countOfNamesDoesNotIncrementForDuplicateName() {
        normalizer.normalizeName("Thurston, Howell, III");
        normalizer.normalizeName("xyz");
        normalizer.normalizeName("Thurston, Howell, III");

        assertThat(normalizer.countOfNames(), equalTo(2));
    }
}
