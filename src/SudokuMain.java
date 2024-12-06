package sudoku;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * The main Sudoku program
 */

public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning
    public static JMenuBar menuB;
    public static JMenu menu;
    public static JMenuItem m1, m2, m3;
    private CardLayout page;
    private JButton start, btnNewGame, btnHint;

    private GameBoardPanel board;
    Container cp = getContentPane();

    CellInputListener listener = new CellInputListener();

    JPanel mainmenu;

    public SudokuMain() {
        page = new CardLayout();
        mainmenu = new JPanel(page);
        JPanel mainMenu = MainMenu();
        JPanel gameMenu = GameMenu();

        mainmenu.add(mainMenu, "mainMenu");
        mainmenu.add(gameMenu, "gameMenu");

        cp.setLayout(new BorderLayout());

        cp.add(mainmenu, BorderLayout.CENTER);


        // Add a button to the south to re-start the game via board.newGame()
        // ......
        // Initialize the game board to start the game

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    JPanel MainMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        start = new JButton("Start");
        start.addActionListener(listener);
        panel.add(start, BorderLayout.SOUTH);

        return panel;
    }

    JPanel GameMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        board = new GameBoardPanel();

        menuB = new JMenuBar();
        // create a menu
        menu = new JMenu("Menu");
        JMenu file = new JMenu("File");
        // create menuitems
        m1 = new JMenuItem("MenuItem1");
        m2 = new JMenuItem("MenuItem2");
        m3 = new JMenuItem("MenuItem3");

        // add menu items to menu
        menu.add(m1);
        menu.add(m2);
        menu.add(m3);
        // add menu to menu bar
        menuB.add(menu);
        menuB.add(file);

        JPanel menubar = new JPanel(new GridLayout());
        JPanel gameboard = new JPanel();

        menubar.add(menuB);

        JPanel controlPanel = new JPanel();
        btnNewGame = new JButton("New Game");
        btnHint = new JButton("Get Hint");
        controlPanel.add(btnHint);
        controlPanel.add(btnNewGame);
        btnHint.addActionListener(listener);
        btnNewGame.addActionListener(listener);

        panel.add(menubar, BorderLayout.NORTH);
        panel.add(board, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);

        board.newGame();

        return panel;


    }

    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start) {
                page.show(mainmenu, "gameMenu");

            }
            if (e.getSource() == btnHint) {
                String input = JOptionPane.showInputDialog("Enter cell (row,col) for hint (1-9,1-9):");
                if (input != null && input.matches("\\d,\\d")) {
                    String[] parts = input.split(",");
                    int row = Integer.parseInt(parts[0]) - 1;
                    int col = Integer.parseInt(parts[1]) - 1;

                    if (!board.provideHint(row, col)) {
                        JOptionPane.showMessageDialog(SudokuMain.this, "Hint could not be applied!");
                    }
                }
            }

            if(e.getSource()== btnNewGame){
                board.newGame();
            }
        }
    }


    /** The entry main() entry method */
    public static void main(String[] args) {
        // [TODO 1] Check "Swing program template" on how to run
        //  the constructor of "SudokuMain"
        // .........
        SudokuMain game = new SudokuMain();
    }
}