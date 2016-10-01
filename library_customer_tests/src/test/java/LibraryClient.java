import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import controller.*;
import static java.util.Arrays.asList;

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
      return asList(response.getBody());
   }

   public String addPatron(String name) {
      PatronRequest request = new PatronRequest();
      // careful--something can't handle an overloaded single-arg ctor
      request.setName(name);
      ResponseEntity<String> response = template.postForEntity(url("/patrons"), request, String.class);
      return response.getBody();
   }

   public List<PatronRequest> retrievePatrons() {
      ResponseEntity<PatronRequest[]> response = template.getForEntity(url("/patrons"), PatronRequest[].class);
      System.out.println("response:"+ Arrays.toString(response.getBody()));
      List<PatronRequest> asList2 = asList(response.getBody());
      System.out.println("response:"+ asList2);
      return asList2;
   }

   public void clear() {
      template.postForEntity(url("/clear"), null, null);
   }
}
