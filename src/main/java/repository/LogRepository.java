package repository;

import model.Log;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogRepository extends BaseRepository {

    public void enregistrer(Log log) {
        String sql = "INSERT INTO log (date_log, nom_utilisateur, message, action, page) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getCnx().prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(log.getDateHeure()));
            ps.setString(2, log.getNomUtilisateur());
            ps.setString(3, log.getMessageLog());
            ps.setString(4, log.getAction());
            ps.setString(5, log.getPage());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur enregistrement log : " + e.getMessage());
        }
    }

    public List<Log> getTousLesLogs() {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM log ORDER BY date_log DESC";
        try (PreparedStatement ps = getCnx().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Log log = new Log();
                log.setIdLog(rs.getInt("id_log"));
                log.setDateHeure(rs.getTimestamp("date_log").toLocalDateTime());
                log.setNomUtilisateur(rs.getString("nom_utilisateur"));
                log.setMessageLog(rs.getString("message"));
                log.setAction(rs.getString("action"));
                log.setPage(rs.getString("page"));
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lecture logs : " + e.getMessage());
        }
        return logs;
    }
}
