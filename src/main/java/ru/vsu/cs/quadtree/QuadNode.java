package ru.vsu.cs.quadtree;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final class QuadNode<T> {
    private double x;
    private double y;
    private double width;
    private double height;
    private QuadNode<T> parent;
    private Point<T> point;
    private QuadNode<T> northWest;
    private QuadNode<T> northEast;
    private QuadNode<T> SouthWest;
    private QuadNode<T> SouthEast;

    public QuadNode(double x, double y, double width, double height, QuadNode<T> parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }
}
