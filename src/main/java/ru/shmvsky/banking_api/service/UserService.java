package ru.shmvsky.banking_api.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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
import ru.shmvsky.banking_api.exception.EmailAlreadyTaken;
import ru.shmvsky.banking_api.exception.NoContactPresentException;
import ru.shmvsky.banking_api.exception.PhoneNumberAlreadyTaken;
import ru.shmvsky.banking_api.exception.UsernameAlreadyTaken;
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
        validateContacts(request.getEmail(), request.getPhone());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyTaken(request.getUsername());
        }

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullname(request.getFullname());
        user.setBirthDate(LocalDate.parse(request.getBirthDate()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());

        var account = new Account();
        account.setOriginalBalance(request.getAmount());
        account.setBalance(request.getAmount());
        account.setUser(user);

        user.setAccount(account);

        User u = userRepository.save(user);

        System.out.println(u);

        log.debug(String.format("User %s successfully", u.getUsername()));

        return u;
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
        validateContacts(contacts.getEmail(), contacts.getPhone());

        var user = getByUsername(name);
        user.setPhone(contacts.getPhone());
        user.setEmail(contacts.getEmail());

        var u = new UserResponse(userRepository.save(user));

        log.debug(String.format("%s contacts has been updated", u.getUsername()));

        return u;

    }

    private void validateContacts(String email, String phone) {
        if (email == null & phone == null) {
            throw new NoContactPresentException();
        }

        if (userRepository.existsByPhone(phone)) {
            throw new PhoneNumberAlreadyTaken(phone);
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyTaken(email);
        }
    }

    public Page<UserResponse> search(Integer page, SearchRequest searchRequest) {
        LocalDate birthDate = searchRequest.getBirthDate() != null ? LocalDate.parse(searchRequest.getBirthDate(), DateTimeFormatter.ISO_DATE) : null;

        Specification<User> spec = Specification.where(UserSpecifications.hasBirthDateAfter(birthDate))
                .and(UserSpecifications.hasFullNameLike(searchRequest.getFullname()))
                .and(UserSpecifications.hasEmail(searchRequest.getEmail()))
                .and(UserSpecifications.hasPhone(searchRequest.getPhone()));

        Sort sort = Sort.by(searchRequest.getOrder().equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, searchRequest.getSortBy());

        Pageable pageable = PageRequest.of(page, 5, sort);

        Page<User> entities = userRepository.findAll(spec, pageable);

        return entities.map(UserResponse::new);

    }

    @Transactional
    public TransferResponse transfer(TransferRequest request, String name) {
        Account sender = getByUsername(name).getAccount();
        Account receiver = getByUsername(request.getTo()).getAccount();

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new TransferResponse(request.getTo(), request.getAmount(), sender.getBalance(), "rejected");
        }

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            return new TransferResponse(request.getTo(), request.getAmount(), sender.getBalance(), "rejected");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        log.debug(String.format("%s had transferred %f$ to %s", name, request.getAmount(), request.getTo()));

        return new TransferResponse(request.getTo(), request.getAmount(), sender.getBalance(), "success");

    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
