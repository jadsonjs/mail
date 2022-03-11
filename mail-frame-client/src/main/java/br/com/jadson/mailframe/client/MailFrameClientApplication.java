package br.com.jadson.mailframe.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

@SpringBootApplication
public class MailFrameClientApplication implements CommandLineRunner {

	@Autowired
	MailClient client;
	

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MailFrameClientApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {


//		File f1 = new File("/Users/jadson/Desktop/comprovante.pdf");
//		File f2 = new File("/Users/jadson/Desktop/comprovante.pdf");
//
//		byte[] fileBytes = new byte[100];
//
//		//for (int i = 1; i <= 100; i++) {
//
//			client.send(new MailDtoBuilder("xxxxxxxxxxxxxx@gmail.com",
//					"Email Test",
//					" This is an email with limit" +
//					" <br><br>" +
//					" <p> <i>This is the body</i> </p>",
//					"mail-client")
//					.build());
//
//
//			client.send(new MailDtoBuilder("xxxxxxxxxxxxxx@gmail.com",
//					Arrays.asList("to1@gmail.com", "to2@gmail.com", "to3@gmail.com"),
//					Arrays.asList("cc1@gmail.com", "cc2@gmail.com", "cc3@gmail.com"),
//					Arrays.asList("bcc1@gmail.com", "bcc2@gmail.com", "bcc3@gmail.com"),
//					"noReply@gmail.com",
//					"Email Test",
//					" This is an email with limit" +
//							" <br><br>" +
//							" <p> <i>This is the body</i> </p>",
//					"mail-client")
//					.build());

		//}

		client.send(new MailDtoBuilder(
					"jadson.santos@ufrn.br",
					"Email Test",
					" This is an email using apache kafka" +
							" <br><br>" +
							" <p> <i>This is the body</i> </p>",
					"mail-client")
					.build());

	}
}
