package com.befreeman.sudoku.sudokusolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class SudokuSolverApplication {

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        SpringApplication.run(SudokuSolverApplication.class, args);
        log.info("Starting SudokuSolverApplication: args={}", Arrays.toString(args));
    }

}
