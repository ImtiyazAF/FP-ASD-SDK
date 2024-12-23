/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231136 - Maulana Muhammad Ad-Dzikri
 * 2 - 5026231172 - Mochamad Zhulmi Danovanz H
 * 3 - 5026231197- Imtiyaz Shafhal Afif
 */
package sudoku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning
    private int hintsUsed = 0;  // Jumlah hint yang telah digunakan
    private static final int MAX_HINTS = 3;  // Batas maksimum hint

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int BOARD_WIDTH  = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels
    // Define properties
    /** The game board composes of 9x9 Cells (customized JTextFields) */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /** It also contains a Puzzle with array numbers and isGiven */
    private Puzzle puzzle = new Puzzle();

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));// JPanel

        // Allocate the 2D array of Cell, and added into JPanel.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);   // JPanel
            }
        }


        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the

        //DocumentListener listener = new DocumentListener();

        // [TODO 4] Adds this common listener to all editable cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].getDocument().addDocumentListener(new CellInputListener(cells[row][col]));   // For all editable rows and cols
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     */
    public void newGame(int diffInt) {
        // Generate a new puzzle
        if(diffInt == 0){
            puzzle.newPuzzle(5);
        } else if (diffInt == 1){
            puzzle.newPuzzle(10);
        } else{
            puzzle.newPuzzle(15);
        }

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col],puzzle.isGiven[row][col]);
                cells[row][col].status = puzzle.isGiven[row][col] ? CellStatus.CORRECT_GUESS : CellStatus.TO_GUESS;

            }
        }

    }


    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    // [TODO 2] Define a Listener Inner Class for all the editable Cells
    private class CellInputListener implements DocumentListener {
        private Cell sourceCell;

        public CellInputListener(Cell sourceCell) {
            this.sourceCell = sourceCell;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {

            handleTextChange();
//            boolean win = isSolved();
//            if (win == true) JOptionPane.showMessageDialog(null, "Congratulation!");
        }

        @Override
        public void removeUpdate(DocumentEvent e) {

            handleTextChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            handleTextChange();
        }

        private void handleTextChange() {
            if (sourceCell.status != CellStatus.TO_GUESS && sourceCell.status != CellStatus.WRONG_GUESS && sourceCell.status != sudoku.CellStatus.CORRECT_GUESS) {
                return; // Ignore changes in cells that are not TO_GUESS
            }
            // Retrieve the current text from the Cell (inherited from JTextField)
            String inputText = sourceCell.getText();

            try {
                // Parse the input text as an integer
                int numberIn = Integer.parseInt(inputText.trim());


                // For debugging or further processing
                System.out.println("You entered " + numberIn);

                /*
                 * [TODO 5] (later - after TODO 3 and 4)
                 * Check the numberIn against sourceCell.number.
                 * Update the cell status sourceCell.status,
                 * and re-paint the cell via sourceCell.paint().
                 */
                if (numberIn == sourceCell.number) {
                    sourceCell.status = CellStatus.CORRECT_GUESS;
                } else {
                    sourceCell.status = CellStatus.WRONG_GUESS;
                }
                sourceCell.paint();   // re-paint this cell based on its status

                /*
                 * [TODO 6] (later)
                 * Check if the player has solved the puzzle after this move,
                 *   by calling isSolved(). Put up a congratulation JOptionPane, if so.
                 */

                if (isSolved()) {
                    JOptionPane.showMessageDialog(null, "Congratulations!");
                }

            } catch (NumberFormatException e) {
                // Handle invalid input (e.g., non-integer values)
                System.out.println("Invalid input: " + inputText);
            }


        }
    }

    public int getProgress() {
        int filledCells = 0;
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.CORRECT_GUESS) {
                    filledCells++;
                }
            }
        }
        return (filledCells * 100) / (SudokuConstants.GRID_SIZE * SudokuConstants.GRID_SIZE);
    }


    //HINTS
    public boolean provideHint(int row, int col) {
        if (hintsUsed >= MAX_HINTS) {
            JOptionPane.showMessageDialog(null, "Maximum hints used!");
            return false; // Max hints reached
        }

        if (row < 0 || row >= SudokuConstants.GRID_SIZE || col < 0 || col >= SudokuConstants.GRID_SIZE) {
            JOptionPane.showMessageDialog(null, "Invalid cell coordinates!");
            return false;
        }

        if (puzzle.isGiven[row][col] || cells[row][col].status == CellStatus.HINTED) {
            JOptionPane.showMessageDialog(null, "Hint cannot be applied to this cell!");
            return false; // Hint cannot be applied
        }

        hintsUsed++;

        // Update cell with hint
        cells[row][col].status = CellStatus.HINTED;
        cells[row][col].setText(String.valueOf(puzzle.numbers[row][col])); // Reveal number
        cells[row][col].setEditable(false);
        cells[row][col].setBackground(Color.YELLOW);

        return true;
    }
    private boolean isColumnCorrect(int col) {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            if (cells[row][col].status != CellStatus.CORRECT_GUESS) {
                return false;
            }
        }
        return true;
    }

    public void resetGameBoard() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (!puzzle.isGiven[row][col]) {
                    cells[row][col].clear();
                }
            }
        }
    }

}