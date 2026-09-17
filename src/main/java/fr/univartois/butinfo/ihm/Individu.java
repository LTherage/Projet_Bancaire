package fr.univartois.butinfo.ihm;

import java.time.LocalDate;

public class Individu {
    private String nom, prenom;
    private Adresse adresse;
    private int age, codePostal;
    private LocalDate dateNaissance;

    public Individu(String nom, Adresse adresse, String prenom, int codePostal, int age, LocalDate dateNaissance) {
        this.nom = nom;
        this.adresse = adresse;
        this.prenom = prenom;
        this.codePostal = codePostal;
        this.age = age;
        this.dateNaissance = dateNaissance;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setAdresse(Adresse a) throws CategorieException {
        if (a.length() < 6) {
            throw new CategorieException("L'adresse doit faire plus de 6 caractères");
        }
        this.adresse = a;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAge(int a) throws AgeException {
        if (a < 18 || a > 140) {
            throw new AgeException("Vous devez avoir au moins 18 ans et moins de 140 ans");
        }
        this.age = a;
    }

    public int getAge() {
        return age;
    }

    public void setCodePostal(int codePost) throws CategorieException {
        if (codePost < 10000) {
            throw new CategorieException("Le code postal doit comporter 5 chiffres minimum");
        }
        this.codePostal = codePost;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    @Override
    public String toString() {
        return "[nom = " + nom + ", prenom = " + prenom + ", adresse = " + adresse +
                ", codePostal = " + codePostal + ", age = " + age +
                ", date de naissance = " + dateNaissance + "]";
    }

    public static void main(String[] args) {
        // Création de communes
        Adresse.Commune commune1 = new Adresse.Commune(59000, "Lille", 59);
        Adresse.Commune commune2 = new Adresse.Commune(75000, "Paris", 75);
        Adresse.Commune commune3 = new Adresse.Commune(13000, "Marseille", 13);

        // Création d'adresses
        Adresse adresse1 = new Adresse(12, "Rue de la République", commune1);
        Adresse adresse2 = new Adresse(34, "Avenue des Champs-Élysées", commune2);
        Adresse adresse3 = new Adresse(56, "Boulevard Saint-Michel", commune3);

        // Création d'individus
        Individu individu1 = new Individu("Dupont", adresse1, "Jean", 59000, 30, LocalDate.of(1995, 5, 20));
        Individu individu2 = new Individu("Martin", adresse2, "Marie", 75000, 25, LocalDate.of(2000, 8, 15));
        Individu individu3 = new Individu("Leroy", adresse3, "Pierre", 13000, 40, LocalDate.of(1985, 3, 10));
    }



}
