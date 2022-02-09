package br.com.jadson.mailframe.client;

import br.com.jadson.mailframe.client.security.JwtManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

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


		File f1 = new File("/Users/jadson/Desktop/comprovante.pdf");
		File f2 = new File("/Users/jadson/Desktop/comprovante.pdf");


		client.send(new MailDtoBuilder("jadsonjs_gmail.com", "jadson.santos@ufrn.br",
				"Test by Client", " <p> <b>This is a test </b> </p>"+
				" <br><br>"+
				" <p> <i>This is the body</i> </p>", "mail-client")
				.addFile("comprovante1.pdf", f1)
				.addFile("comprovante2.pdf", f2)
				.build());

		System.out.println("Mail was sent");

	}
}
