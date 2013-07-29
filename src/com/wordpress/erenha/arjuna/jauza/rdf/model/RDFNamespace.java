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
public class RDFNamespace {
    private final StringProperty namespace;
    private final StringProperty prefix;

    public RDFNamespace(String namespace, String prefix) {
        this.namespace = new SimpleStringProperty(namespace);
        this.prefix  = new SimpleStringProperty(prefix);
    }

    public String getPrefix() {
        return prefix.get();
    }

    public void setPrefix(String value) {
        prefix.set(value);
    }

    public StringProperty prefixProperty() {
        return prefix;
    }

    public String getNamespace() {
        return namespace.get();
    }

    public void setNamespace(String value) {
        namespace.set(value);
    }

    public StringProperty namespaceProperty() {
        return namespace;
    }
    
}
