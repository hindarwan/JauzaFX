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
public class IndividualDetail {
    private final StringProperty id;
    private final StringProperty value;
    private final StringProperty property;

    public IndividualDetail(String id, String value, String property) {
        this.id = new SimpleStringProperty(id);
        this.value = new SimpleStringProperty(value);
        this.property = new SimpleStringProperty(property);
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

    public String getValue() {
        return value.get();
    }

    public void setValue(String value1) {
        value.set(value1);
    }

    public StringProperty valueProperty() {
        return value;
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
