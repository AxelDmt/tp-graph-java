package main.java.org.acme.graph.path;

import org.acme.graph.model.Edge;
import org.acme.graph.model.Vertex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PathNode {
    
	private final Vertex vertex;
    /**
	 * dijkstra - coût pour atteindre le sommet
	 */
	private double cost;
	/**
	 * dijkstra - arc entrant avec le meilleur coût
	 */
	private Edge reachingEdge;
	/**
	 * dijkstra - indique si le sommet est visité
	 */
	private boolean visited;

	@JsonIgnore
	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@JsonIgnore
	public Edge getReachingEdge() {
		return reachingEdge;
	}

	public void setReachingEdge(Edge reachingEdge) {
		this.reachingEdge = reachingEdge;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}


    public PathNode(Vertex vertex) {
        this.vertex = vertex;
        this.cost = Double.POSITIVE_INFINITY;  // Initialisé à une valeur infinie pour Dijkstra
        this.reachingEdge = null;
        this.visited = false;
    }

}
