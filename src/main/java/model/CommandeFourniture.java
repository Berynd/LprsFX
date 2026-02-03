package model;

public class CommandeFourniture {

    private int idCommandeFourniture;
    private String date;
    private String status;
    private String justificationRefus;
    private int refGestionnaire;
    private int refFournisseur;


    public CommandeFourniture() {

    }


    public CommandeFourniture(String date,String status,
                              int refGestionnaire,int refFournisseur) {
        this.date = date;
        this.status = status;
        this.refGestionnaire = refGestionnaire;
        this.refFournisseur = refFournisseur;
    }


    public int getIdCommandeFourniture() {
        return idCommandeFourniture;
    }

    public void setIdCommandeFourniture(int idCommandeFourniture) {
        this.idCommandeFourniture = idCommandeFourniture;
    }

    public String getDate() {
        return date;

    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJustificationRefus() {
        return justificationRefus;
    }

    public void setJustificationRefus(String justificationRefus) {
        this.justificationRefus = justificationRefus;
    }

    public int getRefGestionnaire() {
        return refGestionnaire;
    }

    public void setRefGestionnaire(int refGestionnaire) {
        this.refGestionnaire = refGestionnaire;
    }

    public int getRefFournisseur() {
        return refFournisseur;
    }

    public void setRefFournisseur(int refFournisseur) {
        this.refFournisseur = refFournisseur;
    }

    @Override
    public String toString() {
        return "CommandeFourniture{" +
                "idCommandeFourniture=" + idCommandeFourniture +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", justificationRefus='" + justificationRefus + '\'' +
                ", gestionnaire=" + refGestionnaire +
                ", fournisseur=" + refFournisseur +
                '}';
    }
}