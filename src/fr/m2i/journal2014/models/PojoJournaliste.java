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
	private String cvContributeur = "";
	
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

	public void setIdContributeur(int idContributeur) {
		this.idContributeur = idContributeur;
	}

	public String getPseudoContributeur() {
		return pseudoContributeur;
	}

	public void setPseudoContributeur(String pseudoContributeur) {
		this.pseudoContributeur = pseudoContributeur;
	}

	public String getEmailContributeur() {
		return emailContributeur;
	}

	public void setEmailContributeur(String emailContributeur) {
		this.emailContributeur = emailContributeur;
	}

	public String getMdpContributeur() {
		return mdpContributeur;
	}

	public void setMdpContributeur(String mdpContributeur) {
		this.mdpContributeur = mdpContributeur;
	}

	public char getCiviliteContributeur() {
		return civiliteContributeur;
	}

	public void setCiviliteContributeur(char civiliteContributeur) {
		this.civiliteContributeur = civiliteContributeur;
	}

	public String getNomContributeur() {
		return nomContributeur;
	}

	public void setNomContributeur(String nomContributeur) {
		this.nomContributeur = nomContributeur;
	}

	public String getPrenomContributeur() {
		return prenomContributeur;
	}

	public void setPrenomContributeur(String prenomContributeur) {
		this.prenomContributeur = prenomContributeur;
	}

	public String getPhotoContributeur() {
		return photoContributeur;
	}

	public void setPhotoContributeur(String photoContributeur) {
		this.photoContributeur = photoContributeur;
	}

	public int getOffresPartenaires() {
		return offresPartenaires;
	}

	public void setOffresPartenaires(int offresPartenaires) {
		this.offresPartenaires = offresPartenaires;
	}

	public Calendar getDateInscriptionContributeur() {
		return dateInscriptionContributeur;
	}

	public void setDateInscriptionContributeur(Calendar dateInscriptionContributeur) {
		this.dateInscriptionContributeur = dateInscriptionContributeur;
	}

	public int getIdStatut() {
		return idStatut;
	}

	public void setIdStatut(int idStatut) {
		this.idStatut = idStatut;
	}

	public String getCvContributeur() {
		return cvContributeur;
	}

	public void setCvContributeur(String cvContributeur) {
		this.cvContributeur = cvContributeur;
	}
	
	

	
	
	
	
	
	
	
}
