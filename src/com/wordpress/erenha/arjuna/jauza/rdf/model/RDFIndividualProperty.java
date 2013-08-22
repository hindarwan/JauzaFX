/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.rdf.model;

/**
 *
 * @author Hindarwan
 */
public class RDFIndividualProperty {
    private RDFProperty rdfProperty;
    private RDFValue rdfValue;

    public RDFIndividualProperty(RDFProperty rdfProperty, RDFValue rdfValue) {
        this.rdfProperty = rdfProperty;
        this.rdfValue = rdfValue;
    }

    public RDFProperty getRdfProperty() {
        return rdfProperty;
    }

    public void setRdfProperty(RDFProperty rdfProperty) {
        this.rdfProperty = rdfProperty;
    }

    public RDFValue getRdfValue() {
        return rdfValue;
    }

    public void setRdfValue(RDFValue rdfValue) {
        this.rdfValue = rdfValue;
    }

}
