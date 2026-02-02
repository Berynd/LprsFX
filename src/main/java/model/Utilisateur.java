package model;

import eu.hansolo.toolbox.time.DateTimes;

public class Utilisateur {

    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private String role;
    private String derniereConnexion;
    private int refFiliere;

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getmdp() {
        return mdp;
    }

    public void setmdp(String mdp) {
        this.mdp = mdp;
    }

    public int getRefFiliere() {
        return refFiliere;
    }

    public void setRefFiliere(int refFiliere) {
        this.refFiliere = refFiliere;
    }

    public String getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(String derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }


    public Utilisateur(int idUtilisateur,  String nom, String prenom,String email,String mdp, String role) {
        this.idUtilisateur = idUtilisateur;
        this.role = role;
        this.mdp = mdp;
        this.email = email;
        this.prenom = prenom;
        this.nom = nom;
    }

    public Utilisateur(String nom, String prenom, String email, String mdp,String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.mdp = mdp;
    }
    public Utilisateur(int idUtilisateur,String nom, String prenom, String email) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;

    }

    public Utilisateur(String email, String mdp) {
        this.email = email;
        this.mdp = mdp;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
