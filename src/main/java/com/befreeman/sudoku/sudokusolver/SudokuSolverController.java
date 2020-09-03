package com.befreeman.sudoku.sudokusolver;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class SudokuSolverController {
    protected static final String PUZZLE_PATH = "/sudoku-puzzle";

    private final SudokuService sudokuService;

    public SudokuSolverController(SudokuService sudokuService) {
        this.sudokuService = sudokuService;
    }

    @PostMapping(value = PUZZLE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SudokuPuzzle>> createPuzzleToSolve(@RequestBody SudokuPuzzle sudokuPuzzle) {
        log.info("createPuzzleToSolve: {}", sudokuPuzzle);
        return sudokuService.solvePuzzle(sudokuPuzzle)
                .map(puzzleId -> new ResponseEntity<>(puzzleId, HttpStatus.CREATED));
    }
}
