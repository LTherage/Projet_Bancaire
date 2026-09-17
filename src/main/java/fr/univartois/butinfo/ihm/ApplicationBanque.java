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
    private TextField text, nomUtilisateur, identifiantField, motifVirementField, montantVirementField,ribField, codePostalField, villeField, departementField,numeroRueField, rueField, nomField, prenomField, numeroClientField, ageField, emailField, telephoneField;
    @FXML
    private Button connexion;
    @FXML
    private PasswordField passwd, motDePasseField, confirmationMotDePasse;

    @FXML
    private DatePicker dateNaissanceField;

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
        changerVue(stage, fxml, SessionManager.getCurrentCompte());
    }

    public static void changerVue(Stage stage, String fxml, CompteBanque compte) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationBanque.class.getResource(fxml));
            Parent root = loader.load();

            if (compte != null && "interface-view.fxml".equals(fxml)) {
                Object controller = loader.getController();
                if (controller instanceof CompteBanqueController compteController) {
                    compteController.setCompteActuel(compte);
                    compteController.setComptesDisponibles(
                            CompteBanque.getComptes().stream()
                                    .filter(c -> c != compte)
                                    .toList());
                }
            }

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

            if (identifiantSaisi == null || identifiantSaisi.isBlank() || motDePasseSaisi == null || motDePasseSaisi.isBlank()) {
                label.setText("Veuillez remplir tous les champs");
                label.setStyle("-fx-text-fill: red;");
                return;
            }

            int id = Integer.parseInt(identifiantSaisi);
            CompteBanque compte = CompteBanque.authentifier(id, motDePasseSaisi);

            if (compte != null) {
                label.setText("Connexion réussie");
                label.setStyle("-fx-text-fill: green;");
                SessionManager.setCurrentCompte(compte);

                Stage stage = (Stage) label.getScene().getWindow();
                if (compte.getRole() == CompteBanque.Role.ADMIN) {
                    changerVue(stage, "dashboard-view.fxml", compte);
                } else if (compte.getRole() == CompteBanque.Role.EMPLOYE) {
                    changerVue(stage, "dashboard-view.fxml", compte);
                } else {
                    changerVue(stage, "dashboard-view.fxml", compte);
                }
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
        SessionManager.clear();
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        changerVue(stage, "hello-view.fxml");
    }

    @FXML
    private void deconnexion() {
        SessionManager.clear();
        Stage stage = (Stage) connexion.getScene().getWindow();
        changerVue(stage, "hello-view.fxml");
    }

    @FXML
    private void ouvrirNavigation() {
        Stage stage = (Stage) connexion.getScene().getWindow();
        changerVue(stage, "navigation-view.fxml");
    }

    @FXML
    private void creerNouveauCompte() {
        try {
            // Vérification des champs
            if (nomField.getText().isEmpty() || identifiantField.getText().isEmpty()
                    || prenomField.getText().isEmpty() || motDePasseField.getText().isEmpty()) {
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
                    nomField.getText(),
                    0.0, // solde initial
                    Integer.parseInt(identifiantField.getText()),
                    Integer.parseInt(prenomField.getText()),
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
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom est obligatoire");
            nomField.setStyle("-fx-border-color: red;");
            return;
        }

        if (prenomField.getText() == null || prenomField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prénom est obligatoire");
            prenomField.setStyle("-fx-border-color: red;");
            return;
        }

        if (ageField.getText() == null || ageField.getText().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'âge est obligatoire");
            ageField.setStyle("-fx-border-color: red;");
            return;
        }

        if (motDePasseField.getText() == null || motDePasseField.getText().isBlank() ||
                !motDePasseField.getText().equals(confirmationMotDePasse.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas");
            motDePasseField.setStyle("-fx-border-color: red;");
            confirmationMotDePasse.setStyle("-fx-border-color: red;");
            return;
        }

        if (conditionsCheckBox == null || !conditionsCheckBox.isSelected()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Vous devez accepter les conditions d'utilisation");
            return;
        }

        nomField.setStyle("");
        prenomField.setStyle("");
        ageField.setStyle("");
        motDePasseField.setStyle("");
        confirmationMotDePasse.setStyle("");

        try {
            int age = Integer.parseInt(ageField.getText());
            if (age < 18) {
                throw new AgeException("Le client doit être majeur pour ouvrir un compte");
            }

            int numeroClient = numeroClientField.getText() == null || numeroClientField.getText().isBlank()
                    ? Integer.parseInt(identifiantField.getText())
                    : Integer.parseInt(numeroClientField.getText());

            int codePostal = codePostalField.getText() == null || codePostalField.getText().isBlank()
                    ? 0
                    : Integer.parseInt(codePostalField.getText());
            int departement = departementField.getText() == null || departementField.getText().isBlank()
                    ? 0
                    : Integer.parseInt(departementField.getText());
            int numeroRue = numeroRueField.getText() == null || numeroRueField.getText().isBlank()
                    ? 0
                    : Integer.parseInt(numeroRueField.getText());

            Adresse.Commune commune = new Adresse.Commune(codePostal, villeField.getText(), departement);
            Adresse adresse = new Adresse(numeroRue, rueField.getText(), commune);

            Client nouveauClient = new Client(
                    nomField.getText(),
                    adresse,
                    prenomField.getText(),
                    codePostal,
                    age,
                    dateNaissanceField.getValue(),
                    numeroClient
            );
            Client.addClient(nouveauClient);

            int identifiant = Integer.parseInt(identifiantField.getText());
            int rib = numeroClient > 0 ? numeroClient + 100000 : identifiant + 100000;
            int password = Integer.parseInt(motDePasseField.getText());

            CompteBanque nouveauCompte = new CompteBanque(
                    nouveauClient.getNom() + " " + nouveauClient.getPrenom(),
                    0.0,
                    identifiant,
                    rib,
                    password,
                    0,
                    0
            );

            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Client et compte créés avec succès pour " + nouveauCompte.getNomProprietaire());

            Stage stage = (Stage) nomField.getScene().getWindow();
            changerVue(stage, "connexion-view.fxml");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Les champs numériques sont invalides");
        } catch (CategorieException | AgeException e) {
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
