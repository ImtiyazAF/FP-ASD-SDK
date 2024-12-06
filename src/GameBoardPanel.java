package sudoku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning
    private int hintsUsed = 0;
    private static final int MAX_HINTS = 3;

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels
    // Define properties
    /**
     * The game board composes of 9x9 Cells (customized JTextFields)
     */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /**
     * It also contains a Puzzle with array numbers and isGiven
     */
    private Puzzle puzzle = new Puzzle();

    private Timer gameTimer;
    private int elapsedTime = 0;
    private int score = 100;
    private int mistakes = 0;
    private JProgressBar progressBar;

    /**
     * Constructor
     */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));// JPanel

        // Allocate the 2D array of Cell, and added into JPanel.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);   // JPanel
            }
        }


        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        this.add(progressBar, BorderLayout.SOUTH);


        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
        CellInputListener listener = new CellInputListener();

        // [TODO 4] Adds this common listener to all editable cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);   // For all editable rows and cols
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    public int getElapsedTime() {
        return elapsedTime;
    }


    public void startTimer() {
        gameTimer = new Timer(1000, e -> {
            elapsedTime++;
        });
        gameTimer.start();
    }

    public void stopTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }

    }

    public void setProgressBarAndTimer() {
        // Inisialisasi JProgressBar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH);

        // Inisialisasi Timer
        gameTimer = new Timer(1000, e -> updateTimer());
    }

    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     */
    public void newGame() {
        // Generate a new puzzle
        puzzle.newPuzzle(2);

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }

        elapsedTime = 0;
        mistakes = 0;
        score = 100;

        setProgressBarAndTimer();

        // Mulai Timer
        gameTimer.start();

        // Perbarui progress
        progressBar.setValue(getProgress());
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

        return (int) ((filledCells / 81.0) * 100);
    }

    private void updateScore() {
        int timePenalty = 0;
        if (score < 0) score = 0;

        if (elapsedTime <= 30) timePenalty = 0;
        else if (elapsedTime <= 60) timePenalty = 5;
        else if (elapsedTime <= 90) timePenalty = 10;
        else if (elapsedTime <= 120) timePenalty = 15;
        else if (elapsedTime <= 300) timePenalty = 20 + (elapsedTime - 120) / 30 * 5;
        else timePenalty = 60;

        score = 100 - timePenalty - (mistakes * 2);
        score = Math.max(score, 40); // Minimal skor adalah 40
    }




    public int getScore() {
        return score;
    }

    public void calculateScore() {
        if (elapsedTime < 30) {
            score = 100;
        } else if (elapsedTime < 60) {
            score = 95;
        } else if (elapsedTime < 90) {
            score = 90;
        } else if (elapsedTime < 120) {
            score = 85;
        } else if (elapsedTime < 150) {
            score = 80;
        } else if (elapsedTime < 180) {
            score = 75;
        } else if (elapsedTime < 200) {
            score = 70;
        } else if (elapsedTime < 300) {
            score = 55 + (250 - elapsedTime) / 50 * 5;
        } else {
            score = 40;
        }
    }

    public void deductPointsForWrongGuess() {
        score -= 2;
        if (score < 0) score = 0;
    }


    public void updateTimer() {
        elapsedTime++;
        calculateScore();
        repaint();
        updateScore();
        progressBar.setValue(getProgress());


        if (progressBar != null) {
            progressBar.setValue(getProgress());
        }
    }


        /**
         * Return true if the puzzle is solved
         * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
         */
        public boolean isSolved () {
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
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference of the JTextField that triggers this action event
            Cell sourceCell = (Cell)e.getSource();

            // Retrieve the int entered
            int numberIn = Integer.parseInt(sourceCell.getText());
            // For debugging
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
            boolean win = isSolved();
            if (win == true) JOptionPane.showMessageDialog(null, "Congratulation!");
            gameTimer.stop(); // Hentikan timer
        }
    }


    private void updateProgress(int value) {
        int progress = getProgress();
        progressBar.setValue(value);
        progressBar.repaint();
        int correctCells = 0;
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.CORRECT_GUESS) {
                    correctCells++;
                }
            }
        }

        progressBar.setValue(progress);
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
        cells[row][col].paint();

        return true;
    }

}