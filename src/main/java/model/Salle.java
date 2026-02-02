package model;

public class Salle {
    
    private int idSalle;
    private String nom;

    // Constructeurs
    public Salle() {
    }

    public Salle(int idSalle, String nom) {
        this.idSalle = idSalle;
        this.nom = nom;
    }

    public Salle(String nom) {
        this.nom = nom;
    }

    // Getters et Setters
    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
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
