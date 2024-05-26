package ru.shmvsky.banking_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @JsonIgnore
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal originalBalance;


    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
