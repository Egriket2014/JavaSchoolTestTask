package com.tsystems.javaschool.tasks.subsequence;

import java.util.LinkedList;
import java.util.List;


public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        if (x == null || y == null) throw new IllegalArgumentException();
        if (x.isEmpty()) return true;
        if (y.size() < x.size()) return false;

        List listY = new LinkedList<>(y);
        if (y.containsAll(x)) {
            int indexInY = 0, offset = 0;
            for (Object o : x) {
                if (indexInY + offset - 1 > listY.indexOf(o) + offset)
                    return false;

                indexInY = listY.indexOf(o);
                listY.remove(o);
                offset++;
            }
            return true;
        }

        return false;
    }
}
