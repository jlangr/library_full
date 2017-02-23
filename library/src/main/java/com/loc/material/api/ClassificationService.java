package com.loc.material.api;

import org.springframework.web.client.RestTemplate;
import util.RestUtil;

import java.util.List;
import java.util.Map;

// This class uses the Open Library API; see https://openlibrary.org/dev/docs/api/books

public class ClassificationService implements ClassificationApi {
    public static final String SERVER = "http://openlibrary.org";
    private RestTemplate template;

    public ClassificationService() {
        template = RestUtil.createRestTemplate();
    }

    public ClassificationService(RestTemplate restTemplate) {
        template = restTemplate;
    }

    @Override
    public Material retrieveMaterial(String sourceId) {
        return createMaterial(sourceId, retrieve(sourceId));
    }

    private Material createMaterial(String sourceId, Map<String, Object> response) {
        Material material = new Material();
        material.setSourceId(sourceId);
        material.setFormat(MaterialType.Book);
        material.setTitle(getString(response, "title"));
        material.setYear(getString(response, "publish_date"));
        material.setAuthor(getFirstAuthorName(response));
        material.setClassification(getLibraryOfCongressClassification(response));
        return material;
    }

    private String getLibraryOfCongressClassification(Map<String, Object> response) {
        @SuppressWarnings("unchecked")
        Map<String, Object> classifications = (Map<String, Object>) response.get("classifications");
        List<Object> libraryOfCongressClassifications = getList(classifications, "lc_classifications");
        return (String) libraryOfCongressClassifications.get(0);
    }

    private String getFirstAuthorName(Map<String, Object> map) {
        Map<String, Object> firstAuthor = getMap(getList(map, "authors"), 0);
        return (String) firstAuthor.get("name");
    }

    @SuppressWarnings("unchecked")
    private List<Object> getList(Map<String, Object> map, String key) {
        return (List<Object>) map.get(key);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMap(List<Object> list, int index) {
        return (Map<String, Object>) list.get(index);
    }

    private String getString(Map<String, Object> map, String key) {
        return (String) map.get(key);
    }

    private String url(String doc) {
        return String.format(SERVER + doc);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> retrieve(String sourceId) {
        Map<String, Object> response = template.getForObject(url(findByDoc(isbnKey(sourceId))), Map.class);
        return (Map<String, Object>) response.get(isbnKey(sourceId));
    }

    private String findByDoc(String bibKey) {
        return "/api/books?bibkeys=" + bibKey + "&jscmd=data&format=json";
    }

    String isbnKey(String sourceId) {
        return "ISBN:" + sourceId;
    }
}