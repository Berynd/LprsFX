package model;

import java.time.LocalDate;

/**
 * Représente la demande d'inscription d'un étudiant dans une filière.
 *
 * Cycle de vie du statut : "En attente" → "Validé" ou "Refusé".
 * Un dossier est créé par la secrétaire et rattaché à un étudiant, une filière
 * et la secrétaire qui l'a saisi.
 */
public class DossierInscription {

    private int idDossierInscription;
    private LocalDate dateCreation;
    private String motivation;   // lettre de motivation de l'étudiant
    private String statut;       // "En attente", "Validé", "Refusé"
    private int refFiliere;      // clé étrangère vers la filière visée
    private int refEtudiant;     // clé étrangère vers l'étudiant concerné
    private int refSecretaire;   // clé étrangère vers la secrétaire qui a saisi le dossier

    /** Constructeur minimal utilisé lors de la lecture en base (id uniquement). */
    public DossierInscription(int idDossierInscription) {
        this.idDossierInscription = idDossierInscription;
    }

    // --- Getters / Setters ---

    public int getIdDossierInscription()                        { return idDossierInscription; }
    public void setIdDossierInscription(int id)                 { this.idDossierInscription = id; }

    public LocalDate getDateCreation()                          { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation)         { this.dateCreation = dateCreation; }

    public String getMotivation()                               { return motivation; }
    public void setMotivation(String motivation)                { this.motivation = motivation; }

    public String getStatut()                                   { return statut; }
    public void setStatut(String statut)                        { this.statut = statut; }

    public int getRefFiliere()                                  { return refFiliere; }
    public void setRefFiliere(int refFiliere)                   { this.refFiliere = refFiliere; }

    public int getRefEtudiant()                                 { return refEtudiant; }
    public void setRefEtudiant(int refEtudiant)                 { this.refEtudiant = refEtudiant; }

    public int getRefSecretaire()                               { return refSecretaire; }
    public void setRefSecretaire(int refSecretaire)             { this.refSecretaire = refSecretaire; }

    @Override
    public String toString() {
        return "DossierInscription{id=" + idDossierInscription + ", date=" + dateCreation
                + ", statut='" + statut + "', etudiant=" + refEtudiant + ", filiere=" + refFiliere + '}';
    }
}
