package dev.growthen.apilibreria.user.mapper;

import dev.growthen.apilibreria.user.dto.response.UserResponse;
import dev.growthen.apilibreria.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
