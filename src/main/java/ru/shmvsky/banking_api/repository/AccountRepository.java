package ru.shmvsky.banking_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shmvsky.banking_api.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}