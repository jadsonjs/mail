package br.com.jadson.mailframe.converters;

import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class UUIDConverter {

    public String toString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    public UUID toUUID(String uuid) {
        return uuid != null ? UUID.fromString(uuid) : null;
    }
}
