package main.java.org.acme.graph.path;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;

import org.acme.graph.model.Vertex;
import org.acme.graph.model.Graph;
import org.acme.graph.model.Edge;
import org.acme.graph.routing.*;
import main.java.org.acme.graph.path.PathNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.hash.HashIndex;

import java.util.stream.Collectors;

public class PathTree {

    private static final Logger log = LogManager.getLogger(DijkstraPathFinder.class);
    private Graph graph;
	private IndexedCollection<PathNode> nodes;


    public PathNode getNode(Vertex vertex) {
        return nodes.retrieve(QueryFactory.equal(PathNode.VERTEX, vertex))
                    .stream()
                    .findFirst()
                    .orElse(null);
    }

    /**
	 * Construit le chemin en remontant les relations incoming edge
	 * 
	 * @param target
	 * @return
	 */
	public List<Edge> getPath(Vertex target) {
        if (!isReached(target)) {
            throw new IllegalArgumentException(String.format("Vertex '%s' is not reached.", target.getId()));
        }
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
    public PathTree(Vertex origin) {
        log.trace("initGraph({})", origin);
    
        this.nodes = new com.googlecode.cqengine.ConcurrentIndexedCollection<>();
        this.nodes.addIndex(HashIndex.onAttribute(PathNode.VERTEX));
        this.nodes.addIndex(NavigableIndex.onAttribute(PathNode.COST));
        this.nodes.addIndex(HashIndex.onAttribute(PathNode.VISITED));
    
        // Ajouter le nœud d'origine
        PathNode originNode = new PathNode(origin);
        originNode.setCost(0.0);
        nodes.add(originNode);
    }   
    
    public boolean isReached(Vertex vertex) {
        PathNode node = nodes.retrieve(QueryFactory.equal(PathNode.VERTEX, vertex))
                     .stream()
                     .findFirst()
                     .orElse(null);
        return node != null && node.getCost() != Double.POSITIVE_INFINITY;
    }

    public PathNode getOrCreateNode(Vertex vertex) {
        PathNode node = nodes.stream()
                             .filter(n -> n.getVertex().equals(vertex))
                             .findFirst()
                             .orElse(null);
        if (node == null) {
            node = new PathNode(vertex);
            nodes.add(node);
        }
        return node;
    }
    
    
    public Collection<Vertex> getReachedVertices() {
        return nodes.stream()
                    .filter(node -> node.getCost() != Double.POSITIVE_INFINITY)
                    .map(PathNode::getVertex)
                    .collect(Collectors.toList());
    }

    public void markVisited(Vertex vertex) {
        PathNode node = getNode(vertex);
        nodes.remove(node); 
        node.setVisited(true);
        nodes.add(node); 
    }
    
    public void setReached(Vertex vertex, double reachingCost, Edge reachingEdge) {
        PathNode node = getOrCreateNode(vertex);
        nodes.remove(node); 
        node.setCost(reachingCost);
        node.setReachingEdge(reachingEdge);
        nodes.add(node); 
    }

    public Vertex getNearestNonVisitedVertex() {
        return nodes.stream()
                    .filter(node -> !node.isVisited())
                    .min((n1, n2) -> Double.compare(n1.getCost(), n2.getCost())) 
                    .map(PathNode::getVertex) 
                    .orElse(null); 
    }
    
}
