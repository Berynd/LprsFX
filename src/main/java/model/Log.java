package model;

import java.time.LocalDateTime;

public class Log {

    private int idLog;
    private LocalDateTime dateHeure;
    private String nomUtilisateur;
    private String messageLog;
    private String action;
    private String page;

    public Log() {}

    public Log(String nomUtilisateur, String messageLog, String action, String page) {
        this.dateHeure = LocalDateTime.now();
        this.nomUtilisateur = nomUtilisateur;
        this.messageLog = messageLog;
        this.action = action;
        this.page = page;
    }

    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public String getMessageLog() { return messageLog; }
    public void setMessageLog(String messageLog) { this.messageLog = messageLog; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getPage() { return page; }
    public void setPage(String page) { this.page = page; }

    @Override
    public String toString() {
        return idLog + " - " + dateHeure + " : " + nomUtilisateur + " " + messageLog + " | " + action + " | " + page + " |";
    }
}
