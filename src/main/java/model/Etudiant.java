package model;

/**
 * Représente un étudiant candidat à l'inscription.
 *
 * Un étudiant peut avoir plusieurs dossiers d'inscription (un par filière visée)
 * et plusieurs rendez-vous avec des professeurs.
 */
public class Etudiant {

    private int idEtudiant;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String dernierDiplome;

    /** Constructeur complet utilisé lors de la lecture en base. */
    public Etudiant(int idEtudiant, String nom, String prenom, String email,
                    String telephone, String adresse, String dernierDiplome) {
        this.idEtudiant = idEtudiant;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dernierDiplome = dernierDiplome;
    }

    // --- Getters / Setters ---

    public int getIdEtudiant()                          { return idEtudiant; }
    public void setIdEtudiant(int idEtudiant)           { this.idEtudiant = idEtudiant; }

    public String getNom()                              { return nom; }
    public void setNom(String nom)                      { this.nom = nom; }

    public String getPrenom()                           { return prenom; }
    public void setPrenom(String prenom)                { this.prenom = prenom; }

    public String getEmail()                            { return email; }
    public void setEmail(String email)                  { this.email = email; }

    public String getTelephone()                        { return telephone; }
    public void setTelephone(String telephone)          { this.telephone = telephone; }

    public String getAdresse()                          { return adresse; }
    public void setAdresse(String adresse)              { this.adresse = adresse; }

    public String getDernierDiplome()                   { return dernierDiplome; }
    public void setDernierDiplome(String dernierDiplome){ this.dernierDiplome = dernierDiplome; }

    @Override
    public String toString() {
        return "Etudiant{id=" + idEtudiant + ", nom='" + nom + "', prenom='" + prenom
                + "', email='" + email + "'}";
    }
}
