//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.23 at 05:21:56 PM CEST 
//


package com.thalesgroup.optet.devenv.jaxb.optetprofiles;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.optet.jaxb.optetprofiles package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.optet.jaxb.optetprofiles
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OptetProfiles }
     * 
     */
    public OptetProfiles createOptetProfiles() {
        return new OptetProfiles();
    }

    /**
     * Create an instance of {@link OptetProfiles.OptetProfile }
     * 
     */
    public OptetProfiles.OptetProfile createOptetProfilesOptetProfile() {
        return new OptetProfiles.OptetProfile();
    }

    /**
     * Create an instance of {@link OptetProfiles.OptetProfile.Checkstyle }
     * 
     */
    public OptetProfiles.OptetProfile.Checkstyle createOptetProfilesOptetProfileCheckstyle() {
        return new OptetProfiles.OptetProfile.Checkstyle();
    }

    /**
     * Create an instance of {@link OptetProfiles.OptetProfile.Codepro }
     * 
     */
    public OptetProfiles.OptetProfile.Codepro createOptetProfilesOptetProfileCodepro() {
        return new OptetProfiles.OptetProfile.Codepro();
    }

    /**
     * Create an instance of {@link OptetProfiles.OptetProfile.Pmd }
     * 
     */
    public OptetProfiles.OptetProfile.Pmd createOptetProfilesOptetProfilePmd() {
        return new OptetProfiles.OptetProfile.Pmd();
    }

    /**
     * Create an instance of {@link OptetProfiles.OptetProfile.Findbugs }
     * 
     */
    public OptetProfiles.OptetProfile.Findbugs createOptetProfilesOptetProfileFindbugs() {
        return new OptetProfiles.OptetProfile.Findbugs();
    }

}
