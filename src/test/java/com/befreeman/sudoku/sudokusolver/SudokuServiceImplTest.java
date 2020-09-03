package com.befreeman.sudoku.sudokusolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SudokuServiceImpl.class})
public class SudokuServiceImplTest {

    private static final int[][] testGrid1 = new int[][]{
            {0, 9, 7, 0, 0, 6, 4, 0, 0},
            {0, 0, 0, 0, 4, 1, 0, 0, 0},
            {0, 4, 5, 0, 2, 3, 0, 0, 7},
            {0, 0, 6, 0, 0, 0, 0, 0, 0},
            {9, 2, 0, 0, 7, 0, 0, 1, 4},
            {5, 0, 1, 3, 0, 0, 7, 0, 0},
            {6, 0, 0, 5, 8, 0, 1, 7, 0},
            {0, 0, 0, 1, 3, 0, 0, 0, 0},
            {0, 0, 2, 4, 0, 0, 5, 3, 0}
    };

    private static final int[][] testGrid1Solution = new int[][]{
            {3, 9, 7, 8, 5, 6, 4, 2, 1},
            {2, 6, 8, 7, 4, 1, 9, 5, 3},
            {1, 4, 5, 9, 2, 3, 6, 8, 7},
            {4, 7, 6, 2, 1, 8, 3, 9, 5},
            {9, 2, 3, 6, 7, 5, 8, 1, 4},
            {5, 8, 1, 3, 9, 4, 7, 6, 2},
            {6, 3, 4, 5, 8, 2, 1, 7, 9},
            {8, 5, 9, 1, 3, 7, 2, 4, 6},
            {7, 1, 2, 4, 6, 9, 5, 3, 8}
    };

    @Autowired
    private SudokuServiceImpl sudokuService;

    @Test
    void verifySubmitPuzzleReturnsTrackingId() throws Exception {
        SudokuPuzzle testPuzzle = new SudokuPuzzle(testGrid1);
        SudokuPuzzle solvedPuzzle = new SudokuPuzzle(testGrid1Solution);
        final Mono<SudokuPuzzle> response = sudokuService.solvePuzzle(testPuzzle);
        assertNotNull(response);
        SudokuPuzzle puzzleResponse = response.block(Duration.ofSeconds(1));
        assertNotNull(puzzleResponse, "Missing solved puzzle");
        assertEquals(solvedPuzzle, puzzleResponse);
    }

    @Test
    void testIsNumberInRow() {
        assertEquals(1, sudokuService.isNumberInRow(9, 0, testGrid1));
        assertEquals(2, sudokuService.isNumberInRow(6, 3, testGrid1));
        assertEquals(7, sudokuService.isNumberInRow(3, 8, testGrid1));
        assertEquals(-1, sudokuService.isNumberInRow(2, 7, testGrid1));
    }

    @Test
    void testIsNumberInCol() {
        assertEquals(5, sudokuService.isNumberInCol(5, 0, testGrid1));
        assertEquals(6, sudokuService.isNumberInCol(8, 4, testGrid1));
        assertEquals(8, sudokuService.isNumberInCol(3, 7, testGrid1));
        assertEquals(-1, sudokuService.isNumberInCol(2, 7, testGrid1));
    }

    @Test
    void testRemoveNumbersFoundInBlock() {
        Set<Integer> testSet = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Set<Integer> expected = Set.of(1, 2, 3, 6, 8);

        for (int i = 0; i < 3; i++ ) {
            for (int j = 0; j < 3; j++) {
                HashSet<Integer> test1 = new HashSet<>(testSet);
                sudokuService.removeNumbersFoundInBlock(i, j, testGrid1, test1);
                assertEquals(expected, test1, "Didn't match with [" + i + "][" + j + "]");
            }
        }
    }

    @Test
    void testGetOtherRowColCoordinates() {
        assertArrayEquals(new int[] {1,2}, sudokuService.getOtherRowColCoordinates(0), "position 0");
        assertArrayEquals(new int[] {0,2}, sudokuService.getOtherRowColCoordinates(1), "position 1");
        assertArrayEquals(new int[] {0,1}, sudokuService.getOtherRowColCoordinates(2), "position 2");
        assertArrayEquals(new int[] {4,5}, sudokuService.getOtherRowColCoordinates(3), "position 3");
        assertArrayEquals(new int[] {3,5}, sudokuService.getOtherRowColCoordinates(4), "position 4");
        assertArrayEquals(new int[] {3,4}, sudokuService.getOtherRowColCoordinates(5), "position 5");
        assertArrayEquals(new int[] {7,8}, sudokuService.getOtherRowColCoordinates(6), "position 6");
        assertArrayEquals(new int[] {6,8}, sudokuService.getOtherRowColCoordinates(7), "position 7");
        assertArrayEquals(new int[] {6,7}, sudokuService.getOtherRowColCoordinates(8), "position 8");
    }
}
