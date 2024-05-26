package ru.shmvsky.banking_api.dto;


import lombok.Value;
import ru.shmvsky.banking_api.model.User;

import java.time.LocalDate;

@Value
public class UserResponse {

    String username;
    String fullname;
    String phone;
    String email;
    LocalDate birthDate;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.fullname = user.getFullname();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
    }
}
