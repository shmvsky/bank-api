package ru.shmvsky.banking_api.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.shmvsky.banking_api.model.User;

import java.time.LocalDate;

public class UserSpecifications {

    public static Specification<User> hasBirthDateAfter(LocalDate birthDate) {
        return (root, query, cb) -> birthDate == null ? null : cb.greaterThan(root.get("birthDate"), birthDate);
    }

    public static Specification<User> hasPhone(String phone) {
        return (root, query, cb) -> phone == null ? null : cb.equal(root.get("phone"), phone);
    }

    public static Specification<User> hasFullNameLike(String fullname) {
        return (root, query, cb) -> fullname == null ? null : cb.like(root.get("fullname"), fullname + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> email == null ? null : cb.equal(root.get("email"), email);
    }
}
