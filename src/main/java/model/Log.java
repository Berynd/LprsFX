package model;

import eu.hansolo.toolbox.time.DateTimes;

public class Log {
    private int idLog;
    private DateTimes dateLog;
    private int refUtilisateur;
    private String nomUtilisateur;
    private String message;
    private String action;
    private String page;

    public Log(int idLog, DateTimes dateLog, int refUtilisateur, String nomUtilisateur, String message, String action, String page) {
        this.idLog = idLog;
        this.dateLog = dateLog;
        this.refUtilisateur = refUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.message = message;
        this.action = action;
        this.page = page;
    }

    public Log(String page, String action, String message, String nomUtilisateur, int refUtilisateur, DateTimes dateLog) {
        this.page = page;
        this.action = action;
        this.message = message;
        this.nomUtilisateur = nomUtilisateur;
        this.refUtilisateur = refUtilisateur;
        this.dateLog = dateLog;
    }

    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public DateTimes getDateLog() {
        return dateLog;
    }

    public void setDateLog(DateTimes dateLog) {
        this.dateLog = dateLog;
    }

    public int getRefUtilisateur() {
        return refUtilisateur;
    }

    public void setRefUtilisateur(int refUtilisateur) {
        this.refUtilisateur = refUtilisateur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "Log{" +
                "idLog=" + idLog +
                ", dateLog=" + dateLog +
                ", refUtilisateur=" + refUtilisateur +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", message='" + message + '\'' +
                ", action='" + action + '\'' +
                ", page='" + page + '\'' +
                '}';
    }
}
