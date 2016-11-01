package library;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class RestTemplateFactory {

   public static RestTemplate create() {
      RestTemplate template = new RestTemplate();

      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

      MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
      messageConverter.setPrettyPrint(false);
      messageConverter.setObjectMapper(mapper);
      template.getMessageConverters()
         .removeIf(m -> m.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName()));
      template.getMessageConverters().add(messageConverter);

      return template;
   }
}
