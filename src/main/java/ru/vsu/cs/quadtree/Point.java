package ru.vsu.cs.quadtree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
final class Point<T> {
    private int x;
    private int y;
    private T value;

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
