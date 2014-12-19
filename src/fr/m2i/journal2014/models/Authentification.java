package fr.m2i.journal2014.models;

/**
 * Cette classe est chargée d'authentifier un utilisateur 
 * en fonction de son identifiant et de son mot de passe
 * 
 * @author seb
 *
 */
public class Authentification {

	private boolean isValide;
	private String identifiant;
	private String motDePasse;

	public Authentification() {
		super();
	}

	public Authentification(String identifiant, String motDePasse) {
		super();
		this.identifiant = identifiant;
		this.motDePasse = motDePasse;
		
	}
	
	public boolean valider(){
		this.isValide = this.test();
		return this.isValide;
	}
	
	public boolean valider(String identifiant, String motDePasse){
		this.identifiant = identifiant;
		this.motDePasse = motDePasse;
		this.isValide = this.test();
		return this.isValide;
	}
	
	private boolean test(){
		boolean isOk = true;
		isOk = this.identifiant.equals("toto") && this.motDePasse.equals("pass");
		return isOk;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
}
