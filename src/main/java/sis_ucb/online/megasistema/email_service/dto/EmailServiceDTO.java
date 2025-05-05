package sis_ucb.online.megasistema.email_service.dto;
import java.util.List;

public class EmailServiceDTO {
    private List<String> recipients;
    private String subject;
    private String body;

    public EmailServiceDTO(List<String> recipients, String subject, String body) {
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "EmailServiceDTO{" +
                "recipients=" + recipients +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
