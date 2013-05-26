/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.model.zzz;

import com.wordpress.erenha.arjuna.jauza.model.*;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableMap;

/**
 *
 * @author Hindarwan
 */
public class IndividualDetail {
    private final StringProperty id = new SimpleStringProperty();
    private final MapProperty<String, String> properties = new SimpleMapProperty<>();

    public ObservableMap getProperties() {
        return properties.get();
    }

    public void setProperties(ObservableMap value) {
        properties.set(value);
    }

    public MapProperty propertiesProperty() {
        return properties;
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
