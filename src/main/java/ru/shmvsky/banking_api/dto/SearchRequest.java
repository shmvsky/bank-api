package ru.shmvsky.banking_api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SearchRequest {
        private String birthDate;
        private String fullname;
        private String phone;
        private String email;
        private String sortBy;
        @Pattern(regexp = "^(ASC|DESC)$")
        private String order;
}
