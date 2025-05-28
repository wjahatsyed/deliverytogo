package com.deliverytogo.auth_service.mapper;

import com.deliverytogo.auth_service.dto.AuthRequest;
import com.deliverytogo.auth_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(AuthRequest request);

    // AuthRequest toDto(User user);
}
