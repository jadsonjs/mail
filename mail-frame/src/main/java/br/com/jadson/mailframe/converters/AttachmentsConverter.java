package br.com.jadson.mailframe.converters;

import br.com.jadson.mailframe.dtos.AttachmentDto;
import br.com.jadson.mailframe.models.Attachment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {Base64Converter.class})
public interface AttachmentsConverter {

    @Mapping(source = "content", target = "content", qualifiedByName = { "Base64Converter", "base64ToBytes" })
    Attachment toModel(AttachmentDto dto);

    @Mapping(source = "content", target = "content", qualifiedByName = { "Base64Converter", "bytesToBase64" })
    @InheritInverseConfiguration
    AttachmentDto toDto(Attachment dto);
}
