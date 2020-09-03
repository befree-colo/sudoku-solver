package com.befreeman.sudoku.sudokusolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = SudokuSolverController.class)
public class SudokuSolverControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SudokuService mockSudokuService;

    @Test
    void whenValidInput_thenReturns200() throws Exception {
        SudokuPuzzle testPuzzle = new SudokuPuzzle();
        Mono<SudokuPuzzle> testPuzzleMono = Mono.just(testPuzzle);
        when(mockSudokuService.solvePuzzle(testPuzzle)).thenReturn(testPuzzleMono);

        webTestClient.post().uri(SudokuSolverController.PUZZLE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(testPuzzle), SudokuPuzzle.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SudokuPuzzle.class)
                .isEqualTo(testPuzzleMono.block(Duration.ofSeconds(1)));

        verify(mockSudokuService).solvePuzzle(testPuzzle);
    }
}
