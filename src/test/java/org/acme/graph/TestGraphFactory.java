package org.acme.graph;

import org.acme.graph.model.Edge;
import org.acme.graph.model.Graph;
import org.acme.graph.model.Vertex;
import org.locationtech.jts.geom.Coordinate;

public class TestGraphFactory {

	/**
	 * d / / a--b--c
	 * 
	 * @return
	 */
	public static Graph createGraph01() {
		Graph graph = new Graph();

		Vertex a = graph.createVertex(new Coordinate(0.0, 0.0), "a");
		/*a.setId("a");
		a.setCoordinate(new Coordinate(0.0, 0.0));
		graph.getVertices().add(a);*/

		Vertex b = graph.createVertex(new Coordinate(1.0, 0.0), "b");
		/*b.setId("b");
		b.setCoordinate(new Coordinate(1.0, 0.0));
		graph.getVertices().add(b);*/

		Vertex c = graph.createVertex(new Coordinate(2.0, 0.0), "c");;
		/*c.setId("c");
		c.setCoordinate(new Coordinate(2.0, 0.0));
		graph.getVertices().add(c);*/

		Vertex d = graph.createVertex(new Coordinate(1.0, 1.0), "d");;
		/*d.setId("d");
		d.setCoordinate(new Coordinate(1.0, 1.0));
		graph.getVertices().add(d);*/

		graph.createEdge(a,b,"ab");
		/*Edge ab = new Edge();
		ab.setId("ab");
		ab.setSource(a);
		ab.setTarget(b);
		graph.getEdges().add(ab);*/

		graph.createEdge(b,c,"bc");
		/*Edge bc = new Edge();
		bc.setId("bc");
		bc.setSource(b);
		bc.setTarget(c);
		graph.getEdges().add(bc);*/

		graph.createEdge(a,d,"ad");
		/*Edge ad = new Edge();
		ad.setId("ad");
		ad.setSource(a);
		ad.setTarget(d);
		graph.getEdges().add(ad);*/

		return graph;
	}

}
