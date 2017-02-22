package testutil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class Asserts {
    interface StockLookupSvc {
        int price(String symbol);
    }

    @Test
    public void stuff() {
        boolean condition = false;
        int idleSpeed = 1000;
        String text = "something";
        String otherText = "";
        List<String> tokens = new ArrayList<>();
        tokens.add("alpha");
        tokens.add("beta");
        tokens.add("gamma");
        tokens.add("delta");
        class AutoEngine {
        }
        ;
        AutoEngine obj = new AutoEngine();


        assertThat(condition, is(false));
        assertThat(text, is(equalTo("something")));
        assertThat(idleSpeed, is(equalTo(1000)));
        assertThat(idleSpeed, is(both(greaterThan(950)).and(lessThan(1100))));
        assertThat(otherText, is(not(equalTo("something"))));
        assertThat(otherText, is(isEmptyOrNullString()));
        assertThat(tokens, hasSize(4));
        assertThat(tokens, hasItem("beta"));
        assertThat(tokens, contains("alpha", "beta", "gamma", "delta"));
        assertThat(tokens, containsInAnyOrder("gamma", "alpha", "beta", "delta"));
        assertThat(obj, is(instanceOf(AutoEngine.class)));
    }

    class Portfolio {
        private StockLookupSvc service;

        Portfolio(StockLookupSvc service) {
            this.service = service;
        }

        void purchase(String symbol, int shares) {

        }

        public int value() {
            return service.price("IBM");
        }
    }

    @Test
    public void mockTest() {
        StockLookupSvc service = mock(StockLookupSvc.class);
        org.mockito.Mockito.when(
                service.price("IBM")).thenReturn(50);

        assertThat(service.price("IBM"), is(equalTo(50)));

    }

    class Auditor {
        public void recordEvent(String s) {
        }

        public void initialize() {
        }
    }

    public class Scanner {
        private Auditor auditor;

        public Scanner(Auditor auditor) {
            this.auditor = auditor;
        }

        public void scan(String upc) {
            // how do we verify this occurred?
            auditor.recordEvent("scanned:" + upc);
        }
    }

    @Test
    public void recordsAuditEventWhenScanned() {
        Auditor auditor = mock(Auditor.class);
        Scanner scanner = new Scanner(auditor);

        scanner.scan("123");

        verify(auditor).recordEvent("scanned:123");
    }

    private Auditor auditor = new Auditor();

    public void Add(String word, String definition) {
        auditor.initialize();
        auditor.recordEvent(String.format("adding %s:%s", word, definition));
    }

    @Test
    public void recordsAuditEventWhenScannedX() {
        Auditor auditor = mock(Auditor.class);
        Scanner scanner = new Scanner(auditor);

        scanner.scan("123");

        verify(auditor).recordEvent("scanned:123");
    }

    class Purchase {
        private String description;
        private int amount;

        public Purchase(String description, int amount) {
            this.description = description;
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public int getAmount() {
            return amount;
        }
    }

    static class DisplayDevice {
        static void appendMessage(String message) {
            throw new RuntimeException("nope");
        }
    }

    class Register {
        private List<Purchase> purchases = new ArrayList<>();
        private int total;

        int getTotal() {
            return total;
        }

        public void completeSale() {
            total = 0;
            for (Purchase purchase : purchases) {
                String message = "item" + purchase.getDescription();
                total += purchase.getAmount();
                appendMessage(message);
            }
        }

        void appendMessage(String message) {
            DisplayDevice.appendMessage(message);
        }

        public void purchase(Purchase item) {
            purchases.add(item);
        }
    }

    @Test
    public void itestCompleteSale() {
        Register register = new Register() {
            @Override
            void appendMessage(String m) {
            }
        };
        register.purchase(new Purchase("milk", 42));

        register.completeSale();

        assertThat(register.getTotal(), is(equalTo(42)));
    }

    class VerificationService {
    }

    class Verifier {
        private final VerificationService verificationService;
        private final int timeout;

        public Verifier(int timeout) {
            this(timeout, new VerificationService());
        }

        public Verifier(int timeout, VerificationService verificationService) {
            this.verificationService = verificationService;
            this.timeout = timeout;
        }
    }
}

