package com.deeplogics.cloud;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import com.deeplogics.cloud.model.Users;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.sendgrid.SendGrid.Email;

/**
 * A class holding all of the html text for each use case
 * Examples: Account Activation, Forgot Password etc.
 * 
 * Also containing the send Email functionality.
 * @author Gabriel
 *
 */
public class EmailMessages {
	
	private Users user;
	private String token;
	
	public EmailMessages(Users user) {
		this.user = user;
	}
	
	public EmailMessages(Users user, String token) {
		this.user = user;
		this.token = token;
	}
	/**
	 * Sends an email
	 * @param subject
	 * @param to
	 * @param action only {"Account Activation", "Forgot Password, "Generate Password"} are valid for now.
	 * @throws IOException
	 */
	public void sendEmail(String subject, String to, String action) throws IOException{
		//For SendGrid API
		SendGrid sendgrid = buildSendGrid();
		
		//Setting properties for email
		Email sendEmail = new Email();
		sendEmail.addTo(to);
		sendEmail.setFrom("no-reply@SpringHibernate.com");
		sendEmail.setFromName("SpringHibernate");
		sendEmail.setSubject(subject);
		String content;
		switch(action) {
			case "Forgot Password": 
				content = htmlTextForForgotPassword(user);
				break;
			case "Account Activation":
				content = htmlTextForAccountActivation(user);
				break;
			case "Generate Password":
				content = htmlTextForGeneratedPass(token);
			default:
				content = "";
		}
		sendEmail.setHtml(content);
		
		try {
			sendgrid.send(sendEmail);
		} catch (SendGridException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * builds a SendGrid Object using the application.properties
	 * sendgrid.username and sendgrid.pass
	 * @return SendGrid Object
	 * @throws IOException
	 */
	private SendGrid buildSendGrid() throws IOException{
		Properties prop = new Properties();
		String propFileName = "application.properties";
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		 
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		
		SendGrid sendgrid = new SendGrid(
				prop.getProperty("sendgrid.username"), 
				prop.getProperty("sendgrid.pass"));
		
		return sendgrid;
	}
	
	/**
	 * Temporary Method creates html text containing the generated pass
	 * @param password
	 * @return
	 */
	private String htmlTextForGeneratedPass(String password){
		StringBuilder fullHtml = new StringBuilder();
		fullHtml.append("<h1>SpringHibernate</h1>");
		fullHtml.append("<p>You changed the password for your SpringHibernate Account.</p>");
		fullHtml.append("<p>Here are your credentials</p>");
		fullHtml.append("<ul><li>Password: " + password + "</li></ul>");
		fullHtml.append("<p>Now you can login to your account</p><br>");
		
		return fullHtml.toString();
	}
	
	private String htmlTextForAccountActivation(Users user) throws UnsupportedEncodingException{
		StringBuilder fullHtml = new StringBuilder();
		fullHtml.append("<h1>SpringHibernate</h1>");
		fullHtml.append("<p>Hello " + user.getFirstName() + "</p>");
		fullHtml.append("<p>You have just registered an account on SpringHibernate. " +
				"Please verify that this is your email address by clicking the link below</p>");
		fullHtml.append("<a href=\"" + "https://localhost:8443/users/activate");//UsersSvcApi.ACTIVATEACC_URL);
		fullHtml.append("?email=" + URLEncoder.encode(user.getEmail(), "UTF-8")
				+ "&uid=" + user.getId()
				+ "&token=" + token + "\">");
		fullHtml.append("Click here to Verify Email.</a>");
		fullHtml.append("<h4>You didn't create this account?</h4>");
		fullHtml.append("Inform us of this immediately by clicking ");
		fullHtml.append("<a href=\"https://SpringHibernate.com/report\">here</a><br>");
		return fullHtml.toString();
	}
	
	/**
	 * Writes the Html Text for the ForgotPassword email for the user
	 * containing the URL with the user's id, email and generated token.
	 * @param user
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String htmlTextForForgotPassword(Users user) throws UnsupportedEncodingException{
		
		StringBuilder fullHtml = new StringBuilder();
		fullHtml.append("<h1>SpringHibernate</h1>");
		fullHtml.append("<p>Someone recently requested a password change in your SpringHibernate Account</p>");
		fullHtml.append("<br><a href=\"http://localhost:8000" 
				+ "?email=" + URLEncoder.encode(user.getEmail(), "UTF-8") 
				+ "&uid=" + user.getId() 
				+ "&token=" + token);
		fullHtml.append("\">Click here to change your password</a>");
		fullHtml.append("<br><br>");
		fullHtml.append("<h4>You didn't ask for this change?</h4>");
		fullHtml.append("Inform us of this immediately by clicking ");
		fullHtml.append("<a href=\"https://SpringHibernate.com/report\">here</a><br>");
		
		return fullHtml.toString();
	}
	
	
}
