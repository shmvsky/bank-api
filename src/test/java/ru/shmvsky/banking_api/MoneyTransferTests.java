package ru.shmvsky.banking_api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.shmvsky.banking_api.dto.RegisterRequest;
import ru.shmvsky.banking_api.dto.TransferRequest;
import ru.shmvsky.banking_api.dto.TransferResponse;
import ru.shmvsky.banking_api.service.UserService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MoneyTransferTests {

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService.deleteUser("sender");
        userService.deleteUser("receiver");

        RegisterRequest sender = new RegisterRequest();
        sender.setUsername("sender");
        sender.setPassword("securePassword");
        sender.setFullname("Sender Fullname");
        sender.setBirthDate("1990-01-01");
        sender.setAmount(new BigDecimal("1000.00"));
        sender.setPhone("+7 (123) 456-78-90");
        sender.setEmail("sender@example.com");

        RegisterRequest receiver = new RegisterRequest();
        receiver.setUsername("receiver");
        receiver.setPassword("securePassword2");
        receiver.setFullname("Receiver Fullname");
        receiver.setBirthDate("1992-02-02");
        receiver.setAmount(new BigDecimal("500.00"));
        receiver.setPhone("+7 (987) 654-32-10");
        receiver.setEmail("receiver@example.com");

        userService.registerUser(sender);
        userService.registerUser(receiver);
    }

    @Test
    public void testTransferSuccess() {
        TransferRequest request = new TransferRequest();
        request.setTo("receiver");
        request.setAmount(new BigDecimal("100.00"));

        TransferResponse response = userService.transfer(request, "sender");

        assertEquals("success", response.status());
        assertEquals(new BigDecimal("900.00"), response.balance());
        assertEquals(new BigDecimal("600.00"), userService.getByUsername("receiver").getAccount().getBalance());
    }

    @Test
    public void testTransferInsufficientBalance() {
        TransferRequest request = new TransferRequest();
        request.setTo("receiver");
        request.setAmount(new BigDecimal("2000.00"));

        TransferResponse response = userService.transfer(request, "sender");

        assertEquals("rejected", response.status());
        assertEquals(new BigDecimal("1000.00"), userService.getByUsername("sender").getAccount().getBalance());
        assertEquals(new BigDecimal("500.00"), userService.getByUsername("receiver").getAccount().getBalance());
    }

    @Test
    public void testTransferInvalidAmount() {
        TransferRequest request = new TransferRequest();
        request.setTo("receiver");
        request.setAmount(new BigDecimal("-100.00"));

        TransferResponse response = userService.transfer(request, "sender");

        assertEquals("rejected", response.status());
        assertEquals(new BigDecimal("1000.00"), userService.getByUsername("sender").getAccount().getBalance());
        assertEquals(new BigDecimal("500.00"), userService.getByUsername("receiver").getAccount().getBalance());

        System.out.println("testTransferInvalidAmount");
    }

}
