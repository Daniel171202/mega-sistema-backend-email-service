package sis_ucb.online.megasistema.email_service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sis_ucb.online.megasistema.email_service.dto.EmailServiceDTO;
import sis_ucb.online.megasistema.email_service.bl.EmailServiceBl;

@RestController
@RequestMapping("/api/v1/auth")
public class EmailServiceApi {

    @Autowired
    private EmailServiceBl emailServiceBl;

    @PostMapping("/send-email-invitation")
    public ResponseEntity<String> sendMailInvitation(@RequestBody EmailServiceDTO email) {
        try {
            emailServiceBl.sendMailInvitation(email);
            return new ResponseEntity<>("Email enviado exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error sending email: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send-email-html")
    public ResponseEntity<String> sendEmailWithHTML(@RequestBody EmailServiceDTO email) {
        try {
            emailServiceBl.sendEmailWithHTML(email);
            return new ResponseEntity<>("Email enviado exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error sending email: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
