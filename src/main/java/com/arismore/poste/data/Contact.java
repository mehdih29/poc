package com.arismore.poste.data;

import java.util.List;
 
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlPath;
 
@XmlRootElement(name="node")
@XmlType(propOrder={"firstName", "lastName", "address", "phoneNumbers"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Contact {
 
    @XmlPath("node[@name='first-name']/text()")
    private String firstName;
 
    @XmlPath("node[@name='last-name']/text()")
    private String lastName;
 
    @XmlPath("node[@name='address']")
    private Address address;
 
    @XmlPath("node[@name='phone-number']")
    private List<PhoneNumber> phoneNumbers;
    
    @Override
    public String toString(){
    	return "first name: " + this.firstName + "\nlast name:  " + this.lastName 
    			+ "\nadresse" + this.address.toString() + "\nphone number" + this.phoneNumbers.toString();
    }

}
