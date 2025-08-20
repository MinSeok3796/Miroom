package org.example.miroom.security;

import org.example.miroom.entity.User;

public interface AuthenticationFacade {
    User getCurrentUser();
    Long getCurrentUserId();
}
