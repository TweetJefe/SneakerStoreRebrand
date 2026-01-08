package com.sneaker.store.users.service;


import com.sneaker.store.users.dto.UserCreateDTO;
import com.sneaker.store.users.dto.UserProfileDTO;
import com.sneaker.store.users.dto.UserUpdateDTO;
import com.sneaker.store.users.model.User;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {
    void  createUser(UserCreateDTO dto);

    void saveUser(User user);

    void updateUser(UserUpdateDTO dto, Long id);

    void deleteUser(Long id);

    UserProfileDTO getUser(Long id);

    void checkUserExistenceByEmail(String email);

    void checkUserExistenceByPhone(String phoneNumber);
}
