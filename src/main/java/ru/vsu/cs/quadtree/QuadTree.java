package ru.vsu.cs.quadtree;

import java.util.List;

public interface QuadTree<T> extends Iterable<T> {
    Point<T> add(double x, double y, T val);
    T get(double x, double y);
    T remove(double x, double y);
    boolean contains(double x, double y);
    QuadNode<T> find(QuadNode<T> node, double x, double y);
    List<T> getValues();
    boolean isEmpty();
    List<String> getPoints();
}
