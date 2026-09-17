package fr.univartois.butinfo.ihm;


public class Adresse {

private int numero;
private String voie;
private Commune commune;


    public Adresse(int numero,String voie, Commune commune) {
        this.numero=numero;
        this.voie=voie;
        this.commune=commune;
    }

    public int getNumero() {
        return numero;
    }
    public String getVoie() {
        return voie;
    }
    public Commune getCommune() {
        return commune;
    }

    public int length() {
        return voie.length();
    }

    static class Commune{

        private int codeINSEE;
        private String nomCommune;
        private int departement;
        ///création d'un constructeur
        public Commune(int codeINSEE,String nomCommune,int departement) {
            this.codeINSEE=codeINSEE;
            this.nomCommune=nomCommune;
            this.departement=departement;
        }

        public int getCodeINSEE() {
            return codeINSEE;
        }

        public String getNomCommune() {
            return nomCommune;
        }

        public int getDepartement() {
            return departement;
        }

        @Override
        public String toString() {
            return "codeINSEE=" + codeINSEE + ", nomCommune=" + nomCommune + ", departement=" + departement;
        }





    }

    @Override
    public String toString() {
        return "Numero=" + numero + ", voie=" + voie + ", commune=" + commune;
    }

    public class Main {
        public static void main(String[] args) {

            Commune commune1 = new Commune(59000, "Lille", 59);
            Commune commune2 = new Commune(75000, "Paris", 75);
            Commune commune3 = new Commune(13000, "Marseille", 13);


            Adresse adresse1 = new Adresse(12, "Rue de la République", commune1);
            Adresse adresse2 = new Adresse(34, "Avenue des Champs-Élysées", commune2);
            Adresse adresse3 = new Adresse(56, "Boulevard Saint-Michel", commune3);

            System.out.println(adresse1.toString());

        }
    }





}




