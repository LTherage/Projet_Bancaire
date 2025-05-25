package fr.univartois.butinfo.ihm;

import java.time.LocalDate;
import java.util.ArrayList;

class Client extends Individu {
    // Rendre la liste statique pour qu'elle soit partagée par tous les clients
    private static ArrayList<Client> tabClient = new ArrayList<>();
    private int numeroClient;

    Client(String nom, Adresse adresse, String prenom, int codePostal, int age, LocalDate dateNaissance, int numeroClient){
        super(nom, adresse, prenom, codePostal, age, dateNaissance);
        this.numeroClient = numeroClient;
    }

    // Méthode statique pour accéder à la liste
    public static ArrayList<Client> getList(){
        return tabClient;
    }

    // Méthode statique pour ajouter un client
    public static void addClient(Client c1) throws CategorieException{
        if(c1.getNom().isEmpty() || c1.getNom() == null || 
           c1.getPrenom() == null || c1.getPrenom().isEmpty() || 
           c1.getDateNaissance() == null || 
           c1.getAdresse() == null || c1.getAdresse().length() == 0) {
            throw new CategorieException("Vous avez oublié un champ à remplir");
        }
        
        // Vérifier si le numéro client existe déjà
        if(tabClient.stream().anyMatch(c -> c.getNumeroClient() == c1.getNumeroClient())) {
            throw new CategorieException("Ce numéro client existe déjà");
        }
        
        tabClient.add(c1);
        System.out.println("Le client a bien été ajouté");
    }

    // Méthode statique pour supprimer un client
    public static void delClient(Client c1) throws CategorieException{
        if(!tabClient.remove(c1)) {
            throw new CategorieException("Le client à enlever n'existe pas");
        }
        System.out.println("Le client a été supprimé");
    }

    // Les autres méthodes restent identiques
    public void setNumeroClient(int a) throws CategorieException{
        if(a < 0) {
            throw new CategorieException("Le numéro client ne peut être négatif");
        }
        this.numeroClient = a;
    }

    public int getNumeroClient(){
        return numeroClient;
    }

    // Méthode statique pour obtenir le nombre de clients
    public static int getNbClient(){
        return tabClient.size();
    }

    // Méthode pour rechercher un client par son numéro
    public static Client rechercherClient(int numeroClient) {
        return tabClient.stream()
                .filter(client -> client.getNumeroClient() == numeroClient)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return String.format("Client n°%d : %s %s", numeroClient, getNom(), getPrenom());
    }
}