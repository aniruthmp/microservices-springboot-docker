package com.carnival.auth.service;

import com.carnival.auth.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by a.c.parthasarathy on 11/3/16.
 */
@Service
public class UserAccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {

        return this.accountRepository.findByUsername(username)
                .map(account -> new User(
                        account.getUsername(),
                        account.getPassword(),
                        account.isActive(),
                        account.isActive(),
                        account.isActive(),
                        account.isActive(),
                        AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER")))
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find the user " +
                        "" + username + "!"));

    }

}
