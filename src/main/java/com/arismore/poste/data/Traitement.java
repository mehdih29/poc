package com.arismore.poste.data;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

public class Traitement {
	private String debutTraitement;
	private String idPlateforme;
	private String idMachineTri;
	private String entreeMachine;
	private String programmeTriTitle;
	private String programmeTriId;
	private String triSanctionPartiel;
	private String etat;
	private String receptacle;
	private String receptacleType;
	private String receptacleNiveau;
	private String receptacleLang;
	private String receptacleReel;
	private String raisonModificationReceptacle;
	private String receptacleReelNiveau;
	private String programmeTriText;
	

	public Traitement(Integer i, Document document, XPath xpath) {
		try {
			this.debutTraitement = (String) xpath.compile(
					"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:span[@class='dateDebut']/text()").evaluate(
					document, XPathConstants.STRING);
			this.idPlateforme = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:span[@class='idPlateforme']/text()")
					.evaluate(document, XPathConstants.STRING);
			this.idMachineTri = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:span[@class='idMachineTri']/text()")
					.evaluate(document, XPathConstants.STRING);
			this.entreeMachine = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:span[@class='entreeMachine']/text()")
					.evaluate(document, XPathConstants.STRING);
			this.programmeTriTitle = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:abbr[@class='programmeTri']/@title/text()")
					.evaluate(document, XPathConstants.STRING);
			this.programmeTriId = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:abbr[@class='programmeTri']/@id/text()")
					.evaluate(document, XPathConstants.STRING);
			this.programmeTriText = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:abbr[@class='programmeTri']/text()")
					.evaluate(document, XPathConstants.STRING);
			//TODO a verifier
			this.triSanctionPartiel = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:dl[@class='parametresTri']/:dd/text()")
					.evaluate(document, XPathConstants.STRING);
			this.etat = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:span[@class='etat']/text()")
					.evaluate(document, XPathConstants.STRING);
			this.receptacle = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:span[@class='receptacle']/text()")
					.evaluate(document, XPathConstants.STRING);
			this.receptacleType = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:span[@class='receptacle']/@type/text()")
					.evaluate(document, XPathConstants.STRING);
			this.receptacleNiveau = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:span[@class='receptacle']/@niveau/text()")
					.evaluate(document, XPathConstants.STRING);
			this.receptacleLang = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:span[@class='receptacle']/@lang/text()")
					.evaluate(document, XPathConstants.STRING);
			this.receptacleReel = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:div[@class='client']/:span[@class='receptacleReel']/text()")
					.evaluate(document, XPathConstants.STRING);
			this.raisonModificationReceptacle = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:div[@class='client']/:span[@class='raisonModificationReceptacle']/text()")
					.evaluate(document, XPathConstants.STRING);
			this.receptacleReelNiveau = (String) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString()+ "]/:div[@class='client']/:span[@class='receptacleReel']/@niveau/text()")
					.evaluate(document, XPathConstants.STRING);
			
			System.out.println(debutTraitement);
			System.out.println(idPlateforme);
			System.out.println(idMachineTri);
			System.out.println(entreeMachine);
			System.out.println(programmeTriTitle);
			System.out.println(programmeTriId);
			System.out.println(triSanctionPartiel);
			System.out.println(etat);
			System.out.println(receptacle);
			System.out.println(receptacleType);
			System.out.println(receptacleNiveau);
			System.out.println(receptacleLang);
			System.out.println(receptacleReel);
			System.out.println(raisonModificationReceptacle);
			System.out.println(receptacleReelNiveau);
			System.out.println(programmeTriText);

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
