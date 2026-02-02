package model;

import java.math.BigDecimal;

/**
 * Classe représentant la relation entre une fourniture et un fournisseur avec son prix
 */
public class FournitureFournisseur {
    
    private int refFourniture;
    private int refFournisseur;
    private BigDecimal prix;
    
    // Pour faciliter l'affichage, on peut aussi stocker les objets liés
    private Fourniture fourniture;
    private Fournisseur fournisseur;

    // Constructeurs
    public FournitureFournisseur() {
    }

    public FournitureFournisseur(int refFourniture, int refFournisseur, BigDecimal prix) {
        this.refFourniture = refFourniture;
        this.refFournisseur = refFournisseur;
        this.prix = prix;
    }

    public FournitureFournisseur(int refFourniture, int refFournisseur, BigDecimal prix, 
                                  Fourniture fourniture, Fournisseur fournisseur) {
        this.refFourniture = refFourniture;
        this.refFournisseur = refFournisseur;
        this.prix = prix;
        this.fourniture = fourniture;
        this.fournisseur = fournisseur;
    }

    // Getters et Setters
    public int getRefFourniture() {
        return refFourniture;
    }

    public void setRefFourniture(int refFourniture) {
        this.refFourniture = refFourniture;
    }

    public int getRefFournisseur() {
        return refFournisseur;
    }

    public void setRefFournisseur(int refFournisseur) {
        this.refFournisseur = refFournisseur;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Fourniture getFourniture() {
        return fourniture;
    }

    public void setFourniture(Fourniture fourniture) {
        this.fourniture = fourniture;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    @Override
    public String toString() {
        return "FournitureFournisseur{" +
                "refFourniture=" + refFourniture +
                ", refFournisseur=" + refFournisseur +
                ", prix=" + prix +
                ", fourniture=" + (fourniture != null ? fourniture.getLibelle() : "null") +
                ", fournisseur=" + (fournisseur != null ? fournisseur.getNom() : "null") +
                '}';
    }
}
