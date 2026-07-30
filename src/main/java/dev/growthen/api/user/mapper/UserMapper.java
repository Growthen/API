package dev.growthen.api.user.mapper;

import dev.growthen.api.user.dto.response.UserResponse;
import dev.growthen.api.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
