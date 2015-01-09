package fr.m2i.journal2014.models;

import java.util.Calendar;

public class PojoJournaliste {

	private int id_contributeur;
	private String pseudo_contributeur;
	private String email_contributeur;
	private String mdp_contributeur;
	private char civilite_contributeur;
	private String nom_contributeur;
	private String prenom_contributeur;
	private String photo_contributeur;
	private int offres_partenaires;
	private Calendar date_inscription_contributeur;
	private int id_statut;
	private String cv_contributeur;
	
	/****************************************
	 * Constructeur sans arguments
	 ****************************************/
	public PojoJournaliste() {
		super();
	}
	
	/****************************************
	 * GETTERS AND SETTERS
	 ****************************************/

	public int getId_contributeur() {
		return id_contributeur;
	}

	public void setId_contributeur(int id_contributeur) {
		this.id_contributeur = id_contributeur;
	}

	public String getPseudo_contributeur() {
		return pseudo_contributeur;
	}

	public void setPseudo_contributeur(String pseudo_contributeur) {
		this.pseudo_contributeur = pseudo_contributeur;
	}

	public String getEmail_contributeur() {
		return email_contributeur;
	}

	public void setEmail_contributeur(String email_contributeur) {
		this.email_contributeur = email_contributeur;
	}

	public String getMdp_contributeur() {
		return mdp_contributeur;
	}

	public void setMdp_contributeur(String mdp_contributeur) {
		this.mdp_contributeur = mdp_contributeur;
	}

	public char getCivilite_contributeur() {
		return civilite_contributeur;
	}

	public void setCivilite_contributeur(char civilite_contributeur) {
		this.civilite_contributeur = civilite_contributeur;
	}

	public String getNom_contributeur() {
		return nom_contributeur;
	}

	public void setNom_contributeur(String nom_contributeur) {
		this.nom_contributeur = nom_contributeur;
	}

	public String getPrenom_contributeur() {
		return prenom_contributeur;
	}

	public void setPrenom_contributeur(String prenom_contributeur) {
		this.prenom_contributeur = prenom_contributeur;
	}

	public String getPhoto_contributeur() {
		return photo_contributeur;
	}

	public void setPhoto_contributeur(String photo_contributeur) {
		this.photo_contributeur = photo_contributeur;
	}

	public int getOffres_partenaires() {
		return offres_partenaires;
	}

	public void setOffres_partenaires(int offres_partenaires) {
		this.offres_partenaires = offres_partenaires;
	}

	public Calendar getDate_inscription_contributeur() {
		return date_inscription_contributeur;
	}

	public void setDate_inscription_contributeur(
			Calendar date_inscription_contributeur) {
		this.date_inscription_contributeur = date_inscription_contributeur;
	}

	public int getId_statut() {
		return id_statut;
	}

	public void setId_statut(int id_statut) {
		this.id_statut = id_statut;
	}

	public String getCv_contributeur() {
		return cv_contributeur;
	}

	public void setCv_contributeur(String cv_contributeur) {
		this.cv_contributeur = cv_contributeur;
	}
	
	
	
	
	
	
}
