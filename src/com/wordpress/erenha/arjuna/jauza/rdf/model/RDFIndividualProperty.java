/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.rdf.model;

import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Hindarwan
 */
public class RDFIndividualProperty {
    private RDFProperty rdfProperty;
    private final StringProperty propertyValue;

    public RDFIndividualProperty(RDFProperty rdfProperty, String propertyValue) {
        this.rdfProperty = rdfProperty;
        this.propertyValue = new SimpleStringProperty(propertyValue);
    }

    public RDFProperty getRdfProperty() {
        return rdfProperty;
    }

    public void setRdfProperty(RDFProperty rdfProperty) {
        this.rdfProperty = rdfProperty;
    }

    public String getPropertyValue() {
        return propertyValue.get();
    }

    public void setPropertyValue(String value) {
        propertyValue.set(value);
    }

    public StringProperty propertyValueProperty() {
        return propertyValue;
    }
    
}
