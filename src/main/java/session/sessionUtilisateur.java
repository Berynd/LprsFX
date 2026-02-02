package session;

import model.Utilisateur;

/**
 * Classe singleton pour gérer la session de l'utilisateur connecté
 */
public class sessionUtilisateur {
    
    // Instance unique (Singleton)
    private static sessionUtilisateur instance;
    
    // Utilisateur actuellement connecté
    private Utilisateur utilisateurConnecte;
    
    // Constructeur privé pour empêcher l'instanciation directe
    private sessionUtilisateur() {
        this.utilisateurConnecte = null;
    }
    
    /**
     * Obtenir l'instance unique de la session
     */
    public static sessionUtilisateur getInstance() {
        if (instance == null) {
            instance = new sessionUtilisateur();
        }
        return instance;
    }
    
    /**
     * Définir l'utilisateur connecté (lors de la connexion)
     */
    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        System.out.println("Session ouverte pour : " + utilisateur.getEmail());
    }
    
    /**
     * Obtenir l'utilisateur actuellement connecté
     */
    public Utilisateur getUtilisateurConnecte() {
        return this.utilisateurConnecte;
    }
    
    /**
     * Vérifier si un utilisateur est connecté
     */
    public boolean estConnecte() {
        return this.utilisateurConnecte != null;
    }
    
    /**
     * Déconnecter l'utilisateur (fermer la session)
     */
    public void deconnecter() {
        if (utilisateurConnecte != null) {
            System.out.println("Session fermée pour : " + utilisateurConnecte.getEmail());
            this.utilisateurConnecte = null;
        }
    }
    
    /**
     * Obtenir le nom complet de l'utilisateur connecté
     */
    public String getNomComplet() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom();
        }
        return "Invité";
    }
    
    /**
     * Obtenir le rôle de l'utilisateur connecté
     */
    public String getRole() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getRole();
        }
        return null;
    }
    
    /**
     * Obtenir l'email de l'utilisateur connecté
     */
    public String getEmail() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getEmail();
        }
        return null;
    }
}
