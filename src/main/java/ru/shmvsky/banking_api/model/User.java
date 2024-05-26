package ru.shmvsky.banking_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 55)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullname;

    private LocalDate birthDate;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Account account;

}
