package fr.m2i.journal2014.models;

import java.util.Calendar;

public class PojoJournaliste {

	private int idContributeur;
	private String pseudoContributeur;
	private String emailContributeur;
	private String mdpContributeur;
	private char civiliteContributeur;
	private String nomContributeur;
	private String prenomContributeur;
	private String photoContributeur;
	private int offresPartenaires;
	private Calendar dateInscriptionContributeur;
	private int idStatut;
	private String cvContributeur;
	
	/****************************************
	 * Constructeur sans arguments
	 ****************************************/
	public PojoJournaliste() {
		super();
	}
	
	/****************************************
	 * GETTERS AND SETTERS
	 ****************************************/

	public int getIdContributeur() {
		return idContributeur;
	}

	public void setIdContributeur(int id_contributeur) {
		this.idContributeur = id_contributeur;
	}

	public String getPseudoContributeur() {
		return pseudoContributeur;
	}

	public void setPseudoContributeur(String pseudo_contributeur) {
		this.pseudoContributeur = pseudo_contributeur;
	}

	public String getEmailContributeur() {
		return emailContributeur;
	}

	public void setEmailContributeur(String email_contributeur) {
		this.emailContributeur = email_contributeur;
	}

	public String getMdpContributeur() {
		return mdpContributeur;
	}

	public void setMdpContributeur(String mdp_contributeur) {
		this.mdpContributeur = mdp_contributeur;
	}

	public char getCiviliteContributeur() {
		return civiliteContributeur;
	}

	public void setCiviliteContributeur(char civilite_contributeur) {
		this.civiliteContributeur = civilite_contributeur;
	}

	public String getNomContributeur() {
		return nomContributeur;
	}

	public void setNomContributeur(String nom_contributeur) {
		this.nomContributeur = nom_contributeur;
	}

	public String getPrenomContributeur() {
		return prenomContributeur;
	}

	public void setPrenomContributeur(String prenom_contributeur) {
		this.prenomContributeur = prenom_contributeur;
	}

	public String getPhotoContributeur() {
		return photoContributeur;
	}

	public void setPhotoContributeur(String photo_contributeur) {
		this.photoContributeur = photo_contributeur;
	}

	public int getOffresPartenaires() {
		return offresPartenaires;
	}

	public void setOffresPartenaires(int offres_partenaires) {
		this.offresPartenaires = offres_partenaires;
	}

	public Calendar getDate_inscription_contributeur() {
		return dateInscriptionContributeur;
	}

	public void setDate_inscription_contributeur(
			Calendar date_inscription_contributeur) {
		this.dateInscriptionContributeur = date_inscription_contributeur;
	}

	public int getId_statut() {
		return idStatut;
	}

	public void setId_statut(int id_statut) {
		this.idStatut = id_statut;
	}

	public String getCv_contributeur() {
		return cvContributeur;
	}

	public void setCv_contributeur(String cv_contributeur) {
		this.cvContributeur = cv_contributeur;
	}
	
	
	
	
	
	
}
