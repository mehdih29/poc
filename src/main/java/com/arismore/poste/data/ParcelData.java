package com.arismore.poste.data;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.gson.annotations.Expose;

public class ParcelData {

	@Expose
	private String isie;
	@Expose
	private Boolean image;
	@Expose
	private String format;
	@Expose
	private String priorite;
	@Expose
	private String etat;
	@Expose
	private String plateformTri;
	@Expose
	private String envoloppe_expire;
	@Expose
	private String cle_externe;
	@Expose
	private String updated;
	@Expose
	private String published;
	@Expose
	private ArrayList<Adresse> adresses;
	@Expose
	private String derivationAdresse;
	private Traitement traitement;
	//private ArrayList<TraitementManuel> traitementsManuel;
	

	public ParcelData(Document document, XPath xpath , Traitement traitement)
			throws XPathExpressionException {
		
		this.traitement = traitement;
		
		this.isie = (String) xpath.compile(
				"/a:entry/a:title/:div/:span[@class='id']/text()").evaluate(
				document, XPathConstants.STRING);
		System.out.println(isie);

		this.image = (Boolean) xpath.compile(
				"/a:entry/a:id/image?format=jpeg/text()").evaluate(document,
				XPathConstants.BOOLEAN);
		System.out.println(image);

		this.format = (String) xpath.compile(
				"/a:entry/a:title/:div/:span[@class='format']/text()")
				.evaluate(document, XPathConstants.STRING);
		System.out.println(format);

		this.priorite = (String) xpath.compile(
				"/a:entry/a:title/:div/:span[@class='priorite']/text()")
				.evaluate(document, XPathConstants.STRING);
		System.out.println(priorite);

		this.etat = (String) xpath.compile(
				"/a:entry/a:title/:div/:span[@class='etat']/text()").evaluate(
				document, XPathConstants.STRING);
		System.out.println(etat);

		this.plateformTri = (String) xpath.compile(
				"/a:entry/a:author/a:name/text()").evaluate(document,
				XPathConstants.STRING);
		System.out.println(plateformTri);

		this.envoloppe_expire = (String) xpath.compile(
				"/a:entry/a:title/:div/:div[@class='expires']/text()")
				.evaluate(document, XPathConstants.STRING);
		System.out.println(envoloppe_expire);

		this.cle_externe = (String) xpath
				.compile(
						"/a:entry/a:title/div/div[@class='client']/span[@class='cleExterne']/text()")
				.evaluate(document, XPathConstants.STRING);
		System.out.println(cle_externe);

		this.updated = (String) xpath.compile("/a:entry/a:updated/text()")
				.evaluate(document, XPathConstants.STRING);
		System.out.println(updated);

		this.published = (String) xpath.compile("/a:entry/a:published/text()")
				.evaluate(document, XPathConstants.STRING);
		System.out.println(published);

		this.derivationAdresse = (String) xpath.compile(
				"/a:entry/a:content/:div/:span[@class='derivation']/text()")
				.evaluate(document, XPathConstants.STRING);
		System.out.println(derivationAdresse);

	/*	NodeList traitement = (NodeList) xpath.compile(
				"/a:entry/a:content/:div/:ul[@class='traitement']/:li")
				.evaluate(document, XPathConstants.NODESET);
		if (traitement.getLength() > 0) {
			this.traitements = new ArrayList<Traitement>();
			System.out.println(traitement.getLength());
			for (int i = 1; i <= traitement.getLength(); i++) {
				this.traitements.add(new Traitement(i, document, xpath));
			}
		}
		NodeList traitementManuel = (NodeList) xpath.compile(
				"/a:entry/a:content/:div/:ul[@class='traitementManuel']/:li").evaluate(
				document, XPathConstants.NODESET);
		if (traitementManuel.getLength() > 0) {
			this.traitementsManuel = new ArrayList<TraitementManuel>();
			for (int i = 1; i <= traitementManuel.getLength(); i++) {
				this.traitementsManuel.add(new TraitementManuel(i, document,
						xpath));
			}
		}*/

		NodeList adresse = (NodeList) xpath.compile(
				"/a:entry/a:content/:div/:ul[@class='adresses']/:li").evaluate(
				document, XPathConstants.NODESET);
		if (adresse.getLength() > 0) {
			this.adresses = new ArrayList<Adresse>();
			System.out.println(adresse.getLength());
			for (int i = 1; i <= adresse.getLength(); i++) {
				this.adresses.add(new Adresse(i, document, xpath));
			}
		}
	}

	@Override
	public String toString() {
		return "";
	}

}
