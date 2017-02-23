package util;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestUtil {
    public static RestTemplate createRestTemplate() {
        RestTemplate template = new RestTemplate();

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setPrettyPrint(false);
        template.getMessageConverters()
                .removeIf(m -> m.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName()));

        template.getMessageConverters().add(messageConverter);

        return template;
    }
}
