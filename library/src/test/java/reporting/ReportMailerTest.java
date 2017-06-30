package reporting;

import org.junit.Before;
import org.junit.Test;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static reporting.ReportMailer.*;

public class ReportMailerTest {
    private Report report;

    @Before
    public void create() {
        report = mock(Report.class);
    }

    @Test
    public void stub() {
        MailDestination mailDestination = new MailDestination("gettysburg");
        MailDestination[] destinations = {
                mailDestination
        };
        ReportMailer mailer = new ReportMailer(destinations) {
            @Override
            protected Endpoint getEndpoint(MailDestination destination) {
                return new Endpoint();
            }
        };
    }

    @Test
    public void constructMailMessageReturnsRecipient() throws MessagingException {
        when(report.getName()).thenReturn("name");
        when(report.getText()).thenReturn("text");

        Message message = constructMailMessageStatic("jeff@x.com", report, null);

        Address[] allRecipients = message.getAllRecipients();
        Optional<String> first = Arrays.stream(allRecipients)
                .map(address -> address.toString())
                .findFirst();
        assertThat(first.get(), equalTo("jeff@x.com"));
    }

    @Test
    public void constructMailMessageReturnsReportNameAndContent() throws MessagingException, IOException {
        when(report.getName()).thenReturn("subject");
        when(report.getText()).thenReturn("text");

        Message message = constructMailMessageStatic("jeff@x.com", report, null);

        assertThat(message.getSubject(), equalTo("subject"));
        assertThat(message.getContent(), equalTo("text"));
    }
}
