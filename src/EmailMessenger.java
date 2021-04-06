import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

import java.net.URI;
import java.net.URISyntaxException;

public class EmailMessenger {

	static private Logger log = Logger.getLogger(EmailMessenger.class.getName());

	private EmailMessenger(String msg) {
		if (msg.equals("e"))
			init2();

		if (msg.equals("n"))
			init3();
	}

	private EmailMessenger() throws Exception {

	}

// ews [s]
	String ewsUrl = null;
	String ewsEmailAddress = null;
	String ewsPassword = null;
	ExchangeService service = null;
	ExchangeCredentials credentials = null;

	private void init2() {
		this.ewsUrl = "https://outlook.office365.com/ews/exchange.asmx"; // https://myUrl/ews/exchange.asmx
		this.ewsEmailAddress = "userid@gmail.com"; //a gmail id which has been used to open account in microsoft
		this.ewsPassword = "*****"; // password of that microsoft account
		this.service = new ExchangeService(ExchangeVersion.Exchange2010); // your server version
		this.credentials = new WebCredentials(this.ewsEmailAddress, this.ewsPassword); // email username, password
		this.service.setCredentials(credentials);
		try {
			this.service.setUrl(new URI(this.ewsUrl));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void sendEmail(String[] toAddress, String[] ccAddress, String emailSubject, String emailBody,
			String[] attachmentPath) throws MessagingException, FileNotFoundException, Exception {

		EmailMessage msg = new EmailMessage(this.service);
		msg.setSubject(emailSubject);
		MessageBody body = MessageBody.getMessageBodyFromText(emailBody);
		body.setBodyType(BodyType.HTML);
		msg.setBody(body);
		for (String s : toAddress) {
			msg.getToRecipients().add(s); // email receiver
		}

		if (ccAddress != null) {
			for (String s : ccAddress) {
				msg.getCcRecipients().add(s); // email cc recipients
			}
		}

		/*
		 * if (attachmentPath != null && attachmentPath.length > 0) { for (int a = 0; a
		 * < attachmentPath.length; a++) {
		 * msg.getAttachments().addFileAttachment(attachmentPath[a]); } }
		 */
		// msg.getAttachments().addFileAttachment("G:\\Daihan\\Java TODO.txt");
		msg.send(); // 1. sends email but don't save in sent item folder 2. Hide sender email
		// msg.sendAndSaveCopy(); //1. Sends this e-mail message and saves a copy of it
		// in the Sent Items folder. 2. Shows sender email
		log.info("Mail sent successfully via EWS");
	}
// ews [e]

// normal smtp[s]
	private String smtpHost = null;
	private String smtpFrom = null;
	private String smtpPass = null;
	private Session smtpSession = null;

	private void init3() {
		Properties props = System.getProperties();
		this.smtpHost = "smtp.gmail.com";//  mail server
		this.smtpFrom = "userid@gmail.com";// gmail id
		this.smtpPass = "*****";// gmail id pass

		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", this.smtpHost);
		props.put("mail.smtp.user", this.smtpFrom);
		props.put("mail.smtp.password", this.smtpPass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		this.smtpSession = Session.getDefaultInstance(props);
	}

	public void sendEmail(String emailSubject, String emailBody, String[] to, String[] cc, String[] bcc,
			String[] attachmentPath) throws Exception {
		MimeMessage message = new MimeMessage(this.smtpSession);

		try {
			message.setFrom(new InternetAddress(this.smtpFrom, "National University of Singapore")); // from , view

			// set to
			if (to != null) {
				InternetAddress[] toAddress = new InternetAddress[to.length];
				for (int i = 0; i < to.length; i++) {
					toAddress[i] = new InternetAddress(to[i]);
				}
				for (int i = 0; i < toAddress.length; i++) {
					message.addRecipient(Message.RecipientType.TO, toAddress[i]);
				}
			}

			// set cc
			if (cc != null) {
				InternetAddress[] ccAddress = new InternetAddress[cc.length];
				for (int i = 0; i < cc.length; i++) {
					ccAddress[i] = new InternetAddress(cc[i]);
				}

				for (int i = 0; i < ccAddress.length; i++) {
					message.addRecipient(Message.RecipientType.CC, ccAddress[i]);
				}
			}

			// set bcc
			if (bcc != null) {
				InternetAddress[] bccAddress = new InternetAddress[bcc.length];
				for (int i = 0; i < bcc.length; i++) {
					bccAddress[i] = new InternetAddress(bcc[i]);
				}

				for (int i = 0; i < bccAddress.length; i++) {
					message.addRecipient(Message.RecipientType.BCC, bccAddress[i]);
				}
			}

			message.setSubject(emailSubject);
			message.setText(emailBody);
			Transport transport = this.smtpSession.getTransport("smtp");
			transport.connect(this.smtpHost, this.smtpFrom, this.smtpPass);
			transport.sendMessage(message, message.getAllRecipients());
			log.info("Mail sent successfully via SMTP");
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
// normal smtp[e]	

	public static void main(String args[]) {
		String[] toAddress ={"userid1@gmail.com","userid2@gmail.com"};
		String[] ccAddress ={"userid3@gmail.com","userid4@gmail.com"};
		String[] bccAddress = { "userid5@gmail.com", "userid6@gmail.com" };
		try {
			// ews [s]
			EmailMessenger app =new EmailMessenger("e");
		    for(int i=71;i<=73;i++){
		    	String emailSubject = "Final ews "+i;
		    	String emailBody = "Final body "+i;
		    	app.sendEmail(toAddress,ccAddress,emailSubject, emailBody,null);
		    }
		    
			EmailMessenger var=new EmailMessenger("e");
			for(int i=74;i<=76;i++){
		    	String emailSubject ="ews another object testing - Subject "+i;
		    	String emailBody = "ews another object testing - Body "+i;
			    var.sendEmail(toAddress, ccAddress,emailSubject,emailBody,null);
		    }
			// ews [e]

			// normal smtp [s]
			EmailMessenger var1 = new EmailMessenger("n");
			for (int i = 95; i <= 97; i++) {
				String emailSubject = "smtp subject " + i;
				String emailBody = "smtp body " + i;
				var1.sendEmail(emailSubject, emailBody, toAddress, ccAddress, bccAddress, null);
			}

			EmailMessenger var2 = new EmailMessenger("n");
			for (int i = 98; i <=100; i++) {
				String emailSubject = "another object smtp subject " + i;
				String emailBody = "another object smtp body " + i;
				var2.sendEmail(emailSubject, emailBody, toAddress, ccAddress, bccAddress, null);
			}
			// normal smtp [e]

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
