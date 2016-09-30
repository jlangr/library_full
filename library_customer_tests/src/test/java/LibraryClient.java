import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import controller.BranchRequest;

public class LibraryClient {
	private RestTemplate template = new RestTemplate();

	public LibraryClient() {
		prepareRestTemplate();
	}

	private void prepareRestTemplate() {
		template = new RestTemplate();

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		messageConverter.setPrettyPrint(false);
		messageConverter.setObjectMapper(mapper);
		template.getMessageConverters().removeIf(m -> m.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName()));
		template.getMessageConverters().add(messageConverter);
	}

	public static final String SERVER = "http://localhost:3003";

	private String url(String doc) {
	   return String.format(SERVER + doc);
	}

   public void addBranch(String name) {
      BranchRequest request = new BranchRequest();
      request.setName(name);
      template.postForEntity(url("/branches"), request, String.class);
   }

   public List<BranchRequest> retrieveBranches(String user) {
      ResponseEntity<BranchRequest[]> response = template.getForEntity(url("/branches"), BranchRequest[].class);
      return Arrays.asList(response.getBody());
   }
}
