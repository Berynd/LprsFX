package service;

import model.Log;
import repository.LogRepository;
import session.SessionUtilisateur;

/**
 * Service de journalisation des actions utilisateurs.
 *
 * Chaque action importante (connexion, ajout, modification, suppression…)
 * doit appeler LogService.log() afin de laisser une trace en base de données.
 * Le nom de l'utilisateur est récupéré automatiquement depuis la session courante.
 *
 * Exemple d'appel depuis un controller :
 *   LogService.log("Étudiant ajouté : Dupont Jean", "AJOUTER", "FicheEtudiante");
 */
public class LogService {

    /** Repository utilisé pour persister les logs en base. */
    private static final LogRepository repo = new LogRepository();

    /**
     * Enregistre une entrée dans le journal d'activité.
     *
     * @param message description lisible de l'action effectuée
     * @param action  type d'action en majuscules (ex : "AJOUTER", "SUPPRIMER", "CONNEXION")
     * @param page    nom de la page / controller où l'action s'est produite
     */
    public static void log(String message, String action, String page) {
        // Récupère automatiquement le nom de l'utilisateur connecté
        String nomUtilisateur = SessionUtilisateur.getInstance().getNomComplet();
        Log log = new Log(nomUtilisateur, message, action, page);
        repo.enregistrer(log);
    }
}
