package br.com.jadson.mailframe.converters;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Named("Base64Converter")
public interface Base64Converter {


    @Named("base64ToBytes")
    private byte[] base64ToBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    @Named("bytesToBase64")
    private String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
