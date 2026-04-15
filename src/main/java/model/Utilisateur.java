package model;

/**
 * Représente un utilisateur de l'application (personnel administratif).
 *
 * Rôles possibles : "Admin", "Secrétaire", "Professeur", "Gestionnaire de stock".
 * Le mot de passe est toujours stocké hashé avec BCrypt (jamais en clair).
 * L'id est 0 tant que l'objet n'a pas été persisté en base.
 */
public class Utilisateur {

    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;       // toujours hashé BCrypt
    private String role;
    private String derniereConnexion;
    private int refFiliere;

    // --- Constructeurs ---

    /** Constructeur complet, utilisé lors de la lecture en base (avec id). */
    public Utilisateur(int idUtilisateur, String nom, String prenom, String email, String mdp, String role) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
    }

    /** Constructeur sans id, utilisé lors de la création d'un nouvel utilisateur. */
    public Utilisateur(String nom, String prenom, String email, String mdp, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
    }

    /** Constructeur léger (id + infos d'affichage), sans mot de passe. */
    public Utilisateur(int idUtilisateur, String nom, String prenom, String email) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    /** Constructeur minimal pour la vérification d'email uniquement. */
    public Utilisateur(String email, String mdp) {
        this.email = email;
        this.mdp = mdp;
    }

    // --- Getters / Setters ---

    public int getIdUtilisateur()                        { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur)      { this.idUtilisateur = idUtilisateur; }

    public String getNom()                               { return nom; }
    public void setNom(String nom)                       { this.nom = nom; }

    public String getPrenom()                            { return prenom; }
    public void setPrenom(String prenom)                 { this.prenom = prenom; }

    public String getEmail()                             { return email; }
    public void setEmail(String email)                   { this.email = email; }

    /** @return le mot de passe hashé BCrypt. */
    public String getMdp()                               { return mdp; }
    public void setMdp(String mdp)                       { this.mdp = mdp; }

    public String getRole()                              { return role; }
    public void setRole(String role)                     { this.role = role; }

    public String getDerniereConnexion()                 { return derniereConnexion; }
    public void setDerniereConnexion(String dc)          { this.derniereConnexion = dc; }

    public int getRefFiliere()                           { return refFiliere; }
    public void setRefFiliere(int refFiliere)            { this.refFiliere = refFiliere; }

    @Override
    public String toString() {
        return "Utilisateur{id=" + idUtilisateur + ", nom='" + nom + "', prenom='" + prenom
                + "', email='" + email + "', role='" + role + "'}";
    }
}
