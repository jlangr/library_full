package com.loc.material.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static util.MapUtil.createMap;
import java.util.*;
import org.junit.*;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

public class ClassificationServiceTest {
   @Mock
   private RestTemplate restTemplate;
   @InjectMocks
   private ClassificationService service;

   @Before
   public void create() {
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void retrieveMaterialPopulatesFromResponse() {
      String isbn13 = "9780131482395";
      Map<Object, Object> responseMap = createMap(service.isbnKey(isbn13),
         createMap("title", "Agile Java",
            "publish_date", "2005"));
      when(restTemplate.getForObject(contains(isbn13), eq(Map.class))).thenReturn(responseMap);

      Material material = service.retrieveMaterial(isbn13);

      assertThat(material.getTitle(), equalTo("Agile Java"));
   }
}
