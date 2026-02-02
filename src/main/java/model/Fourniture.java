package model;

public class Fourniture {
    
    private int idFourniture;
    private String libelle;
    private String description;
    private int stockActuel;

    // Constructeurs
    public Fourniture() {
    }

    public Fourniture(int idFourniture, String libelle, String description, int stockActuel) {
        this.idFourniture = idFourniture;
        this.libelle = libelle;
        this.description = description;
        this.stockActuel = stockActuel;
    }

    public Fourniture(String libelle, String description, int stockActuel) {
        this.libelle = libelle;
        this.description = description;
        this.stockActuel = stockActuel;
    }

    // Getters et Setters
    public int getIdFourniture() {
        return idFourniture;
    }

    public void setIdFourniture(int idFourniture) {
        this.idFourniture = idFourniture;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockActuel() {
        return stockActuel;
    }

    public void setStockActuel(int stockActuel) {
        this.stockActuel = stockActuel;
    }

    @Override
    public String toString() {
        return "Fourniture{" +
                "idFourniture=" + idFourniture +
                ", libelle='" + libelle + '\'' +
                ", description='" + description + '\'' +
                ", stockActuel=" + stockActuel +
                '}';
    }
}
