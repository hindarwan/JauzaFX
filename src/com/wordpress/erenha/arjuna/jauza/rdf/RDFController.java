/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.rdf;

import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFProperty;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFClass;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFContext;
import com.wordpress.erenha.arjuna.jauza.controller.MainController;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFIndividual;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFIndividualProperty;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFNamespace;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFOntology;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFValue;
import com.wordpress.erenha.arjuna.jauza.util.StaticValue;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import jauzafx.scene.control.Dialogs;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.TreeModel;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
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

    //for native stor repo
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

    public void addNamespace(String prefix, String ns) {
        try {
            RepositoryConnection connection = repo.getConnection();
            try {
                connection.setNamespace(prefix, ns);
            } finally {
                connection.close();
            }
        } catch (RepositoryException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
        getNamespaces();
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

    public String getNamespaces(String prefix) {
        try {
            RepositoryConnection connection = repo.getConnection();
            try {
                return connection.getNamespace(prefix);
            } finally {
                connection.close();
            }
        } catch (RepositoryException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public void getOntologies() {
        try {
            mainController.getCurrentOntologies().clear();
            RepositoryConnection connection = repo.getConnection();
            String query = "PREFIX dc:<http://purl.org/dc/elements/1.1/> "
                    + "SELECT DISTINCT ?c ?cLabel\n"
                    + "WHERE\n"
                    + "{\n"
                    + "?c rdf:type owl:Ontology.\n"
                    + "?c rdfs:label|dc:title ?cLabel.\n"
                    + "}";
            try {
                TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                TupleQueryResult result = tupleQuery.evaluate();
                List<String> bindingNames = result.getBindingNames();
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    Value uri = bindingSet.getValue(bindingNames.get(0));
                    Value label = bindingSet.getValue(bindingNames.get(1));
                    mainController.getCurrentOntologies().add(new RDFOntology(uri.stringValue(), label.stringValue()));
                }
            } finally {
                connection.close();
            }

        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getClasses() {
        try {
            mainController.getCurrentClassesLabel().clear();
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
//                    mainController.getCurrentClasses().add(new RDFClass(uri.stringValue(), label.stringValue()));
                    mainController.getCurrentClassesLabel().add(new RDFClass(uri.stringValue(), label.stringValue()));
                }
            } finally {
                connection.close();
            }

        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getClassesInNSdefined() {
        mainController.getCurrentClassesLabel().clear();
        ObservableList<RDFNamespace> currentNamespaces = mainController.getCurrentNamespaces();
        for (RDFNamespace ns : currentNamespaces) {
            try {
                RepositoryConnection connection = repo.getConnection();
                String query = "SELECT DISTINCT ?c ?cLabel\n"
                        + "WHERE\n"
                        + "{\n"
                        + "?c rdf:type rdfs:Class.\n"
                        //                        + "?c rdfs:label ?cLabel.\n"
                        //                    + "?c rdfs:isDefinedBy <" + ns + ">.\n"
                        //                        + "FILTER(STRSTARTS(STR(?c),\"" + ns.getNamespace() + "\"))"
                        //                        + "}"
                        + "FILTER(STRSTARTS(STR(?c),\"" + ns.getNamespace() + "\")).\n"
                        + "BIND(STRAFTER(STR(?c),\"" + ns.getNamespace() + "\") AS ?cLabel)"
                        + "}";
                try {
                    TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                    TupleQueryResult result = tupleQuery.evaluate();
                    List<String> bindingNames = result.getBindingNames();
                    while (result.hasNext()) {
                        BindingSet bindingSet = result.next();
                        Value uri = bindingSet.getValue(bindingNames.get(0));
                        Value label = bindingSet.getValue(bindingNames.get(1));
//                        mainController.getCurrentClasses().add(new RDFClass(uri.stringValue(), label.stringValue()));
//                        mainController.getCurrentClassesLabel().add(ns.getPrefix() + ":" + label.stringValue());
//                        mainController.getCurrentClassesLabel().add(uri.stringValue());
//                        mainController.getCurrentClassesLabel().add(label.stringValue());
                    }
                } finally {
                    connection.close();
                }

            } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
                Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getClassesByNS(String ns) {
        try {
            mainController.getCurrentClasses().clear();
            RepositoryConnection connection = repo.getConnection();
            String query = "SELECT DISTINCT ?c ?cLabel ?cComment\n"
                    + "WHERE\n"
                    + "{\n"
                    + "?c rdf:type rdfs:Class.\n"
                    + "?c rdfs:label ?cLabel.\n"
                    + "?c rdfs:comment ?cComment.\n"
                    //                    + "?c rdfs:isDefinedBy <" + ns + ">.\n"
                    + "FILTER(STRSTARTS(STR(?c),\"" + ns + "\"))"
                    + "}";
            try {
                TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                TupleQueryResult result = tupleQuery.evaluate();
                List<String> bindingNames = result.getBindingNames();
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    Value uri = bindingSet.getValue(bindingNames.get(0));
                    Value label = bindingSet.getValue(bindingNames.get(1));
                    Value comment = bindingSet.getValue(bindingNames.get(2));
                    mainController.getCurrentClasses().add(new RDFClass(uri.stringValue(), "\"" +label.stringValue() + "\"\n" + comment.stringValue()));
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
//                    mainController.getCurrentPropertiesLabel().add(label.stringValue());
                }
            } finally {
                connection.close();
            }

        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPropertiesInNSdefined() {
        mainController.getCurrentPropertiesLabel().clear();
        ObservableList<RDFNamespace> currentNamespaces = mainController.getCurrentNamespaces();
        for (RDFNamespace ns : currentNamespaces) {
            try {
                RepositoryConnection connection = repo.getConnection();
                String query = "SELECT DISTINCT ?p ?pLabel\n"
                        + "WHERE\n"
                        + "{\n"
                        + "?p rdf:type rdf:Property.\n"
                        //                        + "?p rdfs:label ?pLabel.\n"
                        //                    + "?p rdfs:isDefinedBy <" + ns + ">.\n"
                        + "FILTER(STRSTARTS(STR(?p),\"" + ns.getNamespace() + "\")).\n"
                        + "BIND(STRAFTER(STR(?p),\"" + ns.getNamespace() + "\") AS ?pLabel)"
                        + "}";
                try {
                    TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                    TupleQueryResult result = tupleQuery.evaluate();
                    List<String> bindingNames = result.getBindingNames();
                    while (result.hasNext()) {
                        BindingSet bindingSet = result.next();
                        Value uri = bindingSet.getValue(bindingNames.get(0));
                        Value label = bindingSet.getValue(bindingNames.get(1));
//                        mainController.getCurrentClasses().add(new RDFClass(uri.stringValue(), label.stringValue()));
//                        mainController.getCurrentPropertiesLabel().add(ns.getPrefix() + ":" + label.stringValue());
//                        mainController.getCurrentPropertiesLabel().add(label.stringValue());
//                        mainController.getCurrentPropertiesToShow().add(new RDFProperty(uri.stringValue(), ns.getPrefix() + label.stringValue()));

                    }
                } finally {
                    connection.close();
                }

            } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
                Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getPropertiesByNS(String ns) {
        try {
            mainController.getCurrentProperties().clear();
            RepositoryConnection connection = repo.getConnection();
            String query = "SELECT DISTINCT ?p ?pLabel ?pComment\n"
                    + "WHERE\n"
                    + "{\n"
                    + "?p rdf:type rdf:Property.\n"
                    + "?p rdfs:label ?pLabel.\n"
                    + "?p rdfs:comment ?pComment.\n"
                    //                    + "?p rdfs:isDefinedBy <" + ns + ">.\n"
                    + "FILTER(STRSTARTS(STR(?p),\"" + ns + "\"))"
                    + "}";
            try {
                TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                TupleQueryResult result = tupleQuery.evaluate();
                List<String> bindingNames = result.getBindingNames();
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    Value uri = bindingSet.getValue(bindingNames.get(0));
                    Value label = bindingSet.getValue(bindingNames.get(1));
                    Value comment = bindingSet.getValue(bindingNames.get(2));
                    mainController.getCurrentProperties().add(new RDFProperty(uri.stringValue(), "\"" + label.stringValue() + "\"\n" + comment.stringValue()));
//                    mainController.getCurrentPropertiesLabel().add(label.stringValue());
                }
            } finally {
                connection.close();
            }

        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getPropertiesByClass(RDFClass rdfClass) {
        try {
            mainController.getCurrentPropertiesLabel().clear();
            RepositoryConnection connection = repo.getConnection();
            String query = "SELECT DISTINCT ?p ?pLabel\n"
                    + "WHERE\n"
                    + "{\n"
                    + "?p rdf:type rdf:Property.\n"
                    + "?p rdfs:domain <" + rdfClass.getUri() + ">.\n"
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

//                    mainController.getCurrentPropertiesLabel().add(toNamespacePrefix(uri.stringValue()));
                    mainController.getCurrentPropertiesLabel().add(new RDFProperty(uri.stringValue(), label.stringValue()));
//                    mainController.getCurrentPropertiesLabel().add(label.stringValue());
                }
            } finally {
                connection.close();
            }

        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPropertiesCommentByClass(RDFClass rdfClass) {
        if (rdfClass != null) {
            try {
                mainController.getCurrentProperties().clear();
                RepositoryConnection connection = repo.getConnection();
                String query = "SELECT DISTINCT ?p ?pLabel ?pComment\n"
                        + "WHERE\n"
                        + "{\n"
                        + "?p rdf:type rdf:Property.\n"
                        + "?p rdfs:domain <" + rdfClass.getUri() + ">.\n"
                        + "?p rdfs:label ?pLabel.\n"
                        + "?p rdfs:comment ?pComment.\n"
                        + "}";
                try {
                    TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                    TupleQueryResult result = tupleQuery.evaluate();
                    List<String> bindingNames = result.getBindingNames();
                    while (result.hasNext()) {
                        BindingSet bindingSet = result.next();
                        Value uri = bindingSet.getValue(bindingNames.get(0));
                        Value label = bindingSet.getValue(bindingNames.get(1));
                        Value comment = bindingSet.getValue(bindingNames.get(2));
                        mainController.getCurrentProperties().add(new RDFProperty(uri.stringValue(), "\"" + label.stringValue() + "\"\n" + comment.stringValue()));
                    }
                } finally {
                    connection.close();
                }

            } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
                Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private RDFClass getClassFromProperty(RDFProperty property) {
        try {
            RepositoryConnection connection = repo.getConnection();

            String query = "SELECT DISTINCT ?c ?cLabel\n"
                    + "WHERE\n"
                    + "{\n"
                    + "<" + property.getUri() + "> rdfs:range ?c.\n"
                    + "}";
            try {
                TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                TupleQueryResult result = tupleQuery.evaluate();
                List<String> bindingNames = result.getBindingNames();
                if (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    Value uri = bindingSet.getValue(bindingNames.get(0));
                    return new RDFClass(uri.stringValue(), "");
                }
            } finally {
                connection.close();
            }
            return null;
        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<RDFValue> getAllIndividualByTypeFromProperty(RDFProperty property) {
        RDFClass rdfClass = getClassFromProperty(property);
        List<RDFValue> list = new ArrayList<>();
        try {
            RepositoryConnection connection = repo.getConnection();
            String query = "PREFIX dc:<http://purl.org/dc/elements/1.1/>\n"
                    + "SELECT DISTINCT ?s ?sLabel\n"
                    + "WHERE\n"
                    + "{\n"
                    + "?s rdf:type <" + rdfClass.getUri() + ">.\n"
                    + "?s rdfs:label|dc:title ?sLabel.\n"
                    + "}";
            try {
                TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                TupleQueryResult result = tupleQuery.evaluate();
                List<String> bindingNames = result.getBindingNames();
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    Value uri = bindingSet.getValue(bindingNames.get(0));
                    Value label = bindingSet.getValue(bindingNames.get(1));
                    list.add(new RDFValue(uri.stringValue(), label.stringValue()));
                }
            } finally {
                connection.close();
            }
            return list;
        } catch (RepositoryException | MalformedQueryException | QueryEvaluationException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
            return list;
        }
    }

    public void saveAllIndividual() {
        String ns = StaticValue.baseURL + "/resource/";
        ObservableList<RDFIndividual> currentIndividuals = mainController.getCurrentIndividuals();
        ValueFactory f = repo.getValueFactory();
        Graph g = new TreeModel();
        for (RDFIndividual individual : currentIndividuals) {
            URI uriIndividual = f.createURI(ns, individual.getUri());
            URI typeIndividual = f.createURI(individual.getRdfClass().getUri());
            Statement typeStatement = f.createStatement(uriIndividual, RDF.TYPE, typeIndividual);
            g.add(typeStatement);
            List<RDFIndividualProperty> propertyList = individual.getRdfIndividualProperty();
            for (RDFIndividualProperty property : propertyList) {
                URI uriProperty = f.createURI(property.getRdfProperty().getUri());

                if (property.getRdfValue().getLabel().isEmpty() && property.getRdfValue().getUri().isEmpty()) {
                    //do not save
                } else if (property.getRdfValue().getLabel().isEmpty()) {
                    URI valueResource = f.createURI(property.getRdfValue().getUri());
                    Statement resourceStatement = f.createStatement(uriIndividual, uriProperty, valueResource);
                    g.add(resourceStatement);
                } else if (property.getRdfValue().getUri().isEmpty()) {
                    Literal valueLiteral = f.createLiteral(property.getRdfValue().getLabel());
                    Statement literalStatement = f.createStatement(uriIndividual, uriProperty, valueLiteral);
                    g.add(literalStatement);
                } else {
                    URI valueResource = f.createURI(property.getRdfValue().getUri());
                    Statement resourceStatement = f.createStatement(uriIndividual, uriProperty, valueResource);
                    g.add(resourceStatement);
                    Literal valueLiteral = f.createLiteral(property.getRdfValue().getLabel());
                    Statement literalStatement = f.createStatement(valueResource, RDFS.LABEL, valueLiteral);
                    g.add(literalStatement);
                }
            }
        }
        try {
            RepositoryConnection connection = repo.getConnection();
            try {
                connection.add(g, new URIImpl(ns + UUID.randomUUID().toString().replaceAll("-", "")));
                System.out.println("[INFO]: Individual saved");
                Dialogs.showInformationDialog(mainController.getPrimaryStage(), "All Individuals Succesfully Saved", "Information Dialog", "Save Individual");
            } finally {
                connection.close();
            }
        } catch (RepositoryException ex) {
            Logger.getLogger(RDFController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isLiteral(String property) {

        return true;
    }
}
