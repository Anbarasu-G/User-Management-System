package com.ums.service;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

	private JavaMailSender sender;

	public String mailSender(String email, Integer otp){

		MimeMessage message = sender.createMimeMessage();
		try {

			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

			messageHelper.setTo(email);
			messageHelper.setSubject("Verify the Acccount ");
			messageHelper.setText("Your OTP : " + otp);

			sender.send(message);
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return "Otp Sen to your Gmail !!!";
	}

	public int generateOtp() {

		Random random = new Random();
		return random.nextInt(111111, 999999);
	}
}
