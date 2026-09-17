package fr.univartois.butinfo.ihm;

public class AuthMiddleware {
    private final CompteBanque.Role requiredRole;

    public AuthMiddleware(CompteBanque.Role requiredRole) {
        this.requiredRole = requiredRole;
    }

    public boolean canAccess(CompteBanque compte) {
        if (compte == null) {
            return false;
        }
        if (requiredRole == null) {
            return true;
        }
        return switch (requiredRole) {
            case ADMIN -> compte.getRole() == CompteBanque.Role.ADMIN;
            case EMPLOYE -> compte.getRole() == CompteBanque.Role.ADMIN
                    || compte.getRole() == CompteBanque.Role.EMPLOYE;
            case CLIENT -> true;
        };
    }
}
