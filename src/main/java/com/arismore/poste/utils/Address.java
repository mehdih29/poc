package com.arismore.poste.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
 
import org.eclipse.persistence.oxm.annotations.XmlPath;
 
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {
 
    @XmlPath("node[@name='street']/text()")
    private String street;
    
    
    public String toString(){
    	return this.street;
    }
 
}
