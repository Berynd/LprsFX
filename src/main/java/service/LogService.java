package service;

import model.Log;
import repository.LogRepository;
import session.SessionUtilisateur;

public class LogService {

    private static final LogRepository repo = new LogRepository();

    public static void log(String message, String action, String page) {
        String nomUtilisateur = SessionUtilisateur.getInstance().getNomComplet();
        Log log = new Log(nomUtilisateur, message, action, page);
        repo.enregistrer(log);
    }
}
