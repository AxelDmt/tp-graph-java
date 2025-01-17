package org.acme.graph.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


import org.acme.graph.errors.NotFoundException;

import org.acme.graph.model.Edge;
import org.acme.graph.model.Graph;
import org.acme.graph.model.Vertex;
import main.java.org.acme.graph.path.PathNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

/**
 * 
 * Utilitaire pour le calcul du plus court chemin dans un graphe
 * 
 * @author MBorne
 *
 */
public class DijkstraPathFinder {

	private static final Logger log = LogManager.getLogger(DijkstraPathFinder.class);

	private Graph graph;
	private Map<Vertex, PathNode> nodes;

	private PathNode getNode(Vertex vertex) {
        return nodes.get(vertex);
    }

	public DijkstraPathFinder(Graph graph) {
		this.graph = graph;
		this.nodes = new HashMap<>();
	}

	/**
	 * Calcul du plus court chemin entre une origine et une destination
	 * 
	 * @param origin
	 * @param destination
	 * @return
	 */
	public List<Edge> findPath(Vertex origin, Vertex destination) {
		log.info("findPath({},{})...", origin, destination);
		initGraph(origin);
		Vertex current;
		while ((current = findNextVertex()) != null) {
			visit(current);
			if (getNode(destination).getCost() != Double.POSITIVE_INFINITY ) {
				log.info("findPath({},{}) : path found", origin, destination);
				return buildPath(destination);
			}
		}
		log.info("findPath({},{}) : path not found", origin, destination);
		throw new NotFoundException(String.format("Path not found from '%s' to '%s'", origin.getId(), destination.getId()));
	}

	/**
	 * Parcourt les arcs sortants pour atteindre les sommets avec le meilleur coût
	 * 
	 * @param vertex
	 */
	private void visit(Vertex vertex) {
		log.trace("visit({})", vertex);
		List<Edge> outEdges = graph.getOutEdges(vertex);
		/*
		 * On étudie chacun des arcs sortant pour atteindre de nouveaux sommets ou
		 * mettre à jour des sommets déjà atteint si on trouve un meilleur coût
		 */
		for (Edge outEdge : outEdges) {
			Vertex reachedVertex = outEdge.getTarget();
			/*
			 * Convervation de arc permettant d'atteindre le sommet avec un meilleur coût
			 * sachant que les sommets non atteint ont pour coût "POSITIVE_INFINITY"
			 */
			PathNode reachedNode = getNode(reachedVertex);
			double newCost = getNode(vertex).getCost() + outEdge.getCost();
			if (newCost < reachedNode.getCost()) {
				reachedNode.setCost(newCost);
				reachedNode.setReachingEdge(outEdge);
			}
		}
		/*
		 * On marque le sommet comme visité
		 */
		getNode(vertex).setVisited(true);
	}

	/**
	 * Construit le chemin en remontant les relations incoming edge
	 * 
	 * @param target
	 * @return
	 */
	private List<Edge> buildPath(Vertex target) {
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
	private void initGraph(Vertex source) {
        log.trace("initGraph({})", source);
        for (Vertex vertex : graph.getVertices()) {
            PathNode node = new PathNode(vertex);
            nodes.put(vertex, node);
            if (source == vertex) {
                node.setCost(0.0);
            }
        }
    }

	/**
	 * Recherche le prochain sommet à visiter. Dans l'algorithme de Dijkstra, ce
	 * sommet est le sommet non visité le plus proche de l'origine du calcul de plus
	 * court chemin.
	 * 
	 * @return
	 */
	private Vertex findNextVertex() {
		double minCost = Double.POSITIVE_INFINITY;
		Vertex result = null;
		for (Vertex vertex : graph.getVertices()) {
			PathNode node = getNode(vertex);
			// sommet déjà visité?
			if (node.isVisited()) {
				continue;
			}
			// sommet non atteint?
			if (node.getCost() == Double.POSITIVE_INFINITY) {
				continue;
			}
			// sommet le plus proche de la source?
			if (node.getCost() < minCost) {
				result = vertex;
				minCost = node.getCost();
			}
		}
		return result;
	}

}
