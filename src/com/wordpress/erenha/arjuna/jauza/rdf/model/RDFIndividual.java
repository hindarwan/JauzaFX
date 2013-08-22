/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.rdf.model;

import java.util.Collections;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Hindarwan
 */
public class RDFIndividual {
    private final StringProperty uri;
    private RDFClass rdfClass;
    private List<RDFIndividualProperty> rdfIndividualProperty;

    public RDFIndividual(String uri, RDFClass rdfClass, List<RDFIndividualProperty> rdfIndividualProperty) {
        this.uri = new SimpleStringProperty(uri);
        this.rdfClass = rdfClass;
        this.rdfIndividualProperty = rdfIndividualProperty;
    }

    public List<RDFIndividualProperty> getRdfIndividualProperty() {
        return rdfIndividualProperty;
    }

    public void setRdfIndividualProperty(List<RDFIndividualProperty> rdfIndividualProperty) {
        this.rdfIndividualProperty = rdfIndividualProperty;
    }

    public RDFClass getRdfClass() {
        return rdfClass;
    }

    public void setRdfClass(RDFClass rdfClass) {
        this.rdfClass = rdfClass;
    }

    public String getUri() {
        return uri.get();
    }

    public void setUri(String value) {
        uri.set(value);
    }

    public StringProperty uriProperty() {
        return uri;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
