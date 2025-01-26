package org.acme.graph.model;

import org.locationtech.jts.geom.Polygon;

public class Isochrone {

    private double radius;
    private Polygon geometry;

    // Constructeur
    public Isochrone(double radius, Polygon geometry) {
        this.radius = radius;
        this.geometry = geometry;
    }

    // Getters et setters
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Polygon getGeometry() {
        return geometry;
    }

    public void setGeometry(Polygon geometry) {
        this.geometry = geometry;
    }
}
