package sudoku;
import java.awt.*;
import javax.swing.*;
/**
 * The main Sudoku program
 */
public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("New Game");
    private JButton btnHint = new JButton("Get Hint");

    // Constructor
    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        // Add a button to the south to re-start the game via board.newGame()
        // ......
        JPanel controlPanel = new JPanel();
        controlPanel.add(btnHint);
        controlPanel.add(btnNewGame);
        cp.add(controlPanel, BorderLayout.SOUTH);
        // Initialize the game board to start the game
        board.newGame();

        btnHint.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter cell (row,col) for hint (1-9,1-9):");
            if (input != null && input.matches("\\d,\\d")) {
                String[] parts = input.split(",");
                int row = Integer.parseInt(parts[0]) - 1;
                int col = Integer.parseInt(parts[1]) - 1;

                if (!board.provideHint(row, col)) {
                    JOptionPane.showMessageDialog(this, "Hint could not be applied!");
                }
            }
        });

        btnNewGame.addActionListener(e -> board.newGame());

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    /** The entry main() entry method */
    public static void main(String[] args) {
        // [TODO 1] Check "Swing program template" on how to run
        //  the constructor of "SudokuMain"
        // .........
        SudokuMain t = new SudokuMain();
    }
}