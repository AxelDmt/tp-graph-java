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
import main.java.org.acme.graph.path.PathTree;
import main.java.org.acme.graph.path.PathNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import org.acme.graph.model.Isochrone;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.algorithm.ConvexHull;

import java.util.HashSet;
import java.util.Set;

import org.locationtech.jts.geom.Geometry;


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
	private PathTree pathTree;

	public DijkstraPathFinder(Graph graph) {
		this.graph = graph;
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
		pathTree = new PathTree(origin);
		Vertex current;
		while ((current = findNextVertex()) != null) {
			visit(current);
			if (pathTree.isReached(destination)) {
				log.info("findPath({},{}) : path found", origin, destination);
				return pathTree.getPath(destination);
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
			PathNode reachedNode = pathTree.getOrCreateNode(reachedVertex);
			double newCost = pathTree.getNode(vertex).getCost() + outEdge.getCost();
			if (newCost < reachedNode.getCost()) {
				reachedNode.setCost(newCost);
				reachedNode.setReachingEdge(outEdge);
			}
		}
		/*
		 * On marque le sommet comme visité
		 */
		pathTree.getNode(vertex).setVisited(true);
	}

	/**
	 * Recherche le prochain sommet à visiter. Dans l'algorithme de Dijkstra, ce
	 * sommet est le sommet non visité le plus proche de l'origine du calcul de plus
	 * court chemin.
	 * 
	 * @return
	 */
	private Vertex findNextVertex() {
		return pathTree.getNearestNonVisitedVertex();
	}

	public Isochrone findIsochrone(Vertex origin, double radius) {
		log.info("findIsochrone({}, {})...", origin, radius);
	
		pathTree = new PathTree(origin);
		Set<Coordinate> reachedCoordinates = new HashSet<>();
	
		// Visite des sommets dans le graphe
		Vertex current;
		while ((current = findNextVertex()) != null) {
			visit(current);
	
			PathNode currentNode = pathTree.getNode(current);
			if (currentNode.getCost() <= radius) {
				// On ajoute les coordonnées atteintes si elles sont dans le rayon spécifié
				reachedCoordinates.add(current.getCoordinate());
			}
	
			// Si le coût dépasse le rayon, on arrête la recherche
			if (currentNode.getCost() > radius) {
				break;
			}
		}
	
		// Vérification si des points ont été atteints avant de procéder au ConvexHull
		if (reachedCoordinates.isEmpty()) {
			throw new IllegalStateException("Aucun point n'a été atteint dans le rayon spécifié.");
		}
	
		// Calcul du polygone ConvexHull à partir des coordonnées
		GeometryFactory geometryFactory = new GeometryFactory();
		ConvexHull convexHull = new ConvexHull(reachedCoordinates.toArray(new Coordinate[0]), geometryFactory);
	
		// On s'assure que le ConvexHull retourne bien un Polygon
		Geometry hullGeometry = convexHull.getConvexHull();
		if (!(hullGeometry instanceof Polygon)) {
			throw new IllegalStateException("Le ConvexHull ne retourne pas un Polygon valide.");
		}
	
		Polygon isochronePolygon = (Polygon) hullGeometry;
	
		// Retourne l'isochrone avec le rayon et le polygone calculé
		return new Isochrone(radius, isochronePolygon);
	}
}
