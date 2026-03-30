package fr.univtln.projet.planning.mapper.authentication;

import fr.univtln.projet.planning.entity.authentication.AuthenticationEntity;
import fr.univtln.projet.planning.modele.authentication.Authentication;

public class AuthenticationMapper {

    private AuthenticationMapper() {
        // Prevent instantiation of utility class
    }

    public static AuthenticationEntity toDomain(Authentication a) {
        if (a == null) return null;
        if (a.isPasswordDefined()) {
            return new AuthenticationEntity(
                    a.getEmail(),
                    a.getHashedPassword()
            );
        } else {
            return new AuthenticationEntity(a.getEmail());
        }
    }

    public static Authentication toJpa(AuthenticationEntity a) {
        if (a == null) return null;
        if (a.isPasswordDefined()) {
            return new Authentication(
                    a.getEmail(),
                    a.getHashedPassword()
            );
        } else {
            return new Authentication(a.getEmail());
        }
    }
}

