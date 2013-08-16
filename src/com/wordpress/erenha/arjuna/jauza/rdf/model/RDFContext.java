/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.rdf.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Hindarwan
 */
public class RDFContext {
    private final StringProperty uri;
    private final StringProperty label;

    public RDFContext(String uri, String label) {
        this.uri = new SimpleStringProperty(uri);
        this.label = new SimpleStringProperty(label);
    }

    public String getLabel() {
        return label.get();
    }

    public void setLabel(String value) {
        label.set(value);
    }

    public StringProperty labelProperty() {
        return label;
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
        return label.get();
    }
    
    
}
