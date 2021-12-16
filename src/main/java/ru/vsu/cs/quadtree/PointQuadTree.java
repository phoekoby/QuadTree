package ru.vsu.cs.quadtree;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;


@Getter
public final class PointQuadTree<T> implements QuadTree<T> {
    private final QuadNode<T> root;
    private int size = 0;

    public PointQuadTree(double minX, double minY, double maxX, double maxY) {
        this.root = new QuadNode<>(minX, minY, maxX - minX, maxY - minY, null);
    }

    @Override
    public Point<T> add(double x, double y, T val) {
        if (x < root.getX() || y < root.getY() || x > root.getX() + root.getWidth() || y > root.getY() + root.getHeight()) {
            return null;
        }
        Point<T> addingPoint = new Point<>(x, y, val);
        boolean added = insert(root, addingPoint);
        if (added) {
            size++;
            return addingPoint;
        }
        return null;
    }

    private boolean insert(QuadNode<T> parent, Point<T> point) {
        boolean added = false;
        if (parent.getPoint() == null && parent.getNorthWest() == null) {
            setPointInNode(parent, point);
            size++;
            added = true;
        } else if (parent.getNorthWest() == null) {
            if (parent.getPoint().getX() == point.getX() && parent.getPoint().getY() == point.getY()) {
                setPointInNode(parent, point);
            } else {
                separation(parent);
                added = insert(parent, point);
            }
        } else if (parent.getNorthWest() != null) {
            added = insert(getNodeForPoint(parent, point.getX(), point.getY()), point);
        }
        return added;
    }

    private QuadNode<T> getNodeForPoint(QuadNode<T> parent, double x, double y) {
        double minX = parent.getX() + parent.getWidth() / 2;
        double minY = parent.getY() + parent.getHeight() / 2;
        if (x < minX) {
            return y < minY ? parent.getNorthWest() : parent.getSouthWest();
        } else {
            return y < minY ? parent.getNorthEast() : parent.getSouthEast();
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
        double x = quadNode.getX();
        double y = quadNode.getY();
        double halfWidth = quadNode.getWidth() / 2;
        double halfHeight = quadNode.getHeight() / 2;
        quadNode.setNorthWest(new QuadNode<>(x, y, halfWidth, halfHeight, quadNode));
        quadNode.setNorthEast(new QuadNode<>(x + halfWidth, y, halfWidth, halfHeight, quadNode));
        quadNode.setSouthWest(new QuadNode<>(x, y + halfHeight, halfWidth, halfHeight, quadNode));
        quadNode.setSouthEast(new QuadNode<>(x + halfWidth, y + halfHeight, halfWidth, halfHeight, quadNode));
        insert(quadNode, point);
    }

    @Override
    public T get(double x, double y) {
        QuadNode<T> node = find(this.root, x, y);
        return node != null ? node.getPoint().getValue() : null;
    }

    @Override
    public T remove(double x, double y) {
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
    public boolean contains(double x, double y) {
        return get(x, y) != null;
    }

    @Override
    public boolean isEmpty() {
        return root.getNorthWest() == null && root.getPoint() == null;
    }

    @Override
    public List<T> getValues() {
        if(size==0){
            return Collections.emptyList();
        }
        final List<T> resultList = new ArrayList<>();
        visitor(root, (quadNode) -> resultList.add(quadNode.getPoint().getValue()));
        return resultList;
    }
    @Override
    public List<String> getPoints() {
        if(size==0){
            return Collections.emptyList();
        }
        final List<String> resultList = new ArrayList<>();
        visitor(root, (quadNode) -> resultList.add(quadNode.getPoint().toString()));
        return resultList;
    }

    private void visitor(QuadNode<T> quadNode, Consumer<QuadNode<T>> consumer) {
        if (quadNode.getPoint() != null) {
            consumer.accept(quadNode);
        } else if (quadNode.getNorthWest() != null) {
            visitor(quadNode.getNorthWest(), consumer);
            visitor(quadNode.getNorthEast(), consumer);
            visitor(quadNode.getSouthEast(), consumer);
            visitor(quadNode.getSouthWest(), consumer);
        }
    }

    @Override
    public QuadNode<T> find(QuadNode<T> node, double x, double y) {
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
        Queue<QuadNode<T>> queue = new LinkedList<>();
        visitor(root, queue::offer);
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return !queue.isEmpty();
            }

            @Override
            public T next() {
                QuadNode<T> node = queue.poll();
                return node != null ? node.getPoint().getValue() : null;
            }
        };

    }
}

