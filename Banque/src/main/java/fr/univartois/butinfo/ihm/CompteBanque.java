package fr.univartois.butinfo.ihm;

public class CompteBanque {
    private String nomProprietaire;
    private double solde;
    private int rib,virement, retrait, identifiantCompte, motDePasse;
    static final int MAX_VIREMENT = 1000;

    CompteBanque(String nomProprietaire, double solde, int identifiantCompte, int rib, int motDePasse, int virement, int retrait) {
        this.nomProprietaire = nomProprietaire;
        this.solde = solde;
        this.identifiantCompte = identifiantCompte;
        this.rib = rib;
        this.motDePasse = motDePasse;
        this.virement = virement;
        this.retrait = retrait;
    }

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
        try {
            this.virement = vir;
        } catch (Exception VirementException) {
            if (virement < 0) {
                throw new VirementException("On ne peut pas réinitialiser le plafond virement en négatif");
            }
        }

    }

    public int getRetrait() {
        return retrait;
    }

    public void setRetrait(int plafondRetrait) throws RetraitException {
        try {
            this.retrait = plafondRetrait;
        } catch (Exception RetraiException) {
            if (plafondRetrait < 0) {
                throw new RetraitException("Le plafond de retrait ne peut pas être rénitialisé négativement");
            }
        }

    }

    public double getSolde(CompteBanque compte1) throws CompteException {
        double sold = 0.0;
        try {
            if (compte1.getNomProprietaire().equals(compte1.getNomProprietaire()) && compte1.motDePasse == motDePasse) {
                sold = this.solde;
            }

        } catch (Exception CompteException) {
            throw new CompteException("Par sécurité, vous ne pouvez pas voir le solde car vous n'êtes pas propriétaire");
        }
        return sold;
    }


    public void retrait(double s, CompteBanque compte) throws SoldeException, CompteException {
        try {
            solde -= s;
            System.out.println("Vous avez retiré" + s + " euros de votre compte, votre solde actuel est de " + getSolde(compte) + "euros");

        } catch (Exception SoldeException) {
            if (s > solde) {
                throw new SoldeException("Vous ne pouvez pas retirer plus d'argent que vous n'en avez");
            }
        }

    }

    public void depot(double s, CompteBanque compte) throws SoldeException, CompteException {
        try {
            solde += s;
            System.out.println("Vous avez déposé" + s + " euros dans votre compte, votre solde actuel est de " + getSolde(compte) + "euros");
        } catch (Exception SoldeException) {
            if (s < 0) {
                throw new SoldeException("Vous ne pouvez pas déposer un solde négatif dans votre compte");
            }
        }
    }


    public void virement(CompteBanque compte1, CompteBanque compte2, double montant) throws SoldeException, CompteException {
        System.out.println("Choisissez l'identifiant de compte auquel vous voulez faire un virement bancaire");
        int idDestinataire = compte1.getIdentifiantCompte(); // Identifiant de compte destinataire
        try {

            if (idDestinataire == compte1.identifiantCompte && compte2.solde >= montant) {
                compte2.solde -= montant;
                compte1.solde += montant;
                compte1.getSolde(compte1);
                compte2.virement += montant;
            } else if (idDestinataire == compte2.identifiantCompte && compte1.solde >= montant) {
                compte1.solde -= montant;
                compte2.solde += montant;
                compte2.getSolde(compte2);
                compte1.virement += montant;
            }
        } catch (Exception CompteException) {
            if (idDestinataire != compte1.identifiantCompte || idDestinataire != compte2.identifiantCompte) {
                throw new CompteException("l'identifiant du compte destinataire n'a pas été bien saisit");
            } else {
                throw new SoldeException("Le solde du compte" + idDestinataire + " est insuffisant");
            }
        }
    }


    public void connexion(CompteBanque compte1, int mdp) throws CompteException {
        try {
            if (compte1.getMotDePasse() == mdp) {
                System.out.println("Bienvenu :) " + compte1.getNomProprietaire() + "dans votre compte. Votre solde est actuellement de" + compte1.getSolde(compte1) + "euros");
                System.out.println("Que voulez-vous faire sur votre compte Monsieur" + compte1.getNomProprietaire() + "?");
            }

        } catch (Exception CompteException) {
            throw new CompteException("Vous avez tapper un mot de passe éronné");
        }
    }

    public void plafond_virement(CompteBanque compte1, CompteBanque compte2, int montant) throws VirementException{
        try {
            if (compte1.getVirement() + montant <= MAX_VIREMENT) {
                compte1.virement += montant;
                virement(compte1, compte2, montant);
                System.out.println("Vous pouvez faire un virement, votre plafond bancaire est de : " + virement + "euros ");
                if (MAX_VIREMENT - 20 < virement) {
                    System.out.println("Votre plafond est bientôt atteint");
                }
            }
        } catch (Exception VirementException) {
            if (compte1.getVirement() > MAX_VIREMENT) {
                throw new VirementException("Le plafond bancaire est dépassé");
            }
        }

    }

    public void plafond_retrait(double s, CompteBanque compte) throws RetraitException {
        try {
            if (compte.getRetrait() + s <= MAX_VIREMENT) {
                compte.retrait += s;
                retrait(s, compte);
                System.out.println("Vous pouvez faire un retrait, votre plafond retrait est de : " + retrait + "euros ");
                if (MAX_VIREMENT - 20 < retrait) {
                    System.out.println("Votre plafond est bientôt atteint");
                }
            }

        } catch (Exception RetraitException) {
            if (compte.getRetrait() > MAX_VIREMENT) {
                throw new RetraitException("Le plafond de retrait est dépassé");
            }
        }
    }

    public static boolean verifierAuthentification(int identifiant, String motDePasse) {
        try {
            // Pour test, utilisez ces valeurs (à remplacer par une vraie base de données)
            return (identifiant == 101 && motDePasse.equals("9876")) ||
                    (identifiant == 102 && motDePasse.equals("1234"));
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args){
        try {
            // Création de comptes bancaires
            CompteBanque compte1 = new CompteBanque("Jean Dupont", 5000, 101, 123456, 9876, 0, 0);
            CompteBanque compte2 = new CompteBanque("Marie Curie", 3000, 102, 654321, 1234, 0, 0);

            // Connexion au compte
            compte1.connexion(compte1, 9876);

            // Dépôt d'argent
            compte1.depot(500, compte1);

            // Retrait d'argent
            compte1.retrait(200, compte1);

            // Virement entre comptes
            compte1.virement(compte1, compte2, 1000);

            // Vérification des soldes après transactions
            System.out.println("Solde du compte de Jean : " + compte1.getSolde(compte1));
            System.out.println("Solde du compte de Marie : " + compte2.getSolde(compte2));

        } catch (CompteException | SoldeException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}





