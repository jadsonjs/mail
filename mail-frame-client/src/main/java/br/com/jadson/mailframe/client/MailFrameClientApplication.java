package br.com.jadson.mailframe.client;

import br.com.jadson.mailframe.client.dto.MailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

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


		MailDto dto = new MailDto("jadsonjs@gmail.com", "jadson.santos@ufrn.br",
				"Test by Client", " <p> <b>This is a test </b> </p>"+
				" <br><br>"+
				" <p> <i>This is the body</i> </p>", "mail-client");

		client.send(dto);

		System.out.println("Mail sent");

	}
}
