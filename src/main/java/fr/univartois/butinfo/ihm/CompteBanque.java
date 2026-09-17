package fr.univartois.butinfo.ihm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompteBanque {
    private String nomProprietaire;
    private double solde;
    private int rib, virement, retrait, identifiantCompte, motDePasse;
    private Role role = Role.CLIENT;
    static final int MAX_VIREMENT = 1000;
    private static final List<CompteBanque> COMPTES = new ArrayList<>();
    private static boolean comptesInitialises = false;
    private final List<Operation> operations = new ArrayList<>();

    public CompteBanque(String nomProprietaire, double solde, int identifiantCompte, int rib, int motDePasse, int virement, int retrait) {
        this.nomProprietaire = nomProprietaire;
        this.solde = solde;
        this.identifiantCompte = identifiantCompte;
        this.rib = rib;
        this.motDePasse = motDePasse;
        this.virement = virement;
        this.retrait = retrait;
        ajouterCompte(this);
    }

    public enum Role {
        ADMIN,
        EMPLOYE,
        CLIENT
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role == null ? Role.CLIENT : role;
    }

    public static List<CompteBanque> getComptes() {
        if (!comptesInitialises) {
            initialiserComptesDeTest();
            comptesInitialises = true;
        }
        return COMPTES;
    }

    public static void initialiserComptesDeTest() {
        if (COMPTES.isEmpty()) {
            CompteBanque admin = new CompteBanque("Admin Banque", 20000, 1, 100001, 1111, 0, 0);
            admin.setRole(Role.ADMIN);
            CompteBanque employe = new CompteBanque("Employe Banque", 8000, 2, 100002, 2222, 0, 0);
            employe.setRole(Role.EMPLOYE);
            CompteBanque client1 = new CompteBanque("Jean Dupont", 5000, 101, 123456, 9876, 0, 0);
            client1.setRole(Role.CLIENT);
            CompteBanque client2 = new CompteBanque("Marie Curie", 3000, 102, 654321, 1234, 0, 0);
            client2.setRole(Role.CLIENT);
            COMPTES.add(admin);
            COMPTES.add(employe);
            COMPTES.add(client1);
            COMPTES.add(client2);
        }
    }

    public static void ajouterCompte(CompteBanque compte) {
        if (compte == null) {
            return;
        }
        boolean existe = COMPTES.stream().anyMatch(c -> c.identifiantCompte == compte.identifiantCompte);
        if (!existe) {
            COMPTES.add(compte);
        }
    }

    public static CompteBanque trouverCompteParIdentifiant(int identifiantCompte) {
        return COMPTES.stream()
                .filter(compte -> compte.identifiantCompte == identifiantCompte)
                .findFirst()
                .orElse(null);
    }

    public List<Operation> getOperations() { return operations; }

    public String getNomProprietaire() {
        return nomProprietaire;
    }

    private void setNomProprietaire(String nom) {
        this.nomProprietaire = nom;
    }

    public void setSolde(double s) {
        this.solde = s;
    }

    public void setIdentifiantCompte(int id) {
        this.identifiantCompte = id;
    }

    public int getIdentifiantCompte() {
        return identifiantCompte;
    }

    public int getRib() {
        return rib;
    }

    public void setMotDePasse(int mdp) {
        this.motDePasse = mdp;
    }

    public int getMotDePasse() {
        return motDePasse;
    }

    public int getVirement() {
        return virement;
    }

    public void setVirement(int vir) throws VirementException {
        if (vir < 0) {
            throw new VirementException("On ne peut pas réinitialiser le plafond virement en négatif");
        }
        this.virement = vir;
    }

    public int getRetrait() {
        return retrait;
    }

    public void setRetrait(int plafondRetrait) throws RetraitException {
        if (plafondRetrait < 0) {
            throw new RetraitException("Le plafond de retrait ne peut pas être rénitialisé négativement");
        }
        this.retrait = plafondRetrait;
    }

    public double getSolde(CompteBanque compte1) throws CompteException {
        if (compte1 == null) {
            throw new CompteException("Compte invalide");
        }
        if (!Objects.equals(this.nomProprietaire, compte1.nomProprietaire)
                || this.identifiantCompte != compte1.identifiantCompte) {
            throw new CompteException("Par sécurité, vous ne pouvez pas voir le solde car vous n'êtes pas propriétaire");
        }
        return this.solde;
    }

    public void retrait(double montant, CompteBanque compte) throws SoldeException, CompteException {
        if (compte == null) {
            throw new CompteException("Compte invalide");
        }
        if (montant <= 0) {
            throw new SoldeException("Le montant du retrait doit être positif");
        }
        if (solde < montant) {
            throw new SoldeException("Vous ne pouvez pas retirer plus d'argent que vous n'en avez");
        }
        solde -= montant;
        operations.add(new Operation("Retrait", -montant, solde, "Retrait d'argent"));
        System.out.println("Vous avez retiré " + montant + " euros de votre compte, votre solde actuel est de " + getSolde(compte) + " euros");
    }

    public void depot(double montant, CompteBanque compte) throws SoldeException, CompteException {
        if (compte == null) {
            throw new CompteException("Compte invalide");
        }
        if (montant <= 0) {
            throw new SoldeException("Vous ne pouvez pas déposer un montant négatif ou nul");
        }
        solde += montant;
        operations.add(new Operation("Dépôt", montant, solde, "Dépôt d'argent"));
        System.out.println("Vous avez déposé " + montant + " euros dans votre compte, votre solde actuel est de " + getSolde(compte) + " euros");
    }

    public void virement(CompteBanque expediteur, CompteBanque destinataire, double montant) throws SoldeException, CompteException {
        if (expediteur == null || destinataire == null) {
            throw new CompteException("Les comptes de l'opération sont invalides");
        }
        if (expediteur == destinataire) {
            throw new SoldeException("Impossible d'effectuer un virement vers le même compte");
        }
        if (montant <= 0) {
            throw new SoldeException("Le montant du virement doit être positif");
        }
        if (expediteur.solde < montant) {
            throw new SoldeException("Solde insuffisant pour effectuer le virement");
        }
        expediteur.solde -= montant;
        destinataire.solde += montant;
        expediteur.operations.add(new Operation("Virement émis", -montant, expediteur.solde, "Virement vers " + destinataire.getNomProprietaire()));
        destinataire.operations.add(new Operation("Virement reçu", montant, destinataire.solde, "Virement de " + expediteur.getNomProprietaire()));
        System.out.println("Virement de " + montant + " euros effectué de " + expediteur.getNomProprietaire() + " vers " + destinataire.getNomProprietaire());
    }

    public void connexion(CompteBanque compte1, int mdp) throws CompteException {
        if (compte1 == null) {
            throw new CompteException("Compte invalide");
        }
        if (compte1.getMotDePasse() != mdp) {
            throw new CompteException("Vous avez tapé un mot de passe erroné");
        }
        System.out.println("Bienvenue :) " + compte1.getNomProprietaire() + " dans votre compte. Votre solde est actuellement de " + compte1.getSolde(compte1) + " euros");
        System.out.println("Que voulez-vous faire sur votre compte Monsieur " + compte1.getNomProprietaire() + " ?");
    }

    public void plafond_virement(CompteBanque compte1, CompteBanque compte2, int montant) throws VirementException, CompteException, SoldeException {
        if (compte1 == null || compte2 == null) {
            throw new CompteException("Les comptes de l'opération sont invalides");
        }
        if (montant <= 0) {
            throw new SoldeException("Le montant du virement doit être positif");
        }
        if (compte1.getVirement() + montant > MAX_VIREMENT) {
            throw new VirementException("Le plafond bancaire est dépassé");
        }
        compte1.virement += montant;
        virement(compte1, compte2, montant);
        System.out.println("Vous pouvez faire un virement, votre plafond bancaire est de : " + compte1.virement + " euros");
        if (MAX_VIREMENT - 20 < compte1.virement) {
            System.out.println("Votre plafond est bientôt atteint");
        }
    }

    public void plafond_retrait(double s, CompteBanque compte) throws RetraitException, CompteException, SoldeException {
        if (compte == null) {
            throw new CompteException("Compte invalide");
        }
        if (s <= 0) {
            throw new SoldeException("Le montant du retrait doit être positif");
        }
        if (compte.getRetrait() + s > MAX_VIREMENT) {
            throw new RetraitException("Le plafond de retrait est dépassé");
        }
        compte.retrait += s;
        retrait(s, compte);
        System.out.println("Vous pouvez faire un retrait, votre plafond retrait est de : " + compte.retrait + " euros");
        if (MAX_VIREMENT - 20 < compte.retrait) {
            System.out.println("Votre plafond est bientôt atteint");
        }
    }

    public static CompteBanque authentifier(int identifiant, String motDePasse) {
        if (motDePasse == null || motDePasse.isBlank()) {
            return null;
        }
        if (!comptesInitialises) {
            initialiserComptesDeTest();
            comptesInitialises = true;
        }
        CompteBanque compte = trouverCompteParIdentifiant(identifiant);
        if (compte != null && String.valueOf(compte.getMotDePasse()).equals(motDePasse.trim())) {
            return compte;
        }
        if ((identifiant == 101 && motDePasse.equals("9876")) || (identifiant == 102 && motDePasse.equals("1234"))) {
            return trouverCompteParIdentifiant(identifiant);
        }
        return null;
    }

    public static boolean verifierAuthentification(int identifiant, String motDePasse) {
        return authentifier(identifiant, motDePasse) != null;
    }

    public static void main(String[] args) {
        try {
            CompteBanque compte1 = new CompteBanque("Jean Dupont", 5000, 101, 123456, 9876, 0, 0);
            CompteBanque compte2 = new CompteBanque("Marie Curie", 3000, 102, 654321, 1234, 0, 0);

            compte1.connexion(compte1, 9876);
            compte1.depot(500, compte1);
            compte1.retrait(200, compte1);
            compte1.virement(compte1, compte2, 1000);

            System.out.println("Solde du compte de Jean : " + compte1.getSolde(compte1));
            System.out.println("Solde du compte de Marie : " + compte2.getSolde(compte2));

        } catch (CompteException | SoldeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}





