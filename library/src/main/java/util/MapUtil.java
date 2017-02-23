package util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtil {
    public static Map<Object, Object> createMap(Object... keyValuePairs) {
        Map<Object, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2)
            map.put(keyValuePairs[i], keyValuePairs[i + 1]);
        return map;
    }
}