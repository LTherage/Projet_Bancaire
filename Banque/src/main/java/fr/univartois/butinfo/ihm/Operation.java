package fr.univartois.butinfo.ihm;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Operation {
        private final String date;
        private final String type;
        private final double montant;
        private final double solde;
        private final String motif;

        public Operation(String type, double montant, double solde, String motif) {
            this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            this.type = type;
            this.montant = montant;
            this.solde = solde;
            this.motif = motif;
        }

        // Getters
        public String getDate() { return date; }
        public String getType() { return type; }
        public double getMontant() { return montant; }
        public double getSolde() { return solde; }
        public String getMotif() { return motif; }
}



