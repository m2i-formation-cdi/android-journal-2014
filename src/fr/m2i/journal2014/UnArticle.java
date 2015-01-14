package fr.m2i.journal2014;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class UnArticle extends Activity {

 	private TextView textViewTitreArticle;
 
	private TextView textViewDateParutionArticle;
	private TextView textViewContributeurArticle;
	private TextView textViewChapoArticle;
	private TextView textViewResumeArticle;
	private TextView textViewTexteArticle;
	private TextView textViewNomRubrique;
	private TextView textViewCategorieArticle;
	private TextView textViewMotsClesArticle;

	public String indexArticle = "";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.un_article);
		
		Log.i("Journal2014", "Test");

		textViewTitreArticle = (TextView) findViewById(R.id.textViewTitreArticle);
		textViewDateParutionArticle = (TextView) findViewById(R.id.textViewDateParutionArticle);
		textViewContributeurArticle = (TextView) findViewById(R.id.textViewContributeurArticle);
		textViewChapoArticle = (TextView) findViewById(R.id.textViewChapoArticle);
		textViewResumeArticle = (TextView) findViewById(R.id.textViewResumeArticle);
		textViewTexteArticle = (TextView) findViewById(R.id.textViewTexteArticle);
		textViewNomRubrique = (TextView) findViewById(R.id.textViewNomRubrique);
		textViewCategorieArticle = (TextView) findViewById(R.id.textViewCategorieArticle);
		textViewMotsClesArticle = (TextView) findViewById(R.id.textViewMotsClesArticle);
		
		// récupération de l'identifiant de l'article depuis les autres activités.
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     //---------récupération de l'identifiant------------
			indexArticle = extras.getString("idArticle");
		}

		
		// affichageArticle();
		new TacheAsynchroneBD().execute("");
		
	}

	/*
	 * 
	 * AsyncTask<Params, Progress, Result>
	 */
	private class TacheAsynchroneBD extends
			AsyncTask<String, Integer, List<ResultSet>> {

		// Déclarations des variables connexions et résultats des requêtes
		//private String indexArticle;
		private PreparedStatement lpArticle;
		private ResultSet lrRubrique;
		private ResultSet lrContributeur;
		private ResultSet lrCategorie;
		private Connection lcCnx;
		private ResultSet lrArticleSelectionne;
		private ResultSet lrMotCle;
		private PreparedStatement lpsRubrique;
		private PreparedStatement lpsContributeur;
		private PreparedStatement lpsCategorie;
		private PreparedStatement lpsMotCle;
		// private String liId;

		// Déclaration de la variable liste pour contenir les résultats des requêtes
		private List<ResultSet> lsVueArticle;
		
		// Déclarations des variables StringBuilder pour l'affiche de l'article
		private StringBuilder lsbResultat;
		private StringBuilder lsbContributeurs;
		private StringBuilder lsbCategories;
		private StringBuilder lsbMotsCles;

		@Override
		// ----------------------------
		protected List<ResultSet> doInBackground(String... parametre) {

			// Instantiation du StringBuilder lsbResultat pour l'affichage d'éventuel erreur
			StringBuilder lsbResultat = new StringBuilder();
			// L'adresse IP du serveur
			String lsIP = "172.26.10.54";
			// Le port du serveur
			String lsPort = "3306";
			// Le nom de la base de données
			String lsBD = "journal2014";
			// Le login utilisateur pour la base de donées
			String lsUSER = "p";
			// Le mot de passe de l'utilisateur pour la base de donées
			String lsMDP = "b";

			
			// Connection à la base de données
			// préparation et exécution des requêtes
			// stockage des résultats dans la liste
			try {
				Class.forName("com.mysql.jdbc.Driver");
				lcCnx = DriverManager.getConnection("jdbc:mysql://" + lsIP
						+ ":" + lsPort + "/" + lsBD, lsUSER, lsMDP);


				/**
				 * Sélection de l'article
				 */
				// Requête préparée pour sélectionner les attributs à afficher
				lpArticle = lcCnx
						.prepareStatement("SELECT article.id_article, article.titre_article, article.chapeau_article, article.resume_article, "
								+ "article.texte_article, article.date_parution_article, contributeur.prenom_contributeur, contributeur.nom_contributeur, rubrique.rubrique, "
								+ "mot_cle.mot_cle FROM journal2014.rubrique rubrique "
								+ "CROSS JOIN (((journal2014.article_contributeur article_contributeur "
								+ "INNER JOIN journal2014.article article ON (article_contributeur.id_article = article.id_article)) "
								+ "INNER JOIN journal2014.contributeur contributeur ON (article_contributeur.id_contributeur = contributeur.id_contributeur)) "
								+ "INNER JOIN journal2014.article_mot_cle article_mot_cle ON (article_mot_cle.id_article = article.id_article)) "
								+ "INNER JOIN journal2014.mot_cle mot_cle ON (article_mot_cle.id_mot_cle = mot_cle.id_mot_cle) "
								+ "WHERE article.id_article = " + indexArticle);

				// exécution de la requête
				lrArticleSelectionne = lpArticle.executeQuery();

				lsVueArticle = new ArrayList<ResultSet>();
				// Ajout du résultat de l'exécution de la requête dans la liste
				lsVueArticle.add(lrArticleSelectionne);

				
				/**
				 * Sélection des rubriques concernant l'article
				 */
				// Requête préparée pour sélectionner les attributs à afficher
				lpsRubrique = lcCnx
						.prepareStatement("SELECT rubrique.rubrique"
								+ " FROM journal2014.article article, journal2014.rubrique rubrique"
								+ " WHERE article.id_rubrique = rubrique.id_rubrique AND article.id_article = "
								+ indexArticle);

				// exécution de la requête
				lrRubrique = lpsRubrique.executeQuery();
				// Ajout du résultat de l'exécution de la requête dans la liste
				lsVueArticle.add(lrRubrique);
				

				/**
				 * Sélection des contributeurs de l'article
				 */
				// Requête préparée pour sélectionner les attributs à afficher
				lpsContributeur = lcCnx
						.prepareStatement("SELECT contributeur.prenom_contributeur, contributeur.nom_contributeur"
								+ " FROM (journal2014.article_contributeur article_contributeur"
								+ " INNER JOIN journal2014.contributeur contributeur"
								+ " ON (article_contributeur.id_contributeur = contributeur.id_contributeur))"
								+ " INNER JOIN journal2014.article article"
								+ " ON (article_contributeur.id_article = article.id_article)"
								+ " WHERE article_contributeur.id_article = article.id_article"
								+ " AND article_contributeur.id_contributeur = contributeur.id_contributeur"
								+ " AND article.id_article = " + indexArticle);

				// exécution de la requête
				lrContributeur = lpsContributeur.executeQuery();
				// Ajout du résultat de l'exécution de la requête dans la liste
				lsVueArticle.add(lrContributeur);

				
				/**
				 * Sélection des catégories de l'article
				 */
				// Requête préparée pour sélectionner les attributs à afficher
				lpsCategorie = lcCnx
						.prepareStatement("SELECT categorie.categorie"
								+ " FROM journal2014.article article, journal2014.categorie categorie"
								+ " WHERE     article.id_categorie = categorie.id_categorie "
								+ " AND article.id_article = " + indexArticle);

				// exécution de la requête
				lrCategorie = lpsCategorie.executeQuery();
				// Ajout du résultat de l'exécution de la requête dans la liste
				lsVueArticle.add(lrCategorie);

				
				/**
				 * Sélection des mots clés de l'article
				 */
				// Requête préparée pour sélectionner les attributs à afficher
				lpsMotCle = lcCnx
						.prepareStatement("SELECT mot_cle.mot_cle"
								+ " FROM (journal2014.article_mot_cle article_mot_cle"
								+ " INNER JOIN journal2014.mot_cle mot_cle"
								+ " ON (article_mot_cle.id_mot_cle = mot_cle.id_mot_cle))"
								+ " INNER JOIN journal2014.article article"
								+ " ON (article_mot_cle.id_article = article.id_article)"
								+ " WHERE     article.id_article = article_mot_cle.id_article"
								+ " AND article_mot_cle.id_mot_cle = mot_cle.id_mot_cle"
								+ " AND article.id_article =  " + indexArticle);

				// exécution de la requête
				lrMotCle = lpsMotCle.executeQuery();
				// Ajout du résultat de l'exécution de la requête dans la liste
				lsVueArticle.add(lrMotCle);


			} catch (ClassNotFoundException e) { // Message d'erreur si la connection à la base de données ne fonctionne pas à cause du pilote
				lsbResultat.append("Pilote ? ");
				lsbResultat.append(e.getMessage());
			} catch (SQLException e) {// Message d'erreur sur les requêtes SQL
				lsbResultat.append("SQL ? ");
				lsbResultat.append(e.getMessage());
			}

			// Renvoie la valeur a onPostExecute
			return lsVueArticle; // renvoi de la liste contenant les résultats des requêtes

		} // / doInBackground

		@Override
		// -------------------------
		protected void onPostExecute(List<ResultSet> arsResultat) {
			// Synchronisation avec le thread de l'UI
			// Affiche le resultat final

			try { // boucle sur les attributs de l'article pour l'affectation aux champs d'affichage
				while (lrArticleSelectionne.next()) {
					// Affectation des attributs pour l'affichage
					// liId = lrArticleSelectionne.getString(1);
					// Affichage du titre de l'article (rang 2 dans l'ordre de la requête préparée)
					textViewTitreArticle.setText(lrArticleSelectionne
							.getString(2));
					textViewChapoArticle.setText(lrArticleSelectionne
							.getString(3));
					textViewResumeArticle.setText(lrArticleSelectionne
							.getString(4));
					textViewTexteArticle.setText(lrArticleSelectionne
							.getString(5));
					// Affectation au champ textViewDateParutionArticle pour l'affichage de la date de parution de l'article 
					// (rang 6 dans l'ordre de la requête préparée)
					textViewDateParutionArticle.setText(lrArticleSelectionne
							.getString(6));

					// Fermeture des connections à la base de données après la requête de sélection des attributs de l'article à afficher
					lrArticleSelectionne.close();
					lpArticle.close();

				}
			} catch (SQLException e) { // Message d'erreur si un problème apparait lors du bouclage sur les attributs de l'article
				e.printStackTrace();
			}

			lsbResultat = new StringBuilder();
			try {// boucle sur les rubriques de l'article pour l'affectation aux champs d'affichage
				while (lrRubrique.next()) {
					// récupération du nom de la rubrique (rang 1 dans l'ordre de la requête préparée)
					lsbResultat.append(lrRubrique.getString(1));
					// Affichage du séparateur entre les rubrique
					lsbResultat.append(" - ");
					// Suppression du dernier séparateur concaténé à la dernière rubrique affichée sur la ligne
					// (les trois derniers caractères)
					lsbResultat = lsbResultat.delete(lsbResultat.length() - 3,
							lsbResultat.length());
					// Affectation au champ textViewNomRubrique pour l'affichage des rubriques (type: A - B - C ...) 
					textViewNomRubrique.setText(lsbResultat);

					// Fermeture des connections à la base de données après la requête de sélection des rubriques à afficher
					lrRubrique.close();
					lpsRubrique.close();

				}
			} catch (SQLException e) { // Message d'erreur si un problème apparait lors du bouclage sur les rubriques de l'article
				e.printStackTrace();
			}

			lsbContributeurs = new StringBuilder();
			try { // boucle sur les contributeurs de l'article pour l'affectation aux champs d'affichage
				while (lrContributeur.next()) {
					// récupération du prénom du contributeur (rang 1 dans l'ordre de la requête préparée)
					lsbContributeurs.append(lrContributeur.getString(1)); 
					// Séparateur espace entre prénom et nom
					lsbContributeurs.append(" ");
					// récupération du nom du contributeur (rang 1 dans l'ordre de la requête préparée)
					lsbContributeurs.append(lrContributeur.getString(2));
					// Séparateur ET entre les contributeurs
					lsbContributeurs.append(" ET ");

					// Fermeture des connections à la base de données après la requête de sélection des contributeurs à afficher
					lrContributeur.close();
					lpsContributeur.close();

				}
			} catch (SQLException e) { // Message d'erreur si un problème apparait lors du bouclage sur les contributeurs de l'article
				e.printStackTrace();
			}

			// Suppression du dernier séparateur concaténé au dernière contributeur affichée sur la ligne
			// (les quatres derniers caractères)
			lsbContributeurs = lsbContributeurs.delete(
					lsbContributeurs.length() - 4, lsbContributeurs.length());

			// Affectation au champ textViewContributeurArticle pour l'affichage des contributeurs (type: A ET B ET C ...) 
			textViewContributeurArticle.setText(lsbContributeurs);

			lsbCategories = new StringBuilder();
			try { // boucle sur les categories de l'article pour l'affectation aux champs d'affichage
				while (lrCategorie.next()) {
					// récupération du nom de la catégorie (rang 1 dans l'ordre de la requête préparée)
					lsbCategories.append(lrCategorie.getString(1));
					// Séparateur ET entre les catégories
					lsbCategories.append(" ET ");

					// Fermeture des connections à la base de données après la requête de sélection des categories à afficher
					lrCategorie.close();
					lpsCategorie.close();

				}
			} catch (SQLException e) {// Message d'erreur si un problème apparait lors du bouclage sur les categories de l'article
				e.printStackTrace();
			}

			// Suppression du dernier séparateur concaténé à la dernière categorie affichée sur la ligne
			// (les quatres derniers caractères)
			lsbCategories = lsbCategories.delete(lsbCategories.length() - 4,
					lsbCategories.length());

			// Affectation au champ textViewCategorieArticle pour l'affichage des catégories (type: A ET B ET C ..) 
			textViewCategorieArticle.setText(lsbCategories);

			lsbMotsCles = new StringBuilder();
			
			try { // boucle sur les mots clés de l'article pour l'affectation aux champs d'affichage
				while (lrMotCle.next()) {
					lsbMotsCles.append(lrMotCle.getString(1));
					lsbMotsCles.append(" - ");

					// Fermeture des connections à la base de données après la requête de sélection des mots clés à afficher
					lrMotCle.close();
					lpsMotCle.close();

				}
			} catch (SQLException e) {// Message d'erreur si un problème apparait lors du bouclage sur les mots clés de l'article
				e.printStackTrace();
			}

			// Suppression du dernier séparateur concaténé au dernière mot clé affichée sur la ligne
			// (les trois derniers caractères)
			lsbMotsCles = lsbMotsCles.delete(lsbMotsCles.length() - 3,
					lsbMotsCles.length());

			// Affectation au champ textViewMotsClesArticle pour l'affichage des contributeurs (type: A - B - C ...)
			textViewMotsClesArticle.setText(lsbMotsCles);

		} // / onPostExecute

	}// / TacheAsynchroneBD

}// /UnArticle
