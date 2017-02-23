package testutil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CollectionsUtilTest {
    private Collection<Object> collection;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void initialize() {
        collection = new ArrayList<Object>();
    }

    @Test
    public void soleElementRetrievesFirstAndOnlyElement() {
        collection.add("a");

        Object soleElement = CollectionsUtil.soleElement(collection);

        assertThat(soleElement, equalTo("a"));
    }

    @Test
    public void soleElementThrowsWhenNoElementsExist() {
        exceptionRule.expect(AssertionError.class);
        exceptionRule.expectMessage(CollectionsUtil.NO_ELEMENTS);

        CollectionsUtil.soleElement(collection);
    }

    @Test
    public void soleElementThrowsWhenMoreThanOneElement() {
        exceptionRule.expect(AssertionError.class);
        exceptionRule.expectMessage(CollectionsUtil.MORE_THAN_ONE_ELEMENT);
        collection.add("a");
        collection.add("b");

        CollectionsUtil.soleElement(collection);
    }
}