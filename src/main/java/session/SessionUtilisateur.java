package session;

import model.Utilisateur;

/**
 * Singleton qui représente la session de l'utilisateur connecté.
 *
 * Une seule instance existe pendant toute la durée de l'application.
 * Elle stocke l'utilisateur authentifié et permet à n'importe quel
 * controller d'y accéder sans passer l'objet de main en main.
 *
 * Utilisation typique :
 *   SessionUtilisateur.getInstance().getUtilisateurConnecte()
 */
public class SessionUtilisateur {

    /** Instance unique (Singleton). */
    private static SessionUtilisateur instance;

    /** L'utilisateur actuellement connecté, null si personne n'est connecté. */
    private Utilisateur utilisateurConnecte;

    /** Constructeur privé : empêche l'instanciation directe. */
    private SessionUtilisateur() {
        this.utilisateurConnecte = null;
    }

    /**
     * Retourne l'instance unique de la session.
     * Crée l'instance si elle n'existe pas encore.
     */
    public static SessionUtilisateur getInstance() {
        if (instance == null) {
            instance = new SessionUtilisateur();
        }
        return instance;
    }

    /** Enregistre l'utilisateur qui vient de se connecter. */
    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }

    /** Retourne l'utilisateur connecté, ou null si personne n'est connecté. */
    public Utilisateur getUtilisateurConnecte() {
        return this.utilisateurConnecte;
    }

    /** @return true si un utilisateur est connecté. */
    public boolean estConnecte() {
        return this.utilisateurConnecte != null;
    }

    /** Déconnecte l'utilisateur en vidant la session. */
    public void deconnecter() {
        this.utilisateurConnecte = null;
    }

    /**
     * @return le prénom + nom de l'utilisateur connecté,
     *         ou "Invité" si personne n'est connecté.
     */
    public String getNomComplet() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom();
        }
        return "Invité";
    }

    /** @return le rôle de l'utilisateur connecté, ou null. */
    public String getRole() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getRole();
        }
        return null;
    }

    /** @return l'email de l'utilisateur connecté, ou null. */
    public String getEmail() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getEmail();
        }
        return null;
    }
}
