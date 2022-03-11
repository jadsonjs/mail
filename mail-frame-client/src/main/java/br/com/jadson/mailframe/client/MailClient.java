package br.com.jadson.mailframe.client;

import br.com.jadson.mailframe.client.dtos.MailDto;
import br.com.jadson.mailframe.client.security.JwtManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MailClient {

    @Value("${mail.service.url}")
    String url;

    @Autowired
    JwtManager jwt;

    public MailDto send(MailDto mailDto) {

        if(mailDto == null){
            throw new IllegalArgumentException("mail data could not be null");
        }

        HttpEntity<MailDto> entity = new HttpEntity<>(mailDto, genarateHeaders());

        RestTemplate restTemplate = new RestTemplate();
        String rabbitmqEndpoint = "/send/mq";
        String kafkaEndpoint = "/send/kafka";
        ResponseEntity<MailDto> response = restTemplate.exchange( url+kafkaEndpoint, HttpMethod.POST, entity, MailDto.class);
        if(response.getStatusCode() == HttpStatus.CREATED ) {
            return response.getBody();
        }

        throw new RuntimeException(response.getBody().toString());
    }

    /**
     * Generate headers with JWT key for authentication
     * @return
     */
    private HttpHeaders genarateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer "+jwt.generateKey());
        return headers;
    }
}
