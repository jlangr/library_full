package com.loc.material.api;

import org.junit.*;
import org.junit.experimental.categories.Category;
import testutil.Slow;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@Category(Slow.class)
public abstract class ClassificationApiContract {
    private Material material;

    @Before
    public void createAndRetrieve() {
        ClassificationApi classificationApiImplementation = createClassificationApiImpl();
        material = classificationApiImplementation.retrieveMaterial(validQueryIsbn());
    }

    @Ignore
    @Test
    public void populatesCriticalFields() {
        assertThat(material.getAuthor(), not(nullValue()));
        assertThat(material.getTitle(), not(nullValue()));
        assertThat(material.getClassification(), not(nullValue()));
    }

    @Ignore
    @Test
    public void echosSourceId() {
        assertThat(material.getSourceId(), is(equalTo(validQueryIsbn())));
    }

    @Ignore
    @Test
    public void populatesFormatWithEnumValue() {
        assertThat(material.getFormat(), isIn(MaterialType.values()));
    }

    @Ignore
    @Test
    public void populatesYearWithReasonableValue() {
        int currentYear = LocalDate.now().getYear();
        assertThat(Integer.parseInt(material.getYear()),
                is(both(greaterThan(1440)).and(lessThanOrEqualTo(currentYear))));
    }


    abstract protected ClassificationApi createClassificationApiImpl();

    abstract protected String validQueryIsbn();
}
