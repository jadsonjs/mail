package br.com.jadson.mailframe.converters;

import br.com.jadson.mailframe.client.dtos.MailDto;
import br.com.jadson.mailframe.models.Mail;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {StringConverter.class, UUIDConverter.class, AttachmentsConverter.class})
public interface MailConverter {

    @Mapping(source = "application", target = "applicationName")
    @Mapping(source = "to", target = "to", qualifiedByName = { "StringConverter", "listToString" })
    @Mapping(source = "cc", target = "cc", qualifiedByName = { "StringConverter", "listToString" })
    @Mapping(source = "bcc", target = "bcc", qualifiedByName = { "StringConverter", "listToString" })
    @Mapping( target = "sendDate", ignore = true)
    @Mapping( target = "status", ignore = true)
    @Mapping( target = "error", ignore = true)
    Mail toModel(MailDto dto);

    @Mapping(source = "applicationName", target = "application")
    @Mapping(source = "to", target = "to", qualifiedByName = { "StringConverter", "stringToList" })
    @Mapping(source = "cc", target = "cc", qualifiedByName = { "StringConverter", "stringToList" })
    @Mapping(source = "bcc", target = "bcc", qualifiedByName = { "StringConverter", "stringToList" })
    @InheritInverseConfiguration
    MailDto toDto(Mail dto);
}
