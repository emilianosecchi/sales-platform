package com.esecchi.userauth.mapper;

import com.esecchi.userauth.model.User;
import com.esecchi.userauth.request.RegisterRequest;
import com.esecchi.userauth.request.UserUpdateRequest;
import com.esecchi.userauth.response.UserResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "registrationDate", expression = "java(java.time.LocalDate.now())")
    User toEntity(RegisterRequest request);

    UserResponseDTO toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UserUpdateRequest request, @MappingTarget User user);

}
