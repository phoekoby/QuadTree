package ru.vsu.cs.quadtree;

import java.util.List;

public interface QuadTree<T> extends Iterable<T> {
    Point<T> add(int x, int y, T val);
    T get(int x, int y);
    T remove(int x, int y);
    boolean contains(int x, int y);
    QuadNode<T> find(QuadNode<T> node, int x, int y);
    List<T> getValues();
    boolean isEmpty();
    List<String> getPoints();
}
