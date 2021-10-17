package com.smart.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

// Did you switch on less secure app from gmail account
// https://myaccount.google.com/lesssecureapps
@Service
public class EmailService {
	// Did you switch on less secure app from gmail account
	// https://myaccount.google.com/lesssecureapps
	public boolean sendEmail(String message, String subject, String to) {

		boolean f = false;
		// Variable for gmail
		String host = "smtp.gmail.com";
		String from = "arrayjson@gmail.com";

		// get the system properties
		Properties properties = System.getProperties();
		System.out.println("PROPERTIES " + properties);

		// setting important information to properties object

		// host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Step 1: to get the session object..
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication("arrayjson@gmail.com", "enter password");
			}

		});

		session.setDebug(true);

		// Step 2 : compose the message [text,multi media]
		MimeMessage m = new MimeMessage(session);

		try {

			// from email
			m.setFrom(from);

			// adding recipient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// adding subject to message
			m.setSubject(subject);

			// adding text to message
//			m.setText(message);
			
			// adding html to message
			m.setContent(message, "text/html");

			// send

			// Step 3 : send the message using Transport class
			Transport.send(m);

			System.out.println("Sent success...................");
			f = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;

	}
}
