package main.java.org.acme.graph.path;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

import org.acme.graph.model.Vertex;
import org.acme.graph.model.Graph;
import org.acme.graph.model.Edge;
import org.acme.graph.routing.*;
import main.java.org.acme.graph.path.PathNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PathTree {

    private static final Logger log = LogManager.getLogger(DijkstraPathFinder.class);
    private Graph graph;
	private Map<Vertex, PathNode> nodes;

    public PathNode getNode(Vertex vertex) {
        return nodes.get(vertex);
    }

    /**
	 * Construit le chemin en remontant les relations incoming edge
	 * 
	 * @param target
	 * @return
	 */
	public List<Edge> getPath(Vertex target) {
		List<Edge> result = new ArrayList<>();
		PathNode targetNode = getNode(target);
		for ( 
			Edge current = targetNode.getReachingEdge();
			current != null;
			current = getNode(current.getSource()).getReachingEdge()
		){
			result.add(current);
		}

		Collections.reverse(result);
		return result;
	}

    /**
	 * Prépare le graphe pour le calcul du plus court chemin
	 * 
	 * @param source
	 */
	public PathTree(Graph graph, Vertex source) {
        log.trace("initGraph({})", source);
        this.graph = graph;
        nodes = new HashMap<>();
        
        for (Vertex vertex : graph.getVertices()) {
            PathNode node = new PathNode(vertex);
            nodes.put(vertex, node);
            if (source == vertex) {
                node.setCost(0.0);
            }
        }
    }

}
