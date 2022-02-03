package com.tsystems.javaschool.tasks.pyramid;

import java.util.*;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        if (!canBuild(inputNumbers)) throw new CannotBuildPyramidException();

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> mutableInput = new ArrayList<>(inputNumbers);

        mutableInput.sort(Comparator.comparingInt(integer -> integer));

        for (int i = 1; !mutableInput.isEmpty(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < i && !mutableInput.isEmpty(); j++) {
                row.add(mutableInput.get(0));
                mutableInput.remove(0);

                //insert zeros between digits
                if (j != i - 1)
                    row.add(0);
            }
            result.add(row);
        }

        //if the bottom row is smaller than the penultimate row
        int bottomRowWidth = result.get(result.size() - 1).size();
        int penultimateRowWidth = result.get(result.size() - 2).size();
        if (bottomRowWidth - penultimateRowWidth != 2)
            throw new CannotBuildPyramidException();

        for (List<Integer> row : result)
            addZeros(row, bottomRowWidth);

        return listToArray(result);
    }

    private int[][] listToArray(List<List<Integer>> list) {
        int[][] intArray = new int[list.size()][];
        for (int i = 0; i < intArray.length; i++)
            intArray[i] = list.get(i).stream().mapToInt(Integer::intValue).toArray();

        return intArray;
    }

    private void addZeros(List<Integer> row, int width) {
        int numberAdditionalZeros = width - row.size();
        for (int i = 0; i < numberAdditionalZeros / 2; i++) {
            row.add(0, 0);
            row.add(0);
        }
    }

    private boolean canBuild(List<Integer> inputNumbers) {
        if (inputNumbers.contains(null))
            throw new CannotBuildPyramidException();

        int rows = 1;
        int numbers = inputNumbers.size();
        while (numbers >= rows)
            numbers -= rows++;

        if (numbers != 0)
            throw new CannotBuildPyramidException();

        return true;
    }
}
