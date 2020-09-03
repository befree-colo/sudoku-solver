package com.befreeman.sudoku.sudokusolver;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

public interface SudokuService {
    /**
     * Submit an unsolved puzzle to be solved
     *
     * @param sudokuPuzzle An unsolved puzzle
     *
     * @return A tracking ID to get the solve status of the puzzle
     */
    Mono<SudokuPuzzle> solvePuzzle(SudokuPuzzle sudokuPuzzle);
}
