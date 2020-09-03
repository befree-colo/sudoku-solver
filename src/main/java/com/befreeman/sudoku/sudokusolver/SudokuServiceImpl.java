package com.befreeman.sudoku.sudokusolver;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Component
public class SudokuServiceImpl implements SudokuService {

    @Override
    public Mono<SudokuPuzzle> solvePuzzle(SudokuPuzzle sudokuPuzzle) {
        int[][] grid = sudokuPuzzle.getGrid();
        boolean done = false;
        int loopCount = 1;
        while (!done) {
            boolean foundASolution = false;

            System.out.println("loopCount = " + loopCount++);

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (grid[i][j] == 0) {
                        // then not solved yet

                        Set<Integer> possibleNumbers = new HashSet<>(9);
                        for (int x = 1; x <= 9; x++) {
                            possibleNumbers.add(x);
                        }
//                        System.out.println("possibleNumbers begin: " + possibleNumbers);


                        algorithms.forEach(alg -> alg.run(i, j, sudokuPuzzle.getGrid(), possibleNumbers));

                        removeNumbersFoundInBlock(i, j, sudokuPuzzle.getGrid(), possibleNumbers);
//                        System.out.println("possibleNumbers after removeNumbersFoundInBlock: " + possibleNumbers);

                        removeNumbersFoundInRow(i, sudokuPuzzle.getGrid(), possibleNumbers);

//                        System.out.println("possibleNumbers after removeNumbersFoundInRow: " + possibleNumbers);

                        removeNumbersFoundInColumn(j, sudokuPuzzle.getGrid(), possibleNumbers);

//                        System.out.println("possibleNumbers after removeNumbersFoundInColumn: " + possibleNumbers);

                        if (possibleNumbers.size() == 1) {
                            int match = possibleNumbers.iterator().next();
                            // we found a value
                            System.out.println("a) Found a value at [" + i + "][" + j + "] = " + match);

                            grid[i][j] = match;
                            foundASolution = true;
                        } else {
                            // for each remaining number
                            // look at the other rows in the block to see if they have this number
                            // if they do then this row must have that number in it
                            // we know the number isn't in this block because we already checked for it
                            // so, look at the other columns to see if they have this number too
                            // if they both have this number, then the number must go in the current cell
                            // NOTE: It is possible the other rows or columns in this block are full
                            //   and can't take this number either, so we can look at that algorithm too

                            for (Integer lookForNum : possibleNumbers) {

                                int[] otherRowCoordinates = getOtherRowColCoordinates(i);
                                int[] otherColCoordinates = getOtherRowColCoordinates(j);

                                if (isNumberInRow(lookForNum, otherRowCoordinates[0], grid) > 0 &&
                                        isNumberInRow(lookForNum, otherRowCoordinates[1], grid) > 0 &&
                                        isNumberInCol(lookForNum, otherColCoordinates[0], grid) > 0 &&
                                        isNumberInCol(lookForNum, otherColCoordinates[1], grid) > 0) {
                                    System.out.println("b) Found a value at [" + i + "][" + j + "] = " + lookForNum);
                                    grid[i][j] = lookForNum;
                                    foundASolution = true;
                                    break;
                                }
                            }


                        }
                    }
                }
            }

            if (!foundASolution) {
                // then we are stumped and can't solve it
                done = true;
                System.out.println("stumped");
            }
        }

        return Mono.just(new SudokuPuzzle(grid));
    }

    int[] getOtherRowColCoordinates(int position) {
        switch (position) {
            case 0:
                return new int[]{1, 2};
            case 1:
                return new int[]{0, 2};
            case 2:
                return new int[]{0, 1};
            case 3:
                return new int[]{4, 5};
            case 4:
                return new int[]{3, 5};
            case 5:
                return new int[]{3, 4};
            case 6:
                return new int[]{7, 8};
            case 7:
                return new int[]{6, 8};
            case 8:
                return new int[]{6, 7};
            default:
                throw new IllegalArgumentException("position must be >= 0 and <= 8");
        }
    }

    int isNumberInRow(int lookForNum, int row, int[][] grid) {
        for (int j = 0; j < 9; j++) {
            if (grid[row][j] == lookForNum) {
                return j;
            }
        }
        return -1;
    }

    int isNumberInCol(int lookForNum, int col, int[][] grid) {
        for (int i = 0; i < 9; i++) {
            if (grid[i][col] == lookForNum) {
                return i;
            }
        }
        return -1;
    }

    void removeNumbersFoundInBlock(int row, int col, int[][] grid, Set<Integer> possibleNumbers) {
        //0 - 2, 3 - 5, 6 - 8
        int rowStart = (row / 3) * 3;
        int colStart = (col / 3) * 3;

        for (int i = rowStart; i < (rowStart + 3); i++) {
            for (int j = colStart; j < (colStart + 3); j++) {
                if (grid[i][j] != 0) {
                    possibleNumbers.remove(grid[i][j]);
                }
            }
        }
    }

    void removeNumbersFoundInRow(int row, int[][] grid, Set<Integer> possibleNumbers) {
        for (int j = 0; j < 8; j++) {
            if (grid[row][j] != 0) {
                possibleNumbers.remove(grid[row][j]);
            }
        }
    }

    void removeNumbersFoundInColumn(int col, int[][] grid, Set<Integer> possibleNumbers) {
        for (int i = 0; i < 8; i++) {
            if (grid[i][col] != 0) {
                possibleNumbers.remove(grid[i][col]);
            }
        }
    }
}
