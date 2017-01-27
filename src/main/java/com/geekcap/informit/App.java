package com.geekcap.informit;
import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.ResourceIterator;


public class App{

    private static final File DB_PATH = new File( "data" );
    private static GraphDatabaseService graphDB;

    // relationship type
	public enum RelationshipTypes implements RelationshipType {
		IS_FRIEND_OF,
		HAS_SEEN;
	}

 	public enum Labels implements Label {
        	USER,
        	MOVIE;
    }

	public static void main(String []args) {
        System.out.println("Hello, this is in Neo4j:\n");
    	// Create Database
            // GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
            // GraphDatabaseService db = dbFactory.newEmbeddedDatabase(DB_PATH);
            graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
			registerShutdownHook();
		// Create Nodes
			Transaction tx = graphDB.beginTx();
	    	Node steve = graphDB.createNode(Labels.USER);
            steve.setProperty("name", "Steve");
        	Node tom = graphDB.createNode(Labels.USER);
			tom.setProperty("name", "Tom");

			Node divergent = graphDB.createNode(Labels.MOVIE);
			divergent.setProperty("name", "Divergent");
     		
			tx.success();
	  
		// Create Relationship
			tx = graphDB.beginTx();
			steve.createRelationshipTo(tom, RelationshipTypes.IS_FRIEND_OF);
			Relationship relationship = steve.createRelationshipTo(divergent, RelationshipTypes.HAS_SEEN);
			tx.success();
			
		// Begin some search
			// Find all users
			ResourceIterator<Node> users = graphDB.findNodes( Labels.USER );
			System.out.println( "Users:" );
			while( users.hasNext() )
			{
    			Node user = users.next();
    			System.out.println( "\t" + user.getProperty( "name" ) );
			}

    }

	private static void registerShutdownHook()
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDB.shutdown();
            }
        } );
    }


}

