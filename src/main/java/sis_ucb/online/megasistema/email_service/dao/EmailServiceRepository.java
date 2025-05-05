package sis_ucb.online.megasistema.email_service.dao;

import sis_ucb.online.megasistema.email_service.dto.EmailServiceDTO;
public interface EmailServiceRepository {
    public void sendEmail(EmailServiceDTO emailServiceDTO) ;

    public void sendMailInvitation(EmailServiceDTO email);
}
