package fr.univartois.butinfo.ihm;

class InfoBanque{
    private String nomBanque;
    private Adresse adresseBanque;
    private int codePostalBanque;

    InfoBanque(String nomBanque, Adresse adresseBanque, int codePostalBanque){
        this.nomBanque = nomBanque;
        this.adresseBanque = adresseBanque;
        this.codePostalBanque = codePostalBanque;
    }

    public String getNomBanque(){
        return nomBanque;
    }

    public void setAdresseBanque(Adresse a){
        if(a.length() < 6){System.out.println("Longueur insuffisante");}
        this.adresseBanque = a;
    }

    public Adresse getAdresseBanque(){
        return adresseBanque;
    }

    public int getCodePostalBanque(){
        return codePostalBanque;
    }

    public static void main(String[] args){
        // Création de communes
        Adresse.Commune commune1 = new Adresse.Commune(59000, "Lille", 59);
        Adresse.Commune commune2 = new Adresse.Commune(75000, "Paris", 75);

        // Création d'adresses pour les banques
        Adresse adresseBanque1 = new Adresse(1, "Place de la Bourse", commune1);
        Adresse adresseBanque2 = new Adresse(2, "Rue du Crédit", commune2);

        // Création d'instances InfoBanque
        InfoBanque banque1 = new InfoBanque("Banque Populaire", adresseBanque1, 59000);
        InfoBanque banque2 = new InfoBanque("Banque Centrale", adresseBanque2, 75000);

        // Affichage des informations des banques
        System.out.println("Informations sur les banques :");
        System.out.println("Banque 1 : " + banque1.getNomBanque() + ", Adresse: " + banque1.getAdresseBanque() + ", Code Postal: " + banque1.getCodePostalBanque());
        System.out.println("Banque 2 : " + banque2.getNomBanque() + ", Adresse: " + banque2.getAdresseBanque() + ", Code Postal: " + banque2.getCodePostalBanque());

    }

}
