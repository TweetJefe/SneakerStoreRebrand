package com.sneaker.store.users.mapper;

import com.sneaker.store.users.dto.UserCreateDTO;
import com.sneaker.store.users.dto.UserProfileDTO;
import com.sneaker.store.users.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserCreateDTO toDTO (User user);
    User toEntity (UserCreateDTO dto);

    UserProfileDTO toDTOProfile (User user);
}

