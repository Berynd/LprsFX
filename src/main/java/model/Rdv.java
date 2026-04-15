package model;

import java.time.LocalDate;

/**
 * Représente un rendez-vous entre un professeur et un étudiant dans une salle.
 *
 * Un créneau est défini par une date + une demi-journée ("Matin" ou "Après-midi").
 * Avant de créer un RDV, il faut vérifier que la salle est disponible sur ce créneau
 * via RdvRepository.isSalleDisponible().
 */
public class Rdv {

    private int idRdv;
    private LocalDate date;
    private String demiJournee;  // "Matin" ou "Après-midi"
    private int refEtudiant;     // clé étrangère vers l'étudiant
    private int refProfesseur;   // clé étrangère vers l'utilisateur (rôle Professeur)
    private int refSalle;        // clé étrangère vers la salle

    // --- Getters / Setters ---

    public int getIdRdv()                           { return idRdv; }
    public void setIdRdv(int idRdv)                 { this.idRdv = idRdv; }

    public LocalDate getDate()                      { return date; }
    public void setDate(LocalDate date)             { this.date = date; }

    public String getDemiJournee()                  { return demiJournee; }
    public void setDemiJournee(String demiJournee)  { this.demiJournee = demiJournee; }

    public int getRefEtudiant()                     { return refEtudiant; }
    public void setRefEtudiant(int refEtudiant)     { this.refEtudiant = refEtudiant; }

    public int getRefProfesseur()                   { return refProfesseur; }
    public void setRefProfesseur(int refProfesseur) { this.refProfesseur = refProfesseur; }

    public int getRefSalle()                        { return refSalle; }
    public void setRefSalle(int refSalle)           { this.refSalle = refSalle; }

    @Override
    public String toString() {
        return "Rdv{id=" + idRdv + ", date=" + date + ", demiJournee='" + demiJournee
                + "', etudiant=" + refEtudiant + ", professeur=" + refProfesseur + ", salle=" + refSalle + '}';
    }
}
