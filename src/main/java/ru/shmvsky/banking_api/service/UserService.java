package ru.shmvsky.banking_api.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CaseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.shmvsky.banking_api.dto.*;
import ru.shmvsky.banking_api.exception.NoContactPresentException;
import ru.shmvsky.banking_api.model.Account;
import ru.shmvsky.banking_api.model.User;
import ru.shmvsky.banking_api.repository.AccountRepository;
import ru.shmvsky.banking_api.repository.UserRepository;
import ru.shmvsky.banking_api.repository.specifications.UserSpecifications;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request) {
        if (request.email() == null & request.phone() == null) {
            throw new NoContactPresentException();
        }

        var user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullname(request.fullname());
        user.setBirthDate(LocalDate.parse(request.birthDate()));
        user.setPhone(request.phone());
        user.setEmail(request.email());

        var account = new Account();
        account.setOriginalBalance(request.amount());
        account.setBalance(request.amount());
        account.setUser(user);

        user.setAccount(account);

        return userRepository.save(user);
    }

    public User getByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username %s not found".formatted(username)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user =  getByUsername(username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public UserResponse updateContacts(ContactsUpdateRequest contacts, String name) {
        if (contacts.email() == null & contacts.phone() == null) {
            throw new NoContactPresentException();
        }

        var user = getByUsername(name);
        user.setPhone(contacts.phone());
        user.setEmail(contacts.email());

        return new UserResponse(userRepository.save(user));

    }

    public Page<UserResponse> search(Integer page, SearchRequest searchRequest) {
        LocalDate birthDate = searchRequest.birthDate() != null ? LocalDate.parse(searchRequest.birthDate(), DateTimeFormatter.ISO_DATE) : null;

        Specification<User> spec = Specification.where(UserSpecifications.hasBirthDateAfter(birthDate))
                .and(UserSpecifications.hasPhone(searchRequest.phone()))
                .and(UserSpecifications.hasFullNameLike(searchRequest.fullname()))
                .and(UserSpecifications.hasEmail(searchRequest.email()));

        Sort sort = Sort.by(searchRequest.order().equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, CaseUtils.toCamelCase(searchRequest.sort_by(), false, '_'));

        Pageable pageable = PageRequest.of(page, 5, sort);

        Page<User> entities = userRepository.findAll(spec, pageable);

        return entities.map(UserResponse::new);

    }

    @Transactional
    public TransferResponse transfer(TransferRequest request, String name) {
        Account sender = getByUsername(name).getAccount();
        Account receiver = getByUsername(request.to()).getAccount();

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return new TransferResponse(request.to(), request.amount(), sender.getBalance(), "rejected");
        }

        if (sender.getBalance().compareTo(request.amount()) < 0) {
            return new TransferResponse(request.to(), request.amount(), sender.getBalance(), "rejected");
        }

        sender.setBalance(sender.getBalance().subtract(request.amount()));
        receiver.setBalance(receiver.getBalance().add(request.amount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        return new TransferResponse(request.to(), request.amount(), sender.getBalance(), "success");

    }
}
