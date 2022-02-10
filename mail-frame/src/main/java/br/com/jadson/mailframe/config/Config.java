package br.com.jadson.mailframe.config;

import br.com.jadson.mailframe.models.MailCounter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;

@Configuration
public class Config {

    @Bean
    @ApplicationScope
    public MailCounter mailCounter() {
        return new MailCounter();
    }
}
