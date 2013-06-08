/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Hindarwan
 */
public class ExtractedIndividual {

    private final StringProperty id;
    private final StringProperty property;
    private final StringProperty content;

    public ExtractedIndividual(String id, String content, String property) {
        this.id = new SimpleStringProperty(id);
        this.property = new SimpleStringProperty(property);
        this.content = new SimpleStringProperty(content);
    }

    public String getContent() {
        return content.get();
    }

    public void setContent(String value) {
        content.set(value);
    }

    public StringProperty contentProperty() {
        return content;
    }

    public String getProperty() {
        return property.get();
    }

    public void setProperty(String value) {
        property.set(value);
    }

    public StringProperty propertyProperty() {
        return property;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String value) {
        id.set(value);
    }

    public StringProperty idProperty() {
        return id;
    }
}
