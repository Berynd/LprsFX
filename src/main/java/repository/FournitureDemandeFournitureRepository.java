package repository;

import model.Fourniture;
import model.FournitureDemandeFourniture;
import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournitureDemandeFournitureRepository {
    
    private Connection cnx;
    private FournitureRepository fournitureRepo;

    public FournitureDemandeFournitureRepository() {
        this.cnx = Database.getConnexion();
        this.fournitureRepo = new FournitureRepository();
    }

    /**
     * Ajouter une fourniture à une demande avec sa quantité
     */
    public boolean ajouterFournitureADemande(int refDemande, int refFourniture, int quantite) {
        String sql = "INSERT INTO fournituredemandefourniture (ref_demande_fourniture, ref_fourniture, quantite) VALUES (?, ?, ?)";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, refDemande);
            stmt.setInt(2, refFourniture);
            stmt.setInt(3, quantite);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Fourniture ajoutée à la demande avec succès !");
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la fourniture à la demande : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupérer toutes les fournitures d'une demande
     */
    public List<FournitureDemandeFourniture> getFournituresDemande(int idDemande) {
        List<FournitureDemandeFourniture> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fournituredemandefourniture WHERE ref_demande_fourniture = ?";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, idDemande);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int refFourniture = rs.getInt("ref_fourniture");
                int quantite = rs.getInt("quantite");
                
                // Récupérer l'objet Fourniture complet
                Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                
                FournitureDemandeFourniture fdf = new FournitureDemandeFourniture(
                    idDemande,
                    refFourniture,
                    quantite,
                    fourniture
                );
                resultats.add(fdf);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournitures de la demande : " + e.getMessage());
            e.printStackTrace();
        }
        return resultats;
    }

    /**
     * Récupérer toutes les demandes contenant une fourniture spécifique
     */
    public List<FournitureDemandeFourniture> getDemandesPourFourniture(int idFourniture) {
        List<FournitureDemandeFourniture> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fournituredemandefourniture WHERE ref_fourniture = ?";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, idFourniture);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int refDemande = rs.getInt("ref_demande_fourniture");
                int quantite = rs.getInt("quantite");
                
                Fourniture fourniture = fournitureRepo.getFournitureParId(idFourniture);
                
                FournitureDemandeFourniture fdf = new FournitureDemandeFourniture(
                    refDemande,
                    idFourniture,
                    quantite,
                    fourniture
                );
                resultats.add(fdf);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des demandes : " + e.getMessage());
            e.printStackTrace();
        }
        return resultats;
    }

    /**
     * Récupérer toutes les associations demande-fourniture
     */
    public List<FournitureDemandeFourniture> getToutesLesAssociations() {
        List<FournitureDemandeFourniture> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fournituredemandefourniture";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int refDemande = rs.getInt("ref_demande_fourniture");
                int refFourniture = rs.getInt("ref_fourniture");
                int quantite = rs.getInt("quantite");
                
                Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                
                FournitureDemandeFourniture fdf = new FournitureDemandeFourniture(
                    refDemande,
                    refFourniture,
                    quantite,
                    fourniture
                );
                resultats.add(fdf);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des associations : " + e.getMessage());
            e.printStackTrace();
        }
        return resultats;
    }

    /**
     * Mettre à jour la quantité d'une fourniture dans une demande
     */
    public boolean mettreAJourQuantite(int refDemande, int refFourniture, int nouvelleQuantite) {
        String sql = "UPDATE fournituredemandefourniture SET quantite = ? WHERE ref_demande_fourniture = ? AND ref_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, nouvelleQuantite);
            stmt.setInt(2, refDemande);
            stmt.setInt(3, refFourniture);
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Quantité mise à jour avec succès !");
                return true;
            } else {
                System.out.println("Aucune association trouvée.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la quantité : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprimer une fourniture d'une demande
     */
    public boolean supprimerFournitureDemande(int refDemande, int refFourniture) {
        String sql = "DELETE FROM fournituredemandefourniture WHERE ref_demande_fourniture = ? AND ref_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, refDemande);
            stmt.setInt(2, refFourniture);
            
            int rowsDeleted = stmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("Fourniture supprimée de la demande avec succès !");
                return true;
            } else {
                System.out.println("Aucune association trouvée.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprimer toutes les fournitures d'une demande (quand on supprime la demande)
     */
    public boolean supprimerToutesFournituresDemande(int refDemande) {
        String sql = "DELETE FROM fournituredemandefourniture WHERE ref_demande_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, refDemande);
            
            int rowsDeleted = stmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println(rowsDeleted + " fourniture(s) supprimée(s) de la demande !");
                return true;
            } else {
                System.out.println("Aucune fourniture trouvée pour cette demande.");
                return true; // Pas d'erreur si aucune fourniture
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des fournitures : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtenir la quantité demandée pour une fourniture spécifique dans une demande
     */
    public int getQuantite(int refDemande, int refFourniture) {
        String sql = "SELECT quantite FROM fournituredemandefourniture WHERE ref_demande_fourniture = ? AND ref_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, refDemande);
            stmt.setInt(2, refFourniture);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantite");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la quantité : " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Calculer la quantité totale demandée pour une fourniture (toutes demandes confondues)
     */
    public int getQuantiteTotaleDemandee(int idFourniture) {
        String sql = "SELECT SUM(quantite) as total FROM fournituredemandefourniture WHERE ref_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, idFourniture);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul de la quantité totale : " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
