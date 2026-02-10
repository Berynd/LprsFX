package model;

public class Salle {

    private Integer idSalle;  // ← CHANGEMENT ICI : Integer au lieu de int
    private String nom;

    // Constructeurs
    public Salle(Integer idSalle, String nom) {  // ← Integer ici aussi
        this.idSalle = idSalle;
        this.nom = nom;
    }

    public Salle(String nom) {
        this.nom = nom;
    }

    // Getters et Setters
    public Integer getIdSalle() {  // ← Integer ici aussi
        return idSalle;
    }

    public void setIdSalle(Integer idSalle) {  // ← Integer ici aussi
        this.idSalle = idSalle;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Salle{" +
                "idSalle=" + idSalle +
                ", nom='" + nom + '\'' +
                '}';
    }
}