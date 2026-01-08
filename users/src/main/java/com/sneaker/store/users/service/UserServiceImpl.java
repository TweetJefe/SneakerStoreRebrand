package com.sneaker.store.users.service;

import com.sneaker.store.users.dto.UserCreateDTO;
import com.sneaker.store.users.dto.UserProfileDTO;
import com.sneaker.store.users.dto.UserUpdateDTO;
import com.sneaker.store.users.exceptions.NullableViolation;
import com.sneaker.store.users.exceptions.ServerException;
import com.sneaker.store.users.exceptions.UniquenessViolation;
import com.sneaker.store.users.mapper.UserMapper;
import com.sneaker.store.users.model.User;
import com.sneaker.store.users.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    private final String PostgreSQLUniquenessViolation = "23505";
    private final String PostgreSQLNullableViolation = "23502";

    public void createUser(UserCreateDTO dto) {
        checkUserExistenceByEmail(dto.email());
        checkUserExistenceByPhone(dto.phoneNumber());

        User user = mapper.toEntity(dto);
        saveUser(user);
    }

    @Override
    public void saveUser(User user) {
        try{
            repository.save(user);
        }catch (DataIntegrityViolationException exception){
            Throwable cause =exception.getCause();
            if(cause instanceof ConstraintViolationException cve){
                String sqlState =cve.getSQLState();
                if(sqlState.equals(PostgreSQLUniquenessViolation)){
                    String constraintName =cve.getConstraintName();
                    throw new UniquenessViolation(constraintName);
                }else if(sqlState.equals(PostgreSQLNullableViolation)){
                    throw new NullableViolation("");
                }
            }else{
                throw new ServerException("");
            }
        }
    }

    @Override
    public void updateUser(UserUpdateDTO dto, Long id) {
        repository.findUserById(id).ifPresentOrElse(
                user -> {
                    if(user.getEmail().equals(dto.email())){
                        throw new IllegalArgumentException("");
                    }
                    if(user.getPhoneNumber().equals(dto.phoneNumber())){
                        throw new IllegalArgumentException("");
                    }
                    checkUserExistenceByEmail(dto.email());
                    checkUserExistenceByPhone(dto.phoneNumber());

                    user.setEmail(dto.email());
                    user.setAddress(dto.address());
                    user.setPhoneNumber(dto.phoneNumber());
                    saveUser(user);
                },
                () -> {
                    throw new EntityNotFoundException("");
                }
        );
    }

    @Override
    public void deleteUser(Long id) {
        repository.findUserById(id).ifPresentOrElse(
                user -> {
                    repository.deleteById(id);
                },
                () -> {
                    throw new EntityNotFoundException("");
                }
        );
    }


    @Override
    public UserProfileDTO getUser(Long id) {
        User user = repository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        return mapper.toDTOProfile(user);
    }

    @Override
    public void checkUserExistenceByEmail(String email){
        repository.findUserByEmail(email)
                .ifPresent(u -> {
                    throw new EntityExistsException("");
                });
    }

    @Override
    public void checkUserExistenceByPhone(String phoneNumber){
        repository.findUserByPhoneNumber(phoneNumber)
                .ifPresent(u -> {
                    throw new EntityExistsException("");
        });
    }
}
