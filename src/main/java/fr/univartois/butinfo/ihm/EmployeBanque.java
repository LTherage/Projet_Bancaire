package fr.univartois.butinfo.ihm;

import java.time.LocalDate;

class EmployeBanque extends Individu{
    private String nomBanque, adresseBanque, poste;
    private int codePostalBanque;

    public EmployeBanque(String nom, Adresse adresse, String prenom, int codePostal, int age, LocalDate dateNaissance, String nomBanque, String adresseBanque, String poste, int codePostalBanque) {
        super(nom, adresse, prenom, codePostal, age, dateNaissance);
        this.nomBanque = nomBanque;
        this.adresseBanque = adresseBanque;
        this.codePostalBanque = codePostalBanque;
        this.poste = poste;
    }

    public String getNomBanque(){
        return nomBanque;
    }

    public void setAdresseBanque(String a){
        if(a.length() < 6){System.out.println("Longueur insuffisante");}
        this.adresseBanque = a;
    }

    public String getAdresseBanque(){
        return adresseBanque;
    }

    public int getCodePostalBanque(){
        return codePostalBanque;
    }

    public void setPoste(String post){
        this.poste = post;
    }

    public String getPoste(){
        return poste;
    }

    public static void main(String[] args){
        // Création de communes
        Adresse.Commune commune1 = new Adresse.Commune(59000, "Lille", 59);
        Adresse.Commune commune2 = new Adresse.Commune(75000, "Paris", 75);

        // Création d'adresses
        Adresse adresse1 = new Adresse(10, "Rue du Commerce", commune1);
        Adresse adresse2 = new Adresse(22, "Boulevard Haussmann", commune2);

        // Création d'employés bancaires
        EmployeBanque employe1 = new EmployeBanque("Dupont", adresse1, "Jean", 59000, 45, LocalDate.of(1980, 5, 20), "Banque Nationale", "123 Avenue de la Banque", "Conseiller financier", 75001);
        EmployeBanque employe2 = new EmployeBanque("Martin", adresse2, "Marie", 75000, 38, LocalDate.of(1987, 8, 15), "Banque Centrale", "456 Rue des Finances", "Directrice de succursale", 75002);

        // Affichage des employés bancaires créés
        System.out.println(employe1);
        System.out.println(employe2);

    }
}
