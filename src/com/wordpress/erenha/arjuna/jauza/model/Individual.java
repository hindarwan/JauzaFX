/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.model;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Hindarwan
 */
public class Individual {

    private final StringProperty id;
    private final StringProperty typeOf;
    private List<IndividualDetail> individualDetail;
    private final BooleanProperty selected;

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public List<IndividualDetail> getIndividualDetail() {
        return individualDetail;
    }

    public void setIndividualDetail(List<IndividualDetail> individualDetail) {
        this.individualDetail = individualDetail;
    }

    public Individual(String id, String typeOf, List individualDetail) {
        this.id = new SimpleStringProperty(id);
        this.typeOf = new SimpleStringProperty(typeOf);
        this.individualDetail = individualDetail;
        this.selected = new SimpleBooleanProperty(false);
    }

    public String getTypeOf() {
        return typeOf.get();
    }

    public void setTypeOf(String value) {
        typeOf.set(value);
    }

    public StringProperty typeOfProperty() {
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
