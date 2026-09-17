package fr.univartois.butinfo.ihm;

public final class SessionManager {
    private static CompteBanque currentCompte;

    private SessionManager() {
    }

    public static void setCurrentCompte(CompteBanque compte) {
        currentCompte = compte;
    }

    public static CompteBanque getCurrentCompte() {
        return currentCompte;
    }

    public static void clear() {
        currentCompte = null;
    }

    public static boolean hasRole(CompteBanque.Role requiredRole) {
        if (requiredRole == null) {
            return true;
        }
        if (currentCompte == null) {
            return false;
        }
        return new AuthMiddleware(requiredRole).canAccess(currentCompte);
    }
}
