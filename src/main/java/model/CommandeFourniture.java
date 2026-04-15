package model;

import java.time.LocalDate;

/**
 * Représente une commande de fournitures passée auprès d'un fournisseur.
 *
 * Une commande est créée par un gestionnaire de stock.
 * Cycle de vie du statut : "En attente" → "Validé" ou "Refusé".
 * En cas de refus, la justification est renseignée dans justificationRefus.
 */
public class CommandeFourniture {

    private int idCommandeFourniture;
    private LocalDate date;
    private String statut;              // "En attente", "Validé", "Refusé"
    private String justificationRefus;  // remplie si la commande est refusée
    private int refGestionnaire;        // clé étrangère vers le gestionnaire qui passe la commande
    private int refFournisseur;         // clé étrangère vers le fournisseur visé

    public CommandeFourniture() {}

    /** Constructeur utilisé lors de la création d'une nouvelle commande. */
    public CommandeFourniture(LocalDate date, String statut, int refGestionnaire, int refFournisseur) {
        this.date = date;
        this.statut = statut;
        this.refGestionnaire = refGestionnaire;
        this.refFournisseur = refFournisseur;
    }

    // --- Getters / Setters ---

    public int getIdCommandeFourniture()                    { return idCommandeFourniture; }
    public void setIdCommandeFourniture(int id)             { this.idCommandeFourniture = id; }

    public LocalDate getDate()                              { return date; }
    public void setDate(LocalDate date)                     { this.date = date; }

    public String getStatut()                               { return statut; }
    public void setStatut(String statut)                    { this.statut = statut; }

    public String getJustificationRefus()                   { return justificationRefus; }
    public void setJustificationRefus(String justif)        { this.justificationRefus = justif; }

    public int getRefGestionnaire()                         { return refGestionnaire; }
    public void setRefGestionnaire(int refGestionnaire)     { this.refGestionnaire = refGestionnaire; }

    public int getRefFournisseur()                          { return refFournisseur; }
    public void setRefFournisseur(int refFournisseur)       { this.refFournisseur = refFournisseur; }

    @Override
    public String toString() {
        return "CommandeFourniture{id=" + idCommandeFourniture + ", date=" + date
                + ", statut='" + statut + "', fournisseur=" + refFournisseur + '}';
    }
}
