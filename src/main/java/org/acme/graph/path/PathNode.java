package main.java.org.acme.graph.path;

import org.acme.graph.model.Edge;
import org.acme.graph.model.Vertex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;


public class PathNode {

    public static final SimpleAttribute<PathNode, Vertex> VERTEX = new SimpleAttribute<PathNode, Vertex>("vertex") {
        public Vertex getValue(PathNode node, QueryOptions queryOptions) {
            return node.vertex;
        }
    };

    public static final SimpleAttribute<PathNode, Double> COST = new SimpleAttribute<PathNode, Double>("cost") {
        public Double getValue(PathNode node, QueryOptions queryOptions) {
            return node.cost;
        }
    };

    public static final SimpleAttribute<PathNode, Boolean> VISITED = new SimpleAttribute<PathNode, Boolean>("visited") {
        public Boolean getValue(PathNode node, QueryOptions queryOptions) {
            return node.visited;
        }
    };

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

	public Vertex getVertex() {
		return vertex;
	}	

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
