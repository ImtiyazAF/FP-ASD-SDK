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
    public static JMenu option;
    public static JMenuItem offSound, newGame, resetGame, exit;
    private CardLayout page;
    private JButton start, btnHint;

    private JTextField nama;
    private JComboBox diff;

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
        //JPanel panelAtas = new JPanel();
        JPanel panelDalam = new JPanel();
        panelDalam.setLayout(new BoxLayout(panelDalam, BoxLayout.Y_AXIS));

        String[] difficulty = {"Easy","Medium","Hard"};
        diff = new JComboBox(difficulty);

        nama = new JTextField();

        //ImageIcon icon1 = new ImageIcon(new ImageIcon("logo.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

        start = new JButton("Start");
        JLabel l1 = new JLabel("Enter Player Name");
        JLabel l2 = new JLabel("Pick Difficulty");
        //JLabel icon = new JLabel(icon1);

        l1.setAlignmentX(Component.CENTER_ALIGNMENT);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);

        start.addActionListener(listener);

        //panelAtas.add(icon);
        panelDalam.add(l1);
        panelDalam.add(nama);
        panelDalam.add(l2);
        panelDalam.add(diff);
        panelDalam.add(start);
        panel.add(panelDalam, BorderLayout.CENTER);
        //panel.add(panelAtas, BorderLayout.NORTH);

        return panel;
    }

    JPanel GameMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel menubar = new JPanel(new GridLayout());
        JPanel gameboard = new JPanel();
        JPanel playerBar = new JPanel(new GridLayout(4,1));
        JPanel foot = new JPanel();

        JLabel l1 = new JLabel("Player Name");
        JLabel l2 = new JLabel("Score");
        JLabel l3 = new JLabel("Progres Bar");
        JLabel l4 = new JLabel("Timer");

        l1.setHorizontalAlignment(SwingConstants.CENTER);
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        l3.setHorizontalAlignment(SwingConstants.CENTER);
        l4.setHorizontalAlignment(SwingConstants.CENTER);

        board = new GameBoardPanel();
        menuB = new JMenuBar();
        option = new JMenu("Options");
        resetGame = new JMenuItem("Reset Game");
        newGame = new JMenuItem("New Game");
        exit = new JMenuItem("Exit");
        offSound = new JMenuItem("Turn Sound Off");
        btnHint = new JButton("Get Hint");

        option.add(offSound);
        option.add(newGame);
        option.add(resetGame);
        option.add(exit);
        menuB.add(option);

        btnHint.addActionListener(listener);
        newGame.addActionListener(listener);
        resetGame.addActionListener(listener);
        exit.addActionListener(listener);

        menubar.add(menuB);
        foot.add(btnHint);
        playerBar.add(l1);
        playerBar.add(l2);
        playerBar.add(l3);
        playerBar.add(l4);

        panel.add(menubar, BorderLayout.NORTH);
        panel.add(board, BorderLayout.CENTER);
        panel.add(playerBar, BorderLayout.WEST);
        panel.add(foot, BorderLayout.SOUTH);

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

            if(e.getSource()== resetGame){
                board.newGame();
            }

            if(e.getSource()==exit){
                System.exit(0);
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