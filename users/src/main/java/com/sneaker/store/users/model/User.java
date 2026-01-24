package com.sneaker.store.users.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name="user_id"))
    @Column(name="role")
    private List<String> roles = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name="user_id"))
    @Column(name="permission")
    private List<String> permissions = new ArrayList<>();

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_favourites", joinColumns = @JoinColumn(name="user_id"))
    @Column(name="favourite")
    private List<Long> favourites = new ArrayList<>();
}
