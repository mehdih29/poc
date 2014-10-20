package com.arismore.poste.utils;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlPath;
 
@XmlRootElement(name="a:entry")
//@XmlType(propOrder={"title", "id", "author", "updated", "traitement"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ParcelData {
	
	@XmlPath("a:title/div/span[@class='id']/text()")
	private String isie;
	
	@XmlPath("a:id/image?format=jpeg/text()")
	private String image;
	
	@XmlPath("a:title/div/span[@class='priorite']/text()")
	private String priorite;
			
	@XmlPath("a:title/div/span[@class='etat']/text()")
	private String etat;
	
    //@XmlPath("/a:entry/a:title/div")
    //private Title title;
 /*
    @XmlPath("/a:entry/a:id/text()")
    private String id;
    
    @XmlPath("/a:entry/a:author/a:name/text()")
    private String author;
    
    @XmlPath("/a:entry/a:title/div/div[@class='expires']/text()")
    private String expire;
    		
    @XmlPath("/a:entry/a:title/div/div[@class='client']/span[@class='cleExterne']/text()")
    private String cle_externe;
    
    @XmlPath("/a:entry/a:updated/text()")
    private Date modified;
    
    @XmlPath("/a:entry/a:published/text()")
    private Date published;
    */
    
    
    //@XmlPath("/a:entry/a:content/x:div/x:ul[@class='traitement']/li")
    //private List<Traitement> traitement;
    
    @Override
    public String toString(){
    	return "";
    }

}
