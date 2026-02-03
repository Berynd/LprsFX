package model;

public class DemandeFourniture {

    private int idDemandeFourniture;
    private String date;
    private  String statut;
    private String raison;
    private String justificationRefus;
    private int refProfesseur;

    public DemandeFourniture() {
    }


    public DemandeFourniture(String date, String statut,
                             String raison, int refProfesseur) {
        this.date = date;
        this.statut = statut;
        this.raison = raison;
        this.refProfesseur = refProfesseur;
    }

    public int getIdDemandeFourniture() {
        return idDemandeFourniture;
    }
    public void setIdDemandeFourniture(int IdDemandeFourniture) {
        this.idDemandeFourniture=IdDemandeFourniture;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut=statut;
    }
    public String getRaison() {
        return raison;
    }
    public void setRaison(String raison) {
        this.raison=raison;
    }
    public String getJustificationRefus() {
        return justificationRefus;
    }
    public void setJustificationRefus(String justificationRefus) {
        this.justificationRefus=justificationRefus;
    }
    public int getRefProfesseur() {
        return refProfesseur;
    }
    public void setRefProfesseur(int refProfesseur) {
        this.refProfesseur=refProfesseur;
    }

    @Override
    public String toString() {
        return "DemandeFourniture{" +
                "IdDemandeFourniture=" + idDemandeFourniture +
                ", date='" + date + '\'' +
                ", statut='" + statut + '\'' +
                ", raison='" + raison + '\'' +
                ", justificationRefus='" + justificationRefus + '\'' +
                ", refProfesseur=" + refProfesseur +
                '}';
    }
}
