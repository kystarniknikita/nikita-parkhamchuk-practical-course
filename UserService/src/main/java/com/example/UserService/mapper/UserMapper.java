package com.example.UserService.mapper;

import com.example.UserService.model.dto.UserRequest;
import com.example.UserService.model.dto.UserResponse;
import com.example.UserService.model.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = CardInfoMapper.class)
public interface UserMapper {

    User toEntity(UserRequest dto);

    UserResponse toDto(User entity);

    void updateUserFromDto(@MappingTarget User entity, UserRequest dto);

    List<UserResponse> toDtos(List<User> entities);
}