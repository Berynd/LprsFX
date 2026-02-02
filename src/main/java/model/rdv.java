package model;

public class rdv {

    private int IdRdv;
    private String date;
    private String demiJournee;
    private int RefEtudiant;
    private int RefProfesseur;
    private int RefSalle;

    public int getIdRdv() {
        return IdRdv;
    }

    public void setIdRdv(int idRdv) {
        IdRdv = idRdv;
    }

    public int getRefSalle() {
        return RefSalle;
    }

    public void setRefSalle(int refSalle) {
        RefSalle = refSalle;
    }

    public int getRefProfesseur() {
        return RefProfesseur;
    }

    public void setRefProfesseur(int refProfesseur) {
        RefProfesseur = refProfesseur;
    }

    public int getRefEtudiant() {
        return RefEtudiant;
    }

    public void setRefEtudiant(int refEtudiant) {
        RefEtudiant = refEtudiant;
    }

    public String getDemiJournee() {
        return demiJournee;
    }

    public void setDemiJournee(String demiJournee) {
        this.demiJournee = demiJournee;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
