package com.task.mgmt.services;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String sender;

  public void sendEmail(String to, String subject, String htmlContent) {

    try {

      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

      // Setting up necessary details
      helper.setFrom(sender);
      helper.setText(htmlContent, true);
      helper.setSubject(subject);
      helper.setTo(InternetAddress.parse(to));
      mailSender.send(mimeMessage);
    } catch (Exception ex) {
      log.error("error while sending email to {} ,{} ", to, ex);
    }
  }
}
