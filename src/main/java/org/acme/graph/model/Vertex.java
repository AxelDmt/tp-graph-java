package org.acme.graph.model;

import java.util.List;
import java.util.ArrayList;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * Un sommet dans un graphe
 * 
 * @author MBorne
 *
 */
public class Vertex {

	/**
	 * Identifiant du sommet
	 */
	private String id;

	/**
	 * Position du sommet
	 */
	@JsonIgnore
	private Coordinate coordinate;

	@JsonIgnore
	private List<Edge> inEdges = new ArrayList<>(); 

	@JsonIgnore
	private List<Edge> outEdges = new ArrayList<>();

	/*public Vertex() {

	}
	*/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	@JsonIgnore
	public List<Edge> getInEdges() {
        return inEdges;
    }

	@JsonIgnore
    public List<Edge> getOutEdges() {
        return outEdges;
    }

	@JsonSerialize(using = GeometrySerializer.class)
	public Point getGeometry() {
		GeometryFactory gf = new GeometryFactory();
		return gf.createPoint(this.coordinate);
	}

	@Override
	public String toString() {
		return id;
	}

}
