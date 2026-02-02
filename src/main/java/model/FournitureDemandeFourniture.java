package model;

/**
 * Classe représentant la relation entre une demande de fourniture et une fourniture avec sa quantité
 */
public class FournitureDemandeFourniture {
    
    private int refDemandeFourniture;
    private int refFourniture;
    private int quantite;
    
    // Pour faciliter l'affichage, on peut aussi stocker les objets liés
    private Fourniture fourniture;

    // Constructeurs
    public FournitureDemandeFourniture() {
    }

    public FournitureDemandeFourniture(int refDemandeFourniture, int refFourniture, int quantite) {
        this.refDemandeFourniture = refDemandeFourniture;
        this.refFourniture = refFourniture;
        this.quantite = quantite;
    }

    public FournitureDemandeFourniture(int refDemandeFourniture, int refFourniture, 
                                       int quantite, Fourniture fourniture) {
        this.refDemandeFourniture = refDemandeFourniture;
        this.refFourniture = refFourniture;
        this.quantite = quantite;
        this.fourniture = fourniture;
    }

    // Getters et Setters
    public int getRefDemandeFourniture() {
        return refDemandeFourniture;
    }

    public void setRefDemandeFourniture(int refDemandeFourniture) {
        this.refDemandeFourniture = refDemandeFourniture;
    }

    public int getRefFourniture() {
        return refFourniture;
    }

    public void setRefFourniture(int refFourniture) {
        this.refFourniture = refFourniture;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Fourniture getFourniture() {
        return fourniture;
    }

    public void setFourniture(Fourniture fourniture) {
        this.fourniture = fourniture;
    }

    @Override
    public String toString() {
        return "FournitureDemandeFourniture{" +
                "refDemandeFourniture=" + refDemandeFourniture +
                ", refFourniture=" + refFourniture +
                ", quantite=" + quantite +
                ", fourniture=" + (fourniture != null ? fourniture.getLibelle() : "null") +
                '}';
    }
}
