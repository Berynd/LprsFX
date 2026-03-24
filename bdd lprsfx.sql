-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Feb 10, 2026 at 07:38 AM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lprsfx`
--

-- --------------------------------------------------------

--
-- Table structure for table `commandefourniture`
--

DROP TABLE IF EXISTS `commandefourniture`;
CREATE TABLE IF NOT EXISTS `commandefourniture` (
  `ref_commande` int NOT NULL,
  `ref_fourniture` int NOT NULL,
  `quantite` int DEFAULT NULL,
  PRIMARY KEY (`ref_commande`,`ref_fourniture`),
  KEY `ref_fourniture` (`ref_fourniture`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `commande_fourniture`
--

DROP TABLE IF EXISTS `commande_fourniture`;
CREATE TABLE IF NOT EXISTS `commande_fourniture` (
  `id_commande_fourniture` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `statut` varchar(50) COLLATE latin1_bin DEFAULT NULL,
  `justification_refus` text COLLATE latin1_bin,
  `ref_gestionnaire` int DEFAULT NULL,
  `ref_fournisseur` int DEFAULT NULL,
  PRIMARY KEY (`id_commande_fourniture`),
  KEY `ref_gestionnaire` (`ref_gestionnaire`),
  KEY `ref_fournisseur` (`ref_fournisseur`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `demande_fourniture`
--

DROP TABLE IF EXISTS `demande_fourniture`;
CREATE TABLE IF NOT EXISTS `demande_fourniture` (
  `id_demande_fourniture` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `statut` varchar(50) COLLATE latin1_bin DEFAULT NULL,
  `raison` text COLLATE latin1_bin,
  `justification_refus` text COLLATE latin1_bin,
  `ref_professeur` int DEFAULT NULL,
  PRIMARY KEY (`id_demande_fourniture`),
  KEY `ref_professeur` (`ref_professeur`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `dossier_inscription`
--

DROP TABLE IF EXISTS `dossier_inscription`;
CREATE TABLE IF NOT EXISTS `dossier_inscription` (
  `id_dossier_inscription` int NOT NULL AUTO_INCREMENT,
  `date_creation` date DEFAULT NULL,
  `motivation` text COLLATE latin1_bin,
  `statut` varchar(50) COLLATE latin1_bin DEFAULT NULL,
  `ref_filiere` int DEFAULT NULL,
  `ref_etudiant` int DEFAULT NULL,
  `ref_secretaire` int DEFAULT NULL,
  PRIMARY KEY (`id_dossier_inscription`),
  KEY `ref_filiere` (`ref_filiere`),
  KEY `ref_etudiant` (`ref_etudiant`),
  KEY `ref_secretaire` (`ref_secretaire`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `etudiant`
--

DROP TABLE IF EXISTS `etudiant`;
CREATE TABLE IF NOT EXISTS `etudiant` (
  `id_etudiant` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `prenom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `email` varchar(150) COLLATE latin1_bin DEFAULT NULL,
  `telephone` varchar(20) COLLATE latin1_bin DEFAULT NULL,
  `adresse` varchar(255) COLLATE latin1_bin DEFAULT NULL,
  `dernier_diplome` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`id_etudiant`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `filiere`
--

DROP TABLE IF EXISTS `filiere`;
CREATE TABLE IF NOT EXISTS `filiere` (
  `id_filiere` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`id_filiere`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `fournisseur`
--

DROP TABLE IF EXISTS `fournisseur`;
CREATE TABLE IF NOT EXISTS `fournisseur` (
  `id_fournisseur` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `contact` varchar(150) COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`id_fournisseur`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `fourniture`
--

DROP TABLE IF EXISTS `fourniture`;
CREATE TABLE IF NOT EXISTS `fourniture` (
  `id_fourniture` int NOT NULL AUTO_INCREMENT,
  `libelle` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `description` text COLLATE latin1_bin,
  `stock_actuel` int DEFAULT '0',
  PRIMARY KEY (`id_fourniture`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `fournituredemandefourniture`
--

DROP TABLE IF EXISTS `fournituredemandefourniture`;
CREATE TABLE IF NOT EXISTS `fournituredemandefourniture` (
  `ref_demande_fourniture` int NOT NULL,
  `ref_fourniture` int NOT NULL,
  `quantite` int DEFAULT NULL,
  PRIMARY KEY (`ref_demande_fourniture`,`ref_fourniture`),
  KEY `ref_fourniture` (`ref_fourniture`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `fourniturefournisseur`
--

DROP TABLE IF EXISTS `fourniturefournisseur`;
CREATE TABLE IF NOT EXISTS `fourniturefournisseur` (
  `ref_fourniture` int NOT NULL,
  `ref_fournisseur` int NOT NULL,
  `prix` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`ref_fourniture`,`ref_fournisseur`),
  KEY `ref_fournisseur` (`ref_fournisseur`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `rdv`
--

DROP TABLE IF EXISTS `rdv`;
CREATE TABLE IF NOT EXISTS `rdv` (
  `id_rdv` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `demi_journee` varchar(20) COLLATE latin1_bin DEFAULT NULL,
  `ref_etudiant` int DEFAULT NULL,
  `ref_professeur` int DEFAULT NULL,
  `ref_salle` int DEFAULT NULL,
  PRIMARY KEY (`id_rdv`),
  KEY `ref_etudiant` (`ref_etudiant`),
  KEY `ref_professeur` (`ref_professeur`),
  KEY `ref_salle` (`ref_salle`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `salle`
--

DROP TABLE IF EXISTS `salle`;
CREATE TABLE IF NOT EXISTS `salle` (
  `id_salle` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`id_salle`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id_utilisateur` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `prenom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `email` varchar(150) COLLATE latin1_bin DEFAULT NULL,
  `mdp` varchar(255) COLLATE latin1_bin DEFAULT NULL,
  `role` varchar(50) COLLATE latin1_bin DEFAULT NULL,
  `date_derniere_connexion` datetime DEFAULT NULL,
  `ref_filiere` int DEFAULT NULL,
  PRIMARY KEY (`id_utilisateur`),
  UNIQUE KEY `email` (`email`),
  KEY `ref_filiere` (`ref_filiere`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `utilisateur`
--

INSERT INTO `utilisateur` (`id_utilisateur`, `nom`, `prenom`, `email`, `mdp`, `role`, `date_derniere_connexion`, `ref_filiere`) VALUES
(3, 'Passard', 'Ethan', 'ethanpassard@gmail.com', '$2a$10$ywNNo9DCR1z8QxIL.lQv5e5DNkgEKsuSOPDKphhhV5CjGImx.jNj6', 'Admin', NULL, NULL),
(4, 'a', 'a', 'a', '$2a$10$fivERpooWHkloFCdydFs4euvlKdo9l3sIX8X2hh/OKJ4rSSkJZqxO', 'Admin', NULL, NULL);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
