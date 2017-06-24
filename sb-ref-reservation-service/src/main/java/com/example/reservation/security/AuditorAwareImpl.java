package com.example.reservation.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by Aniruth Parthasarathy
 */
@Slf4j
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String auditor = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            log.info(authentication.toString());
            auditor = authentication.getName();
        }

        log.info("getCurrentAuditor() returns: " + auditor);
        return auditor;
    }
}
