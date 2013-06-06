/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.rdf;

import com.wordpress.erenha.arjuna.jauza.controller.MainController;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 *
 * @author Hindarwan
 */
public class RDFController {

    private Repository repo;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initRepository(String data) {
        try {
            File dataDir = new File(data);
            String indexes = "spoc,posc,cosp";
            repo = new SailRepository(new ForwardChainingRDFSInferencer(new NativeStore(dataDir, indexes)));
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(File file) {
        try {
            RepositoryConnection connection = repo.getConnection();
            ValueFactory factory = repo.getValueFactory();
            URI context = factory.createURI(file.toURI().toString());
            RDFFormat format = RDFFormat.forFileName(file.toString());
            try {
                connection.begin();
                connection.add(file, null, format, context);
                connection.commit();
            } catch (RepositoryException re) {
                connection.rollback();
            } finally {
                connection.close();
            }
        } catch (IOException | RDFParseException | RepositoryException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getClasses() {
        try {

            RepositoryConnection connection = repo.getConnection();
            String query = "SELECT ?c ?cLabel\n"
                    + "WHERE\n"
                    + "{\n"
                    + "?c rdf:type rdfs:Class.\n"
                    + "?c rdfs:label ?cLabel.\n"
                    + "}";
            try {
                TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                TupleQueryResult result = tupleQuery.evaluate();
                List<String> bindingNames = result.getBindingNames();
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    Value uri = bindingSet.getValue(bindingNames.get(0));
                    Value label = bindingSet.getValue(bindingNames.get(1));
                    System.out.println(uri.stringValue());
                    mainController.getCurrentClasses().add(new RDFClass(uri.stringValue(), label.stringValue()));
                }
            } finally {
                connection.close();
            }

        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getProperties() {
        try {
            RepositoryConnection connection = repo.getConnection();
            String query = "SELECT ?p ?pLabel\n"
                    + "WHERE\n"
                    + "{\n"
                    + "?p rdf:type rdf:Property.\n"
                    + "?p rdfs:label ?pLabel.\n"
                    + "}";
            try {
                TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                TupleQueryResult result = tupleQuery.evaluate();
                List<String> bindingNames = result.getBindingNames();
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    Value uri = bindingSet.getValue(bindingNames.get(0));
                    Value label = bindingSet.getValue(bindingNames.get(1));
                    mainController.getCurrentProperties().add(new RDFProperty(uri.stringValue(), label.stringValue()));
                }
            } finally {
                connection.close();
            }

        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
