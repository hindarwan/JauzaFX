/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.model;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Hindarwan
 */
public class CurrentSelection {
    private final StringProperty id;
    private final StringProperty content;
    private List<String> listContent;

    public CurrentSelection(String id, String content, List<String> list ) {
        this.id = new SimpleStringProperty(id);
        this.content = new SimpleStringProperty(content);
        this.listContent = list;
    }

    public List<String> getListContent() {
        return listContent;
    }
    
    public void setListContent(List<String> list){
        this.listContent = list;
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
