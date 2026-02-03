package model;

public class Rdv {

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

    @Override
    public String toString() {
        return "Rdv{" +
                "IdRdv=" + IdRdv +
                ", date='" + date + '\'' +
                ", demiJournee='" + demiJournee + '\'' +
                ", RefEtudiant=" + RefEtudiant +
                ", RefProfesseur=" + RefProfesseur +
                ", RefSalle=" + RefSalle +
                '}';
    }
}
