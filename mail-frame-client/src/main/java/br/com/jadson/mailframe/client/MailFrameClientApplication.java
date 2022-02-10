package br.com.jadson.mailframe.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
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


		//File f1 = new File("/Users/jadson/Desktop/comprovante.pdf");
		//File f2 = new File("/Users/jadson/Desktop/comprovante.pdf");

		//for (int i = 1; i <= 100; i++) {

//			client.send(new MailDtoBuilder("xxxxxxxxxxxxxx@gmail.com",
//					("Email Test"), " <p> This is an email with limit</p>" +
//					" <br><br>" +
//					" <p> <i>This is the body</i> </p>", "mail-client")
//					//.addFile("comprovante1.pdf", f1)
//					//.addFile("comprovante2.pdf", f2)
//					.build());

		//}

	}
}
