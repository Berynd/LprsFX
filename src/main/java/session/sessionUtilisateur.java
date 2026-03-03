package session;

import model.Utilisateur;

public class SessionUtilisateur {

    private static SessionUtilisateur instance;

    private Utilisateur utilisateurConnecte;

    private SessionUtilisateur() {
        this.utilisateurConnecte = null;
    }

    public static SessionUtilisateur getInstance() {
        if (instance == null) {
            instance = new SessionUtilisateur();
        }
        return instance;
    }

    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }

    public Utilisateur getUtilisateurConnecte() {
        return this.utilisateurConnecte;
    }

    public boolean estConnecte() {
        return this.utilisateurConnecte != null;
    }

    public void deconnecter() {
        this.utilisateurConnecte = null;
    }

    public String getNomComplet() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom();
        }
        return "Invité";
    }

    public String getRole() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getRole();
        }
        return null;
    }

    public String getEmail() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getEmail();
        }
        return null;
    }
}
