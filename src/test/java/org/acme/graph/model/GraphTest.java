package org.acme.graph.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.acme.graph.TestGraphFactory;
import org.acme.graph.errors.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;

public class GraphTest {

	@Test
	public void testFindVertexById() {
		Vertex v = TestGraphFactory.createGraph01().findVertex("a");
		assertNotNull(v);
		assertEquals("a", v.getId());
	}

	@Test
	public void testFindVertexByIdNotFound() {
		Graph g = TestGraphFactory.createGraph01();
		NotFoundException e = Assert.assertThrows(NotFoundException.class, () -> g.findVertex("missing"));
		assertEquals("Vertex 'missing' not found", e.getMessage());
	}

	@Test
	public void testFindVertexByCoordinate() {
		Graph graph = new Graph();
		Coordinate c = new Coordinate(0.0, 0.0);
		Vertex v = new Vertex();
		v.setId("a");
		v.setCoordinate(c);
		graph.getVertices().add(v);
		assertNotNull(v);
		assertEquals("a", v.getId());
	}

	@Test
	public void testFindVertexByCoordinateNotFound() {
		Graph g = TestGraphFactory.createGraph01();
		Coordinate c = new Coordinate(888.0, 999.0);
		NotFoundException e = Assert.assertThrows(NotFoundException.class, () -> g.findVertex(c));
		assertEquals("Vertex not found at [888.0,999.0]", e.getMessage());
	}

	@Test
	public void testGetOutEdges() {
		Graph g = TestGraphFactory.createGraph01();
		Vertex a = g.findVertex("a");
		assertNotNull(a);
		List<Edge> result = g.getOutEdges(a);
		assertEquals(2, result.size());
		assertEquals("ab (a->b)", result.get(0).toString());
		assertEquals("ad (a->d)", result.get(1).toString());
	}

	@Test
	public void testGetInEdges() {
		Graph g = TestGraphFactory.createGraph01();
		Vertex b = g.findVertex("b");
		assertNotNull(b);
		List<Edge> result = g.getInEdges(b);
		assertEquals(1, result.size());
		assertEquals("ab (a->b)", result.get(0).toString());
	}

}
