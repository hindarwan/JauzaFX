/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.rdf;

import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFProperty;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFClass;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFContext;
import com.wordpress.erenha.arjuna.jauza.controller.MainController;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFNamespace;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
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
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.config.SailRepositoryConfig;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.config.SailImplConfig;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.inferencer.fc.config.ForwardChainingRDFSInferencerConfig;
import org.openrdf.sail.nativerdf.NativeStore;
import org.openrdf.sail.nativerdf.config.NativeStoreConfig;

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
            if (dataDir.exists()) {
                File[] listFiles = dataDir.listFiles();
                for (File file : listFiles) {
                    file.delete();
                }
            }
            String indexes = "spoc,posc,cosp";
            repo = new SailRepository(new ForwardChainingRDFSInferencer(new NativeStore(dataDir, indexes)));
            repo.initialize();
        } catch (RepositoryException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initRepository(String sesameServer, String repositoryId) {
        try {
            RemoteRepositoryManager manager = new RemoteRepositoryManager(sesameServer);
            manager.initialize();
//            String repositoryId = "jauzafx-db";
            SailImplConfig config = new ForwardChainingRDFSInferencerConfig(new NativeStoreConfig("spoc,posc,cosp"));
            SailRepositoryConfig repositoryTypeSpec = new SailRepositoryConfig(config);

            RepositoryConfig repConfig = new RepositoryConfig(repositoryId, repositoryTypeSpec);
            manager.addRepositoryConfig(repConfig);

            repo = manager.getRepository(repositoryId);
//            repo = new HTTPRepository(sesameServer, repositoryID);

            repo.initialize();
        } catch (RepositoryException | RepositoryConfigException ex) {
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
        getContext();
    }

    public void add(URL url) {
        try {
            RepositoryConnection connection = repo.getConnection();
            ValueFactory factory = repo.getValueFactory();
            URI context = factory.createURI(url.toString());
            RDFFormat format = RDFFormat.forFileName(url.toString());
            try {
                connection.begin();
                connection.add(url, null, format, context);
                connection.commit();
            } catch (RepositoryException re) {
                connection.rollback();
            } finally {
                connection.close();
            }
        } catch (IOException | RDFParseException | RepositoryException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
        getContext();
    }

    public void getContext() {
        try {
            mainController.getCurrentContext().clear();
            RepositoryConnection connection = repo.getConnection();
            try {
                RepositoryResult<Resource> contextIDs = connection.getContextIDs();
                while (contextIDs.hasNext()) {
                    URI resource = (URI) contextIDs.next();
                    mainController.getCurrentContext().add(new RDFContext(resource.toString(), resource.getLocalName()));
                }
            } finally {
                connection.close();
            }

        } catch (RepositoryException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getNamespaces() {
        try {
            RepositoryConnection connection = repo.getConnection();
            try {
                RepositoryResult<Namespace> namespaces = connection.getNamespaces();
                while (namespaces.hasNext()) {
                    Namespace ns = namespaces.next();
                    mainController.getCurrentNamespaces().add(new RDFNamespace(ns.getName(), ns.getPrefix()));
                }
            } finally {
                connection.close();
            }
        } catch (RepositoryException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getClasses() {
        try {
            mainController.getCurrentClasses().clear();
            RepositoryConnection connection = repo.getConnection();
            String query = "SELECT DISTINCT ?c ?cLabel\n"
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
                    mainController.getCurrentClasses().add(new RDFClass(uri.stringValue(), label.stringValue()));
                    mainController.getCurrentClassesLabel().add(label.stringValue());

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
            mainController.getCurrentProperties().clear();
            RepositoryConnection connection = repo.getConnection();
            String query = "SELECT DISTINCT ?p ?pLabel\n"
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
                    mainController.getCurrentPropertiesLabel().add(label.stringValue());
                }
            } finally {
                connection.close();
            }

        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
