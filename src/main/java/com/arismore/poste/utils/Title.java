package com.arismore.poste.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
 
@XmlAccessorType(XmlAccessType.FIELD)
public class Title {
 
    @XmlAttribute
    private String type;
 
    @XmlValue
    private String number;
    
    
    public String toString(){
    	return "type " + this.type + " number " + this.number;
    }
 
 
} 
