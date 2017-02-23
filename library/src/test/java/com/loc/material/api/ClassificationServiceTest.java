package com.loc.material.api;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import testutil.Slow;

import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static util.MapUtil.createMap;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationServiceTest {
    private static final String THE_ROAD_AUTHOR = "Cormac McCarthy";
    private static final String THE_ROAD_ISBN = "0-307-26543-9";
    private static final String THE_ROAD_TITLE = "The road";
    private static final String THE_ROAD_YEAR = "2006";
    private static final String THE_ROAD_CLASSIFICATION = "PS3563.C337 R63 2006";

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ClassificationService service;

    @Test
    public void retrieveMaterialPopulatesFromResponse() {
        Map<Object, Object> responseMap = createMap(service.isbnKey(THE_ROAD_ISBN),
                createMap("title", THE_ROAD_TITLE,
                        "publish_date", THE_ROAD_YEAR,
                        "classifications", createMap("lc_classifications", asList(THE_ROAD_CLASSIFICATION)),
                        "authors", asList(createMap("name", THE_ROAD_AUTHOR))));
        when(restTemplate.getForObject(contains(THE_ROAD_ISBN), eq(Map.class))).thenReturn(responseMap);

        Material material = service.retrieveMaterial(THE_ROAD_ISBN);

        assertMaterialDetailsForTheRoad(material);
    }

    @Category(Slow.class)
    @Test
    public void liveRetrieve() {
        ClassificationService liveService = new ClassificationService();

        Material material = liveService.retrieveMaterial(THE_ROAD_ISBN);

        assertMaterialDetailsForTheRoad(material);
    }

    private void assertMaterialDetailsForTheRoad(Material material) {
        assertThat(material.getTitle(), equalTo(THE_ROAD_TITLE));
        assertThat(material.getYear(), equalTo(THE_ROAD_YEAR));
        assertThat(material.getAuthor(), equalTo(THE_ROAD_AUTHOR));
        assertThat(material.getSourceId(), equalTo(THE_ROAD_ISBN));
        assertThat(material.getClassification(), equalTo(THE_ROAD_CLASSIFICATION));
    }
}