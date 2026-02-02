package model;

public class Etudiant {

    private int IdEtudiant;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String dernierDiplome;

    public int getIdEtudiant() {
        return IdEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        IdEtudiant = idEtudiant;
    }

    public String getDernierDiplome() {
        return dernierDiplome;
    }

    public void setDernierDiplome(String dernierDiplome) {
        this.dernierDiplome = dernierDiplome;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
