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

   public String addBranch(String name) {
      BranchRequest request = new BranchRequest();
      request.setName(name);
      ResponseEntity<String> response = template.postForEntity(url("/branches"), request, String.class);
      return response.getBody();
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
      return asList(response.getBody());
   }

   public String addHolding(String sourceId, String branchScanCode) {
      AddHoldingRequest request = new AddHoldingRequest();
      request.setBranchScanCode(branchScanCode);
      request.setSourceId(sourceId);
      ResponseEntity<String> response = template.postForEntity(url("/holdings"), request, String.class);
      return response.getBody();
   }

   public void clear() {
      template.postForEntity(url("/clear"), null, null);
   }

   public void checkOutHolding(String patronId, String barcode, Date date) {
      CheckoutRequest request = new CheckoutRequest();
      request.setPatronId(patronId);
      request.setHoldingBarcode(barcode);
      request.setCheckoutDate(date);
      template.postForEntity(url("/holdings/checkout"), request, String.class);
   }

   public HoldingResponse retrieveHolding(String holdingBarcode) {
      ResponseEntity<HoldingResponse> response =
         template.getForEntity(url("/holdings/" + holdingBarcode), HoldingResponse.class);
       return response.getBody();
   }
}
