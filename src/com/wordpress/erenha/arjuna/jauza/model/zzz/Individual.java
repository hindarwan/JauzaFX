/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.model.zzz;

import com.wordpress.erenha.arjuna.jauza.model.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Hindarwan
 */
public class Individual {
    private final StringProperty id = new SimpleStringProperty();
    private final ListProperty<?> typeOf = new SimpleListProperty<>();
    private final ListProperty<IndividualDetail> properties = new SimpleListProperty<>();

    public ObservableList getProperties() {
        return properties.get();
    }

    public void setProperties(ObservableList value) {
        properties.set(value);
    }

    public ListProperty propertiesProperty() {
        return properties;
    }

    public ObservableList getTypeOf() {
        return typeOf.get();
    }

    public void setTypeOf(ObservableList value) {
        typeOf.set(value);
    }

    public ListProperty typeOfProperty() {
        return typeOf;
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
