package fr.univartois.butinfo.ihm;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;


public class ApplicationBanque extends Application {


    public CheckBox conditionsCheckBox;
    @FXML
    private Label label, messageLabel;
    @FXML
    private TextField text, nomUtilisateur, identifiantField, motifVirementField, montantVirementField,ribField, codePostalField, villeField, departementField,numeroRueField, rueField, nomField, prenomField, dateNaissanceField, numeroClientField, ageField, emailField, telephoneField;
    @FXML
    private Button connexion;
    @FXML
    private PasswordField passwd, motDePasseField, confirmationMotDePasse;

    @FXML
    private DatePicker dateClient;



    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Charger le FXML avec un chemin absolu
            String fxmlPath = "/fr/univartois/butinfo/ihm/hello-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (fxmlLoader.getLocation() == null) {
                throw new IOException("Impossible de trouver le fichier : " + fxmlPath);
            }

            Parent viewContent = fxmlLoader.load();
            Scene scene = new Scene(viewContent, 900, 540);

            // Charger le CSS avec un chemin absolu
            String cssPath = "/fr/univartois/butinfo/ihm/style.css";
            String cssUrl = getClass().getResource(cssPath).toExternalForm();
            scene.getStylesheets().add(cssUrl);

            stage.setScene(scene);
            stage.setTitle("Votre Application de banque");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur au démarrage : " + e.getMessage(), e);
        }
    }


    public static void changerVue(Stage stage, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationBanque.class.getResource(fxml));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 900, 540));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de " + fxml);
            e.printStackTrace();
        }
    }

    @FXML
    private void creerCompte() {
        System.out.println("Bouton : Creer un Compte cliqué");
        Stage stage = (Stage) connexion.getScene().getWindow();
        changerVue(stage, "creation-view.fxml");
    }

    @FXML
    private void seConnecter() {
        Stage stage = (Stage) connexion.getScene().getWindow();
        changerVue(stage, "connexion-view.fxml");
    }

    @FXML
    private void identifiant() {
        try {
            String identifiantSaisi = text.getText();
            String motDePasseSaisi = passwd.getText();

            // Vérification des champs vides
            if (identifiantSaisi.isEmpty() || motDePasseSaisi.isEmpty()) {
                label.setText("Veuillez remplir tous les champs");
                label.setStyle("-fx-text-fill: red;");
                return;
            }

            int id = Integer.parseInt(identifiantSaisi);

            // Vérification de l'authentification
            if (CompteBanque.verifierAuthentification(id, motDePasseSaisi)) {
                label.setText("Connexion réussie");
                label.setStyle("-fx-text-fill: green;");

                // Redirection vers la page suivante après connexion réussie
                Stage stage = (Stage) label.getScene().getWindow();
                changerVue(stage, "interface-view.fxml");

            } else {
                label.setText("Identifiant ou mot de passe incorrect");
                label.setStyle("-fx-text-fill: red;");
                text.setText("");
                passwd.setText("");
            }

        } catch (NumberFormatException e) {
            label.setText("L'identifiant doit être un nombre");
            label.setStyle("-fx-text-fill: red;");
            text.setText("");
            passwd.setText("");
        }
    }


    @FXML
    private void onClickButtonConnexion() {
        // Implémenter l'interface du compte courant
        Stage stage = (Stage) connexion.getScene().getWindow();
        changerVue(stage, "interface-view.fxml");
    }

    @FXML
    private void onClickButtonMainPage(ActionEvent event) {
        // Obtenir le bouton qui a déclenché l'événement
        Button button = (Button) event.getSource();
        // Obtenir la scène à partir du bouton
        Stage stage = (Stage) button.getScene().getWindow();
        changerVue(stage, "hello-view.fxml");
    }

    @FXML
    private void creerNouveauCompte() {
        try {
            // Vérification des champs
            if (nomUtilisateur.getText().isEmpty() || identifiantField.getText().isEmpty()
                    || ribField.getText().isEmpty() || motDePasseField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires");
                return;
            }

            // Vérification de la correspondance des mots de passe
            if (!motDePasseField.getText().equals(confirmationMotDePasse.getText())) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas");
                return;
            }

            // Création du compte
            CompteBanque nouveauCompte = new CompteBanque(
                    nomUtilisateur.getText(),
                    0.0, // solde initial
                    Integer.parseInt(identifiantField.getText()),
                    Integer.parseInt(ribField.getText()),
                    Integer.parseInt(motDePasseField.getText()),
                    0, // virement initial
                    0  // retrait initial
            );

            // Afficher confirmation et rediriger vers la page de connexion
            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Compte créé avec succès pour " + nomUtilisateur.getText());

            Stage stage = (Stage) nomUtilisateur.getScene().getWindow();
            changerVue(stage, "connexion-view.fxml");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "L'identifiant, le RIB et le mot de passe doivent être des nombres");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Une erreur est survenue : " + e.getMessage());
        }
    }

    @FXML
    private void creerNouveauClient() {

        if (nomField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom est obligatoire");
            nomField.setStyle("-fx-border-color: red;");

        }

        if (prenomField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prénom est obligatoire");
            prenomField.setStyle("-fx-border-color: red;");

        }


        nomField.setStyle("");
        prenomField.setStyle("");

        try {

            Adresse.Commune commune = new Adresse.Commune(
                    Integer.parseInt(codePostalField.getText()),
                    villeField.getText(),
                    Integer.parseInt(departementField.getText())
            );

            // Création de l'adresse
            Adresse adresse = new Adresse(
                    Integer.parseInt(numeroRueField.getText()),
                    rueField.getText(),
                    commune
            );

            // Création du client
            Client nouveauClient = new Client(
                    nomField.getText(),
                    adresse,
                    prenomField.getText(),
                    Integer.parseInt(codePostalField.getText()),
                    Integer.parseInt(ageField.getText()),
                    LocalDate.parse(dateNaissanceField.getText()),
                    Integer.parseInt(numeroClientField.getText())
            );

            // Ajout du client à la liste
            Client.addClient(nouveauClient);

            // Création du compte bancaire associé
            CompteBanque nouveauCompte = new CompteBanque(
                    nouveauClient.getNom(),
                    0.0, // solde initial
                    Integer.parseInt(identifiantField.getText()),
                    Integer.parseInt(ribField.getText()),
                    Integer.parseInt(motDePasseField.getText()),
                    0, // virement initial
                    0  // retrait initial
            );

            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Client et compte créés avec succès");

            // Redirection vers la page de connexion
            Stage stage = (Stage) nomField.getScene().getWindow();
            changerVue(stage, "connexion-view.fxml");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Les champs numériques sont invalides");
        } catch (CategorieException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    public static void main(String[] args) {
        launch();
    }
}
