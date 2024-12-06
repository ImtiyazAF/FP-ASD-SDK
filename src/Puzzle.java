package sudoku;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The Sudoku number puzzle to be solved
 */

public class Puzzle {
    // All variables have package access
    // The numbers on the puzzle
    int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    // The clues - isGiven (no need to guess) or need to guess
    boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];

    // Constructor
    public Puzzle() {
        super();
    }

    // Generate a new puzzle given the number of cells to be guessed, which can be used
    //  to control the difficulty level.
    // This method shall set (or update) the arrays numbers and isGiven
    public void newPuzzle(int cellsToGuess) {
        generateBoard();
        generatePuzzle(cellsToGuess);
    }

    private boolean isValidMove(int row, int col, int num){
        for(int i = 0; i < SudokuConstants.GRID_SIZE; ++i){
            if(numbers[row][i] == num || numbers[i][col] == num){
                return false;
            }
        }

        int startRow = (row/3) * 3;
        int startCol = (col/3) * 3;
        for(int i = startRow; i < startRow + 3; ++i){
            for(int j = startCol; j < startCol + 3; ++j){
                if(numbers[i][j] == num){
                    return false;
                }
            }
        }
        return true;
    }

    private List<Integer> getPossibleNumbers(int row, int col){
        Set<Integer> possibleNumbers = new HashSet<>();
        for(int num =1; num <= 9; num++){
            if(isValidMove(row, col, num)){
                possibleNumbers.add(num);
            }
        }
        return new ArrayList<>(possibleNumbers);
    }

    private boolean fillBoard(){
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if(numbers[row][col] == 0){
                    List<Integer> possibleNumbers = getPossibleNumbers(row,col);
                    Collections.shuffle(possibleNumbers);

                    for(int num : possibleNumbers){
                        numbers[row][col] = num;
                        if(fillBoard()){
                            return true;
                        }
                        numbers[row][col] = 0;
                    }

                    return false;
                }
            }
        }
        return true;
    }

    private void generateBoard(){
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for(int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                numbers[row][col] = 0;
            }
        }

        if(!fillBoard()){
            System.out.println("Failed to generate a valid Sudoku solution");
        }
    }

    private void generatePuzzle(int cellsToGuess){
        for(int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for(int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                isGiven[row][col] = true;
            }
        }

        Random rand = new Random();
        while (cellsToGuess > 0){
            int row = rand.nextInt(SudokuConstants.GRID_SIZE);
            int col = rand.nextInt(SudokuConstants.GRID_SIZE);

            if(isGiven[row][col]){
                isGiven[row][col] = false;
                cellsToGuess--;
            }
        }
    }
}