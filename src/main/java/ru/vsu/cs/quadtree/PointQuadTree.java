package ru.vsu.cs.quadtree;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;


@Getter
public final class PointQuadTree<T> implements QuadTree<T> {
    private final QuadNode<T> root;
    private int size = 0;

    public PointQuadTree(int minX, int minY, int maxX, int maxY) throws Exception {
        if (minX >= 0 && minY >= 0 && maxX > 0 && maxY > 0 && maxY > minY && maxX > minX) {
            root = new QuadNode<>(minX, minY, maxX - minX, maxY - minY, null);
        }else {
            throw new Exception("Incorrect coordinates");
        }
    }

    @Override
    public T add(int x, int y, T val) {
        if (x < root.getX() || y < root.getY() || x > root.getX() + root.getWidth() || y > root.getY() + root.getHeight()) {
            return null;
        }
        Point<T> addingPoint = new Point<>(x, y, val);
        boolean added = insert(root, addingPoint);
        if (added) {
            size++;
            return val;
        }
        return null;
    }

    private boolean insert(QuadNode<T> node, Point<T> point) {
        boolean added = false;
        if (node.getPoint() == null && node.getNorthWest() == null) {
            setPointInNode(node, point);
            added = true;
        } else if (node.getNorthWest() == null) {
            if (node.getPoint().getX() == point.getX() && node.getPoint().getY() == point.getY()) {
                setPointInNode(node, point);
            } else {
                separation(node);
                added = insert(node, point);
            }
        } else if (node.getNorthWest() != null) {
            added = insert(getNodeForPoint(node, point.getX(), point.getY()), point);
        }
        return added;
    }

    private QuadNode<T> getNodeForPoint(QuadNode<T> parent, int x, int y) {
        int borderX = parent.getX() + parent.getWidth() / 2;
        int borderY = parent.getY() + parent.getHeight() / 2;
        if (x < borderX) {
            return y < borderY ? parent.getNorthWest() : parent.getSouthWest();
        } else {
            return y < borderY ? parent.getNorthEast() : parent.getSouthEast();
        }
    }

    private void setPointInNode(QuadNode<T> quadNode, Point<T> point) {
        if (quadNode.getNorthWest() != null) {
            return;
        }
        quadNode.setPoint(point);
    }

    private void separation(QuadNode<T> quadNode) {
        Point<T> point = quadNode.getPoint();
        quadNode.setPoint(null);
        int x = quadNode.getX();
        int y = quadNode.getY();
        int halfWidth = quadNode.getWidth() / 2;
        int halfHeight = quadNode.getHeight() / 2;
        quadNode.setNorthWest(new QuadNode<>(x, y, halfWidth, halfHeight, quadNode));
        quadNode.setNorthEast(new QuadNode<>(x + halfWidth, y, halfWidth, halfHeight, quadNode));
        quadNode.setSouthWest(new QuadNode<>(x, y + halfHeight, halfWidth, halfHeight, quadNode));
        quadNode.setSouthEast(new QuadNode<>(x + halfWidth, y + halfHeight, halfWidth, halfHeight, quadNode));
        insert(quadNode, point);
    }

    @Override
    public T get(int x, int y) {
        QuadNode<T> node = find(root, x, y);
        return node != null ? node.getPoint().getValue() : null;
    }


    @Override
    public T remove(int x, int y) {
        QuadNode<T> node = find(root, x, y);
        if (node != null) {
            T value = node.getPoint().getValue();
            node.setPoint(null);
            size--;
            return value;
        } else {
            return null;
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return get(x, y) != null;
    }

    @Override
    public boolean isEmpty() {
        return root.getNorthWest() == null && root.getPoint() == null;
    }

    @Override
    public List<T> getValues() {
        if (size == 0) {
            return Collections.emptyList();
        }
        final List<T> resultList = new ArrayList<>();
        visit(root, (quadNode) -> resultList.add(quadNode.getPoint().getValue()));
        return resultList;
    }

    @Override
    public List<String> getPoints() {
        if (size == 0) {
            return Collections.emptyList();
        }
        final List<String> resultList = new ArrayList<>();
        visit(root, (quadNode) -> resultList.add(quadNode.getPoint().toString()));
        return resultList;
    }

    private void visit(QuadNode<T> quadNode, Consumer<QuadNode<T>> consumer) {
        if (quadNode.getPoint() != null) {
            consumer.accept(quadNode);
        } else if (quadNode.getNorthWest() != null) {
            visit(quadNode.getNorthWest(), consumer);
            visit(quadNode.getNorthEast(), consumer);
            visit(quadNode.getSouthEast(), consumer);
            visit(quadNode.getSouthWest(), consumer);
        }
    }

    private QuadNode<T> find(QuadNode<T> node, int x, int y) {
        QuadNode<T> result = null;
        if (node.getPoint() != null && node.getNorthWest() == null) {
            result = node.getPoint().getX() == x && node.getPoint().getY() == y ? node : null;
        } else if (node.getNorthWest() != null) {
            result = find(getNodeForPoint(node, x, y), x, y);
        }
        return result;
    }

    @Override
    public Iterator<T> iterator() {
        List<T> list = new LinkedList<>();
        visit(root, quadNode -> list.add(quadNode.getPoint().getValue()));
        return list.iterator();
    }
}

