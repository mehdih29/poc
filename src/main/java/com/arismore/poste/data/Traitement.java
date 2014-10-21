package com.arismore.poste.data;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Traitement {
	
	public Traitement(Integer i, Document document, XPath xpath){
		try {
			String debutTraitement = (String) xpath.compile("/a:entry/a:content/:div/:ul[@class='traitement']/:li["+ i.toString() +"]/:span[@class='dateDebut']/text()").evaluate(document, XPathConstants.STRING);
			String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/span[@class='idPlateforme']/text()").evaluate(document, XPathConstants.STRING);
					String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/span[@class='idMachineTri']/text()").evaluate(document, XPathConstants.STRING); 
							String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/span[@class='entreeMachine']/text()").evaluate(document, XPathConstants.STRING);
									String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/abbr[@class='programmeTri']/@title/text()").evaluate(document, XPathConstants.STRING);
											String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/abbr[@class='programmeTri']/@id/text()").evaluate(document, XPathConstants.STRING);
													String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/abbr[@class='programmeTri']/text()").evaluate(document, XPathConstants.STRING); 
															String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/dl[@class='parametresTri']/[dt = 'triSanctionPartiel']/dd/text()").evaluate(document, XPathConstants.STRING); 
																	String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/span[@class='etat']/text()").evaluate(document, XPathConstants.STRING); 
																			String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/span[@class='receptacle']/text()").evaluate(document, XPathConstants.STRING);
																					String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/span[@class='receptacle']/@type/text()").evaluate(document, XPathConstants.STRING);
																							String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/span[@class='receptacle']/@niveau/text()").evaluate(document, XPathConstants.STRING);
																									String  = (String) xpath.compile("/a:entry/a:content/x:div/x:ul[@class='traitement']/x:li[i]/x:span[@class='receptacle']/@lang/text()").evaluate(document, XPathConstants.STRING);
																											String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/div[@class='client']/span[@class='receptacleReel']/text()").evaluate(document, XPathConstants.STRING);
																													String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/div[@class='client']/span[@class='raisonModificationReceptacle']/text()").evaluate(document, XPathConstants.STRING); 
																															String  = (String) xpath.compile("/a:entry/a:content/div/ul[@class='traitement']/li[i]/div[@class='client']/span[@class='receptacleReel']/@niveau/text()").evaluate(document, XPathConstants.STRING);
			System.out.println(published);
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
