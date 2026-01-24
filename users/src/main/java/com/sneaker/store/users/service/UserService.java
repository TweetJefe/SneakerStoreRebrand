package com.sneaker.store.users.service;


import com.sneaker.store.users.dto.*;
import com.sneaker.store.users.model.User;

public interface UserService {
    void  createUser(UserCreateDTO dto);

    void saveUser(User user);


    void deleteUser(Long id);

    void checkUserExistenceByEmail(String email);

    void checkUserExistenceByPhone(String phoneNumber);

    UserProfileDTO getUserProfileByEmail(String email);

    void updateEmail(UpdateEmailDTO dto, String email);

    void updateAddress(UpdateAddressDTO dto, String email);

    void updatePassword(UpdatePasswordDTO dto, String email);

    void addToFavorites(Long id, String email);

    void removeFromFavorites(Long id, String email);
}
