package model;

import java.time.LocalDate;

/**
 * Représente une demande de fournitures émise par un professeur.
 *
 * Un professeur crée une demande en listant les fournitures dont il a besoin
 * (stockées dans la table de liaison FournitureDemandeFourniture).
 * Le gestionnaire de stock peut ensuite valider ou refuser la demande.
 *
 * Cycle de vie du statut : "En attente" → "Validé" ou "Refusé".
 * Quand une demande est validée, le stock des fournitures concernées est décrémenté.
 */
public class DemandeFourniture {

    private int idDemandeFourniture;
    private LocalDate date;
    private String statut;              // "En attente", "Validé", "Refusé"
    private String raison;              // motif de la demande rédigé par le professeur
    private String justificationRefus;  // explication du refus, remplie par le gestionnaire
    private int refProfesseur;          // clé étrangère vers l'utilisateur (rôle Professeur)

    public DemandeFourniture() {}

    /** Constructeur utilisé lors de la création d'une nouvelle demande. */
    public DemandeFourniture(LocalDate date, String statut, String raison, int refProfesseur) {
        this.date = date;
        this.statut = statut;
        this.raison = raison;
        this.refProfesseur = refProfesseur;
    }

    // --- Getters / Setters ---

    public int getIdDemandeFourniture()                     { return idDemandeFourniture; }
    public void setIdDemandeFourniture(int id)              { this.idDemandeFourniture = id; }

    public LocalDate getDate()                              { return date; }
    public void setDate(LocalDate date)                     { this.date = date; }

    public String getStatut()                               { return statut; }
    public void setStatut(String statut)                    { this.statut = statut; }

    public String getRaison()                               { return raison; }
    public void setRaison(String raison)                    { this.raison = raison; }

    public String getJustificationRefus()                   { return justificationRefus; }
    public void setJustificationRefus(String justif)        { this.justificationRefus = justif; }

    public int getRefProfesseur()                           { return refProfesseur; }
    public void setRefProfesseur(int refProfesseur)         { this.refProfesseur = refProfesseur; }

    @Override
    public String toString() {
        return "DemandeFourniture{id=" + idDemandeFourniture + ", date=" + date
                + ", statut='" + statut + "', professeur=" + refProfesseur + '}';
    }
}
