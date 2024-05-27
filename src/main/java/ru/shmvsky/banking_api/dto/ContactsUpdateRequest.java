package ru.shmvsky.banking_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ContactsUpdateRequest {
        @Pattern(regexp = "\\+7\\s\\(\\d{3}\\)\\s\\d{3}-\\d{2}-\\d{2}", message = "Invalid phone number format")
        private String phone;
        @Email(message = "Invalid email format")
        private String email;
}
