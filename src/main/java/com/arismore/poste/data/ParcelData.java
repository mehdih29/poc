package com.arismore.poste.data;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.gson.annotations.Expose;

public class ParcelData implements Cloneable{

	private String isie;
	private Boolean image;
	private String format;
	private String priorite;
	private String etat;
	private String plateformTri;
	private String envoloppe_expire;
	private String cle_externe;
	private String updated;
	private String published;
	private ArrayList<Adresse> adresses;
	private String derivationAdresse;
	private Traitement traitement;

	// private ArrayList<TraitementManuel> traitementsManuel;


	public ParcelData(Document document, XPath xpath )//, Traitement traitement)
			throws XPathExpressionException {

		// this.traitement = traitement;

		this.isie = (String) xpath.compile(
				"/a:feed/a:entry/a:title/:div/:span[@class='id']/text()").evaluate(
				document, XPathConstants.STRING);
		
		this.image = (Boolean) xpath.compile(
				"/a:feed/a:entry/a:id/image?format=jpeg/text()").evaluate(document,
				XPathConstants.BOOLEAN);

		this.format = (String) xpath.compile(
				"/a:feed/a:entry/a:title/:div/:span[@class='format']/text()")
				.evaluate(document, XPathConstants.STRING);

		this.priorite = (String) xpath.compile(
				"/a:feed/a:entry/a:title/:div/:span[@class='priorite']/text()")
				.evaluate(document, XPathConstants.STRING);
		
		this.etat = (String) xpath.compile(
				"/a:feed/a:entry/a:title/:div/:span[@class='etat']/text()").evaluate(
				document, XPathConstants.STRING);
		
		this.plateformTri = (String) xpath.compile(
				"/a:feed/a:entry/a:author/a:name/text()").evaluate(document,
				XPathConstants.STRING);
		
		this.envoloppe_expire = (String) xpath.compile(
				"/a:feed/a:entry/a:title/:div/:div[@class='expires']/text()")
				.evaluate(document, XPathConstants.STRING);
		
		this.cle_externe = (String) xpath
				.compile(
						"/a:feed/a:entry/a:title/div/div[@class='client']/span[@class='cleExterne']/text()")
				.evaluate(document, XPathConstants.STRING);
		
		this.updated = (String) xpath.compile("/a:feed/a:entry/a:updated/text()")
				.evaluate(document, XPathConstants.STRING);
		
		this.published = (String) xpath.compile("/a:feed/a:entry/a:published/text()")
				.evaluate(document, XPathConstants.STRING);
		
		this.derivationAdresse = (String) xpath.compile(
				"/a:feed/a:entry/a:content/:div/:span[@class='derivation']/text()")
				.evaluate(document, XPathConstants.STRING);
		
		/*
		 * NodeList traitement = (NodeList) xpath.compile(
		 * "/a:entry/a:content/:div/:ul[@class='traitement']/:li")
		 * .evaluate(document, XPathConstants.NODESET); if
		 * (traitement.getLength() > 0) { this.traitements = new
		 * ArrayList<Traitement>(); System.out.println(traitement.getLength());
		 * for (int i = 1; i <= traitement.getLength(); i++) {
		 * this.traitements.add(new Traitement(i, document, xpath)); } }
		 * NodeList traitementManuel = (NodeList) xpath.compile(
		 * "/a:entry/a:content/:div/:ul[@class='traitementManuel']/:li"
		 * ).evaluate( document, XPathConstants.NODESET); if
		 * (traitementManuel.getLength() > 0) { this.traitementsManuel = new
		 * ArrayList<TraitementManuel>(); for (int i = 1; i <=
		 * traitementManuel.getLength(); i++) { this.traitementsManuel.add(new
		 * TraitementManuel(i, document, xpath)); } }
		 */

		NodeList adresse = (NodeList) xpath.compile(
				"/a:feed/a:entry/a:content/:div/:ul[@class='adresses']/:li").evaluate(
				document, XPathConstants.NODESET);
		if (adresse.getLength() > 0) {
			this.adresses = new ArrayList<Adresse>();
			System.out.println(adresse.getLength());
			for (int i = 1; i <= adresse.getLength(); i++) {
				this.adresses.add(new Adresse(i, document, xpath));
			}
		}
	}
	
	public ArrayList<Adresse> getAdresses() {
		return adresses;
	}

	public void setAdresses(ArrayList<Adresse> adresses) {
		this.adresses = adresses;
	}

	public Traitement getTraitement() {
		return traitement;
	}

	public void setTraitement(Traitement traitement) {
		this.traitement = traitement;
	}
	
	public Object clone() throws CloneNotSupportedException {    
	    // On récupère l'instance à renvoyer par l'appel de la 
	    // méthode super.clone() (ici : Personne.clone())
		ParcelData clone = (ParcelData) super.clone();
	    
	    // On clone l'attribut de type Jouet qui n'est pas immuable.
	    // enfant.jouetPrefere = (Jouet) jouetPrefere.clone();
	    
	    // on renvoie le clone
	    return clone;
  	}

	@Override
	public String toString() {
		return "";
	}

}
