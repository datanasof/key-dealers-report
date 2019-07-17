package mailer;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
 
public class Email {
 
	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
	static Logger logger = Logger.getAnonymousLogger();
	//throws AddressException, MessagingException 
	
	public static String buildTextBody (List <String> promos, List<String> newProducts){
		StringBuilder body = new StringBuilder();
		if(!promos.isEmpty()){
			body.append("Promos detected at: <br>");		
			for(int i=0;i<promos.size();i++){
				body.append(i+1);
				body.append(": "+promos.get(i));
				body.append("<br>");
			}
			body.append("<br>");
		}
		if(!newProducts.isEmpty()){
			body.append("NEW products &/or bundles detected at: <br>");		
			for(int i=0;i<newProducts.size();i++){
				body.append(i+1);
				body.append(": "+newProducts.get(i));
				body.append("<br>");
			}
			body.append("<br>");
		}		
		
		//ADD FUNNY IMAGE/ AVATAR
		body.append("<a href=\"https://media.giphy.com/media/LirPiocYEm8zm/giphy-downsized-large.gif\" target=\"_blank\"><img src=\"https://media.giphy.com/media/LirPiocYEm8zm/giphy.gif\"/></a>");
				
		return body.toString();		
	}
	
	public static String buildTextBodyAvailability (List <String> notAvailable){
		StringBuilder body = new StringBuilder();
		
		body.append("ATTENTION - NO product availability detected at: <br>");		
		for(int i=0;i<notAvailable.size();i++){
			body.append(i+1);
			body.append(": "+notAvailable.get(i));
			body.append("<br>");
		}
		body.append("<br>");
		
		//ADD FUNNY IMAGE/ AVATAR
		body.append("<a href=\"https://media.giphy.com/media/2rtQMJvhzOnRe/giphy.gif\" target=\"_blank\"><img src=\"https://media.giphy.com/media/2rtQMJvhzOnRe/giphy.gif\"/></a>");
		return body.toString();		
	}
 
	public static boolean send(String subject, String emailBody, String... recipients) {
		 
		// Step1
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "25");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");
 
		// Step2
		System.out.println("\n\n 2nd ===> get Mail Session..");
		try{
			getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			generateMailMessage = new MimeMessage(getMailSession);
			//recipients
			for(String recipient : recipients){
				generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			}
			//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("dimitar@antelopeaudio.com"));		
			//SUBJECT
			generateMailMessage.setSubject(subject);
			//mail TEXT		
			generateMailMessage.setContent(emailBody, "text/html");
		} catch(MessagingException e){
			logger.log(Level.SEVERE, "An exception occurred. Mail Session was NOT created..", e);
			e.printStackTrace();
		}		
		System.out.println("Mail Session has been created successfully..");
 
		// Step3
		System.out.println("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = null;
		try{
			transport = getMailSession.getTransport("smtp");
	 
			// Enter your correct gmail UserID and Password		
			transport.connect("smtp.gmail.com", "trackerantelope@gmail.com", "Xsw23edc");			
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			return true;
		} catch(MessagingException e){
			logger.log(Level.SEVERE, "An exception occurred. Mail message was NOT sent correctly..", e);
				
		} finally{
			if(transport != null){
				try{
					transport.close();
				} catch(MessagingException e){
					logger.log(Level.SEVERE, "An exception occurred.", e);
					e.printStackTrace();
				}				
			}			
		}
		return false;
	}
}
