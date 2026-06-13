package bcc.group.mapper;


import bcc.group.dto.contact.ContactRequest;
import bcc.group.entity.ContactMessage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ContactMessage toEntity(ContactRequest request);
}
