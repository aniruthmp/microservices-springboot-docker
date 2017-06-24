package com.example.auth.service;

import com.example.auth.domain.Account;
import com.example.auth.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
class SampleAccountDataCLR implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Autowired
    public SampleAccountDataCLR(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        Stream.of("user,password", "admin,admin", "spring,cloud")
                .map(t -> t.split(","))
                .forEach(tuple ->
                        accountRepository.save(new Account(
                                tuple[0],
                                tuple[1],
                                true
                        ))
                );


    }
}