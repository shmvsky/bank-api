package ru.shmvsky.banking_api.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.shmvsky.banking_api.model.Account;
import ru.shmvsky.banking_api.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateBalances() {
        List<Account> accounts = accountRepository.findAll();
        log.debug("Updating accounts balances");
        for (Account account : accounts) {
            BigDecimal maxAllowedBalance = account.getOriginalBalance().multiply(BigDecimal.valueOf(2.07));
            BigDecimal newBalance = account.getBalance().multiply(BigDecimal.valueOf(1.05));

            if (newBalance.compareTo(maxAllowedBalance) > 0) {
                account.setBalance(account.getBalance());
            } else {
                account.setBalance(newBalance);
            }

            accountRepository.save(account);
        }
    }

}
