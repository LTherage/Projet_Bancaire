package fr.univartois.butinfo.ihm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class BaseDonnee {
    private String url, pseudo, motDePasse;
    private Connection con;



    BaseDonnee(String url, String pseudo, String motDePasse){
        this.url = url;
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
        this.con = null;
    }

    public void setPseudo(String pseud){
        this.pseudo = pseud;
    }

    public String getPseudo(){
        return pseudo;
    }

    public void setMotDePasse(String mdp){
        this.motDePasse = mdp;
    }

    public String getMotDePasse(){
        return motDePasse;
    }

    public void setUrlBdd(String urll){
        this.url = urll;
    }

    public String getUrlBdd(){
        return url;
    }

    public void connecter() {
        try {
            con = DriverManager.getConnection(url, pseudo, motDePasse);
            System.out.println("Connexion réussie !");
        } catch (SQLException e) {
            System.err.println("Échec de la connexion !");
            e.printStackTrace();
        }
    }


    public void grapEnregistrements(String table) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);

            while (resultSet.next()) {
                System.out.println(resultSet.getString(1)); // Affiche les données (modifiable)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertEnregistrement(String requeteSQL) {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(requeteSQL);
            System.out.println("Insertion réussie !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteEnregistrement(String requeteSQL) {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(requeteSQL);
            System.out.println("Suppression réussie !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateEnregistrement(String requeteSQL) {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(requeteSQL);
            System.out.println("Mise à jour réussie !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        // Instanciation de la base de données avec des informations fictives
        BaseDonnee bdd = new BaseDonnee("jdbc:mysql://localhost:3306/banque", "root", "password");

        // Connexion à la base de données
        bdd.connecter();

        // Sélection d'enregistrements depuis une table fictive
        bdd.grapEnregistrements("clients");

        // Insertion d'un enregistrement fictif
        bdd.insertEnregistrement("INSERT INTO clients(nom, prenom) VALUES ('Jean', 'Dupont')");

        // Suppression d'un enregistrement fictif
        bdd.deleteEnregistrement("DELETE FROM clients WHERE id = 1");

        // Mise à jour d'un enregistrement fictif
        bdd.updateEnregistrement("UPDATE clients SET nom='Martin' WHERE id=2");
    }
}


