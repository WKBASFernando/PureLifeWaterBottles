package com.assignment.purelifewaterbottles.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMailController {

    @FXML
    private TextArea txtBody;

    @FXML
    private TextField txtSubject;

    // Field to set the recipient's email address
    // Lombok @Setter is used to auto-generate a setter method
    @Setter
    private String customerEmail;

    @FXML
    public void sendUsingGmailOnAction(ActionEvent actionEvent) {
        if (customerEmail == null) {
            return;
        }

        // The sender's email address
        final String FROM = "sankalpaangelo@gmail.com";

        // Get the subject and body from the text fields
        String subject = txtSubject.getText();
        String body = txtBody.getText();

        // Check if subject or body is empty; show a warning if they are
        if (subject.isEmpty() || body.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Subject and body must not be empty!").show();
            return;
        }

        // you can use forget password for this
        // UUID - generate random unique id

        /* Use only one method for sending emails ((1) or (2)) */
//        Send Email in Java SMTP with TLS Authentication

        // (1) Gmail with app password (need Gmail 2FA)
        // Call the method to send an email via Gmail
        // Using your gmail account
        // You must enable two-factor authentication
        sendEmailWithGmail(FROM, customerEmail, subject, body);

    }

    /**
     * Method to send an email using Gmail's email service.
     *
     * @param from        the sender's email address.
     * @param to          the recipient's email address.
     * @param subject     the subject of the email.
     * @param messageBody the body text of the email.
     */
    private void sendEmailWithGmail(String from, String to, String subject, String messageBody) {
        // Replace this string with your Gmail app-specific password.
        // You must generate an app password if you have two-factor authentication (2FA) enabled on Gmail.
        String PASSWORD = "ywgx eryr oolo bgzc"; // (watch document)

        // Create a new Properties object to hold configuration settings for the SMTP (Simple Mail Transfer Protocol) connection
        Properties props = new Properties();

        // Enable SMTP authentication. This requires the server to authenticate the sender before sending emails.
        props.put("mail.smtp.auth", "true");

        // Enable STARTTLS, which upgrades an insecure connection to a secure one (TLS encryption).
        props.put("mail.smtp.starttls.enable", "true");

        // Specify the SMTP server host. For Gmail, it is "smtp.gmail.com".
        props.put("mail.smtp.host", "smtp.gmail.com");

        // Specify the port to use for SMTP. Port 587 is used for TLS connections. Alternatively, port 465 can be used for SSL.
        props.put("mail.smtp.port", "587");

        // Create a new session with the SMTP server using the configured properties.
        // The Authenticator is used to authenticate the sender's email using their email address (`from`) and app password (`PASSWORD`).
        Session session = Session.getInstance(props, new Authenticator() {

            // Replace with your email and app password
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, PASSWORD); // Replace with your email and password
            }
        });

        try {
            // Create a new MimeMessage object to represent the email message.
            Message message = new MimeMessage(session);

            // Set the sender's email address using the `from` parameter.
            message.setFrom(new InternetAddress(from));

            // Set the recipient(s) of the email. The `to` parameter is parsed to handle multiple recipients, if necessary.
            // `Message.RecipientType.TO` defines that this is the primary recipient (To field).
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set the subject of the email using the `subject` parameter.
            message.setSubject(subject);

            // Set the body (content) of the email using the `messageBody` parameter.
            message.setText(messageBody);

            // Send the email message using the `Transport.send()` method.
            // This sends the email through the SMTP server configured in the session.
            Transport.send(message);

            new Alert(Alert.AlertType.INFORMATION, "Email sent successfully!").show();
        } catch (MessagingException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to send email.").show();
        }
    }
}
