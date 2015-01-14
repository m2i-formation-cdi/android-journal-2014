package fr.m2i.journal2014.models;

public class PojoCategorie {
	private int idCategorie;
	private String categorie;
	
	public PojoCategorie() {
		super();
	}

	public PojoCategorie(int idCategorie, String categorie) {
		super();
		this.idCategorie = idCategorie;
		this.categorie = categorie;
	}

	public int getIdCategorie() {
		return idCategorie;
	}

	public void setIdCategorie(int idCategorie) {
		this.idCategorie = idCategorie;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	@Override
	public String toString() {
		return "PojoCategorie [idCategorie=" + idCategorie + ", categorie="
				+ categorie + "]";
	}
	
	
}
