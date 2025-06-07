package com.ayu.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	boolean isSent = false;
	public boolean sendMail(String to,String subject,String body)
	{
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg,  MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
			
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body,true);
//			SimpleMailMessage msg = new SimpleMailMessage();
//			msg.setTo(to);
//			msg.setSubject(subject);
//			msg.setText(body);
			
			mailSender.send(msg);
			
			isSent = true;
			System.out.println("Mail wle method ka execution hogy");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return isSent;
		
	}
}
