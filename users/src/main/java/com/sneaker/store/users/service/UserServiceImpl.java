package com.sneaker.store.users.service;

import com.sneaker.store.users.dto.*;
import com.sneaker.store.users.exceptions.NullableViolation;
import com.sneaker.store.users.exceptions.ServerException;
import com.sneaker.store.users.exceptions.UniquenessViolation;
import com.sneaker.store.users.mapper.UserMapper;
import com.sneaker.store.users.model.Address;
import com.sneaker.store.users.model.User;
import com.sneaker.store.users.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    private final String PostgreSQLUniquenessViolation = "23505";
    private final String PostgreSQLNullableViolation = "23502";

    public void createUser(UserCreateDTO dto) {
        checkUserExistenceByEmail(dto.email());
        checkUserExistenceByPhone(dto.phoneNumber());

        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        if (dto.role() == null) {
            user.getRoles().add("USER");
        }else{
            user.getRoles().addAll(List.of("ADMIN", "USER"));
        }
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

    @Override
    public UserProfileDTO getUserProfileByEmail(String email) {
        return repository.findUserByEmail(email).map(user -> new UserProfileDTO(
                user.getEmail(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getFavourites()
        )).orElseThrow(EntityExistsException::new);
    }

    @Override
    public void updateEmail(UpdateEmailDTO dto, String email) {
        var user = repository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
        if(!passwordEncoder.matches(dto.password(), user.getPassword())){
            throw new BadCredentialsException("");
        }
        user.setEmail(dto.email());
        saveUser(user);
    }

    @Override
    public void updateAddress(UpdateAddressDTO dto, String email) {
        var user = repository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
        user.setAddress(new Address(
                dto.city(),
                dto.street(),
                dto.zipcode(),
                dto.country(),
                dto.houseNumber(),
                dto.doorNumber()
        ));
        saveUser(user);
    }

    @Override
    public void updatePassword(UpdatePasswordDTO dto, String email) {
        var user = repository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
        if(!passwordEncoder.matches(dto.password(), user.getPassword())){
            throw new BadCredentialsException("");
        }
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        saveUser(user);
    }

    @Override
    public void addToFavorites(Long id, String email) {
        User user = repository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
        user.getFavourites().add(id);
        saveUser(user);
    }

    @Override
    public void removeFromFavorites(Long id, String email) {
        User user = repository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
        user.getFavourites().remove(id);
        saveUser(user);
    }
}
