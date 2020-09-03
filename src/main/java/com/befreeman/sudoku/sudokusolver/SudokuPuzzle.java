package com.befreeman.sudoku.sudokusolver;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class SudokuPuzzle {
    int[][] grid;

    public SudokuPuzzle() {
        grid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = 0;
            }
        }
    }

    public SudokuPuzzle(final int[][] grid) {
        this.grid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.grid[i][j] = grid[i][j];
            }
        }
    }

    /**
     * @return A clone of the grid
     */
    public int[][] getGrid() {
        int[][] response = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                response[i][j] = grid[i][j];
            }
        }
        return response;
    }

    public String toString() {

        // ┌ ┐ ─ │ └ ┘ ├ ┤ ┬ ┴ ┼
        StringBuilder sb = new StringBuilder("\n┌─────┬─────┬─────┐\n");
        for (int i = 0; i < 9; i++) {
            sb.append("│");
            for (int j = 0; j < 9; j++) {
                sb.append(grid[i][j]);
                // 2, 5, 8
                if (j == 2 || j == 5 || j == 8) {
                    // box line
                    sb.append("│");
                } else {
                    // pipe char
                    sb.append("|");
                }
            }
            if (i < 8) {
                sb.append("\n├─────┼─────┼─────┤\n");
            } else {
                sb.append("\n└─────┴─────┴─────┘\n");
            }
        }
        return sb.toString();
    }
}