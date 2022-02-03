package br.com.jadson.mailframe.converters;

import br.com.jadson.mailframe.dtos.MailDto;
import br.com.jadson.mailframe.models.Mail;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {StringConverter.class, UUIDConverter.class, AttachmentsConverter.class})
public interface MailConverter {

    @Mapping(source = "to", target = "to", qualifiedByName = { "StringConverter", "arrayToString" })
    @Mapping(source = "cc", target = "cc", qualifiedByName = { "StringConverter", "arrayToString" })
    @Mapping(source = "bcc", target = "bcc", qualifiedByName = { "StringConverter", "arrayToString" })
    Mail toModel(MailDto dto);

    @Mapping(source = "to", target = "to", qualifiedByName = { "StringConverter", "stringToArray" })
    @Mapping(source = "cc", target = "cc", qualifiedByName = { "StringConverter", "stringToArray" })
    @Mapping(source = "bcc", target = "bcc", qualifiedByName = { "StringConverter", "stringToArray" })
    @InheritInverseConfiguration
    MailDto toDto(Mail dto);
}
