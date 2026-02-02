package model;

public class DossierInscription {

    private int IdDossierInscription;
    private String dateCreation;
    private String motivation;
    private String statut;
    private int RefFiliere;
    private int RefEtudiant;
    private int RefSecretaire;

    public int getIdDossierInscription() {
        return IdDossierInscription;
    }

    public void setIdDossierInscription(int idDossierInscription) {
        IdDossierInscription = idDossierInscription;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getRefFiliere() {
        return RefFiliere;
    }

    public void setRefFiliere(int refFiliere) {
        RefFiliere = refFiliere;
    }

    public int getRefEtudiant() {
        return RefEtudiant;
    }

    public void setRefEtudiant(int refEtudiant) {
        RefEtudiant = refEtudiant;
    }

    public int getRefSecretaire() {
        return RefSecretaire;
    }

    public void setRefSecretaire(int refSecretaire) {
        RefSecretaire = refSecretaire;
    }
}
