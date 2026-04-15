package model;

import java.time.LocalDateTime;

/**
 * Représente une entrée dans le journal d'activité de l'application.
 *
 * Chaque action importante (connexion, ajout, modification, suppression…)
 * génère un Log via LogService.log(). Les logs sont consultables dans la vue LogView.
 *
 * Les champs sont :
 *  - dateHeure   : horodatage automatique à la création
 *  - nomUtilisateur : prénom + nom de l'utilisateur connecté
 *  - action      : type d'opération en majuscules (CONNEXION, AJOUTER, MODIFIER, SUPPRIMER…)
 *  - page        : nom du controller où l'action s'est produite
 *  - messageLog  : description lisible de l'action
 */
public class Log {

    private int idLog;
    private LocalDateTime dateHeure;
    private String nomUtilisateur;
    private String messageLog;
    private String action;
    private String page;

    /** Constructeur vide requis pour la lecture en base (via setters). */
    public Log() {}

    /**
     * Constructeur utilisé par LogService pour créer un nouveau log.
     * L'horodatage est automatiquement fixé à l'instant présent.
     */
    public Log(String nomUtilisateur, String messageLog, String action, String page) {
        this.dateHeure = LocalDateTime.now();
        this.nomUtilisateur = nomUtilisateur;
        this.messageLog = messageLog;
        this.action = action;
        this.page = page;
    }

    // --- Getters / Setters ---

    public int getIdLog()                               { return idLog; }
    public void setIdLog(int idLog)                     { this.idLog = idLog; }

    public LocalDateTime getDateHeure()                 { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure)   { this.dateHeure = dateHeure; }

    public String getNomUtilisateur()                   { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur){ this.nomUtilisateur = nomUtilisateur; }

    public String getMessageLog()                       { return messageLog; }
    public void setMessageLog(String messageLog)        { this.messageLog = messageLog; }

    public String getAction()                           { return action; }
    public void setAction(String action)                { this.action = action; }

    public String getPage()                             { return page; }
    public void setPage(String page)                    { this.page = page; }

    @Override
    public String toString() {
        return idLog + " - " + dateHeure + " : " + nomUtilisateur
                + " | " + action + " | " + page + " | " + messageLog;
    }
}
