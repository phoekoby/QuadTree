package ru.vsu.cs.quadtree;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final class QuadNode<T> {
    private int x;
    private int y;
    private int width;
    private int height;
    private QuadNode<T> parent;
    private Point<T> point;
    private QuadNode<T> northWest;
    private QuadNode<T> northEast;
    private QuadNode<T> SouthWest;
    private QuadNode<T> SouthEast;

    public QuadNode(int x, int y, int width, int height, QuadNode<T> parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }
}
