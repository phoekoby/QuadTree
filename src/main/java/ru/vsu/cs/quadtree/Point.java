package ru.vsu.cs.quadtree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
final class Point<T> implements Comparable<Point<T>> {
    private double x;
    private double y;
    private T value;

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
    @Override
    public int compareTo(Point<T> point) {
        if (this.x < point.x) {
            return -1;
        } else if (this.x > point.x) {
            return 1;
        } else {
            if (this.y < point.y) {
                return -1;
            } else if (this.y > point.y) {
                return 1;
            }
            return 0;
        }
    }
}
