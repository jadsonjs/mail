package br.com.jadson.mailframe.client;

import br.com.jadson.mailframe.client.dto.MailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MailClient {

    @Value("${mail.service.url}")
    String url;

    @Value("${mail.jwt.key}")
    String jwtKey;

    public MailDto send(MailDto mailDto) {

        if(mailDto == null){
            throw new IllegalArgumentException("mail data could not be null");
        }

        HttpEntity<MailDto> entity = new HttpEntity<>(mailDto, genarateHeaders());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MailDto> response = restTemplate.exchange( url+"/send", HttpMethod.POST, entity, MailDto.class);
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
        headers.add("Authorization", "key");
        return headers;
    }
}
