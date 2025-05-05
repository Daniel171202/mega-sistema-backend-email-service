package sis_ucb.online.megasistema.email_service.bl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import sis_ucb.online.megasistema.email_service.dao.EmailServiceRepository;
import sis_ucb.online.megasistema.email_service.dto.EmailServiceDTO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Service
public class EmailServiceBl implements EmailServiceRepository {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceBl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }
   @Override
public void sendMailInvitation(EmailServiceDTO emailServiceDTO) {
    List<String> recipients = emailServiceDTO.getRecipients();
    int totalRecipients = recipients.size();

    // Máximo 20 hilos para evitar sobrecarga
    int threadCount = Math.min(totalRecipients, 20);
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    // Lista de correos que fallaron (segura para hilos)
    List<String> failedRecipients = Collections.synchronizedList(new ArrayList<>());

    CountDownLatch latch = new CountDownLatch(totalRecipients);

    for (String recipient : recipients) {
        executor.submit(() -> {
            try {
                sendSingleEmail(recipient, emailServiceDTO);
            } catch (Exception e) {
                System.err.println("Error al enviar a " + recipient + ": " + e.getMessage());
                failedRecipients.add(recipient);
            } finally {
                latch.countDown();
            }
        });
    }

    executor.shutdown();

    try {
        latch.await(); // Esperar que terminen todos
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

    // Intentar reenviar los fallidos una vez más
   
    // Intentar reenviar los fallidos después de 3 minutos
if (!failedRecipients.isEmpty()) {
    System.out.println("Esperando 3 minutos antes de reintentar correos fallidos...");
    
    new Thread(() -> {
        try {
            Thread.sleep(180_000); // Esperar 3 minutos (180,000 milisegundos)
            System.out.println("Reintentando correos fallidos...");

            ExecutorService retryExecutor = Executors.newFixedThreadPool(Math.min(failedRecipients.size(), 10));
            List<String> stillFailed = Collections.synchronizedList(new ArrayList<>());
            CountDownLatch retryLatch = new CountDownLatch(failedRecipients.size());

            for (String recipient : failedRecipients) {
                retryExecutor.submit(() -> {
                    try {
                        sendSingleEmail(recipient, emailServiceDTO);
                    } catch (Exception e) {
                        System.err.println("Reintento fallido para: " + recipient);
                        stillFailed.add(recipient); // guardar definitivamente
                    } finally {
                        retryLatch.countDown();
                    }
                });
            }

            retryExecutor.shutdown();
            retryLatch.await();

            if (!stillFailed.isEmpty()) {
                System.err.println("Los siguientes correos fallaron permanentemente:");
                stillFailed.forEach(System.err::println);
            }

            System.out.println("Reintento de correos finalizado.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Reintento interrumpido.");
        }
    }).start(); // Lanzar hilo separado para esperar y reintentar
}

    System.out.println("Proceso de envío de correos finalizado.");
}

// Método para enviar un solo correo
private void sendSingleEmail(String recipient, EmailServiceDTO dto) throws Exception {
    var mimeMessage = javaMailSender.createMimeMessage(); // NUEVO: crear mensaje aquí
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    helper.setTo(recipient);
    helper.setSubject(dto.getSubject());

    Context context = new Context();
    context.setVariable("mensaje", dto.getBody());
    String contentHTML = templateEngine.process("prueba", context);
    System.out.println("[" + Thread.currentThread().getName() + "] Enviando correo a: " + recipient);

    helper.setText(contentHTML, true);
    javaMailSender.send(mimeMessage); // usar el mensaje de este hilo
}

public void sendEmailWithHTML(EmailServiceDTO emailServiceDTO) {
    List<String> recipients = emailServiceDTO.getRecipients();
    int totalRecipients = recipients.size();

    int threadCount = Math.min(totalRecipients, 20);
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    List<String> failedRecipients = Collections.synchronizedList(new ArrayList<>());
    CountDownLatch latch = new CountDownLatch(totalRecipients);

    for (String recipient : recipients) {
        executor.submit(() -> {
            try {
                sendSingleEmailWithHTML(recipient, emailServiceDTO);
            } catch (Exception e) {
                System.err.println("Error al enviar a " + recipient + ": " + e.getMessage());
                failedRecipients.add(recipient);
            } finally {
                latch.countDown();
            }
        });
    }

    executor.shutdown();

    try {
        latch.await();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

    // Reintento después de 3 minutos
    if (!failedRecipients.isEmpty()) {
        System.out.println("Esperando 3 minutos antes de reintentar correos fallidos (HTML)...");

        new Thread(() -> {
            try {
                Thread.sleep(180_000); // 3 minutos

                System.out.println("Reintentando correos fallidos (HTML)...");
                ExecutorService retryExecutor = Executors.newFixedThreadPool(Math.min(failedRecipients.size(), 10));
                CountDownLatch retryLatch = new CountDownLatch(failedRecipients.size());
                List<String> stillFailed = Collections.synchronizedList(new ArrayList<>());

                for (String recipient : failedRecipients) {
                    retryExecutor.submit(() -> {
                        try {
                            sendSingleEmailWithHTML(recipient, emailServiceDTO);
                        } catch (Exception e) {
                            System.err.println("Reintento fallido para: " + recipient);
                            stillFailed.add(recipient);
                        } finally {
                            retryLatch.countDown();
                        }
                    });
                }

                retryExecutor.shutdown();
                retryLatch.await();

                if (!stillFailed.isEmpty()) {
                    System.err.println("Los siguientes correos HTML fallaron permanentemente:");
                    stillFailed.forEach(System.err::println);
                    
                }

                System.out.println("Reintento de correos HTML finalizado.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Reintento interrumpido.");
            }
        }).start();
    }

    System.out.println("Proceso de envío de correos HTML finalizado.");
}


    // Método para enviar un solo correo con HTML directamente
    private void sendSingleEmailWithHTML(String recipient, EmailServiceDTO dto) throws Exception {
        var mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(recipient);
        helper.setSubject(dto.getSubject());

        // Enviar el HTML directamente
        String contentHTML = dto.getBody(); // Usar el HTML proporcionado en el body
        System.out.println("[" + Thread.currentThread().getName() + "] Enviando correo a: " + recipient);

        helper.setText(contentHTML, true);
        javaMailSender.send(mimeMessage); // Enviar el correo
    }


    @Override
    public void sendEmail(EmailServiceDTO emailServiceDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendEmail'");
    }


}
