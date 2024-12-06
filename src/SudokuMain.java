/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231136 - Maulana Muhammad Ad-Dzikri
 * 2 - 5026231172 - Mochamad Zhulmi Danovanz H
 * 3 - 5026231197- Imtiyaz Shafhal Afif
 * */
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
    private JLabel name;

    private JTextField nama;
    JComboBox diff;

    private Timer gameTimer;
    private JProgressBar progressBar;
    private JLabel timerLabel;
    private JLabel scoreLabel;

    private int elapsedTime = 0;
    private int score = 100;

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
        JPanel panelAtas = new JPanel();
        panelAtas.setLayout(new BoxLayout(panelAtas, BoxLayout.Y_AXIS));
        JPanel panelDalam = new JPanel();
        panelDalam.setLayout(new BoxLayout(panelDalam, BoxLayout.Y_AXIS));

        String[] difficulty = {"Easy","Medium","Hard"};
        diff = new JComboBox(difficulty);
        diff.setMaximumSize(new Dimension(200, diff.getPreferredSize().height));

        nama = new JTextField();
        nama.setMaximumSize(new Dimension(200, nama.getPreferredSize().height));


        ImageIcon icon1 = new ImageIcon(new ImageIcon("logo.png").getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));

        start = new JButton("Start");
        JLabel l1 = new JLabel("Enter Player Name");
        JLabel l2 = new JLabel("Pick Difficulty");
        JLabel iconLabel = new JLabel(icon1);

        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);

        start.addActionListener(listener);

        panelAtas.add(iconLabel);
        panelDalam.add(l1);
        panelDalam.add(nama);
        panelDalam.add(l2);
        panelDalam.add(diff);
        panelDalam.add(Box.createVerticalStrut(50));
        panelDalam.add(start);

        panel.add(panelAtas, BorderLayout.NORTH);
        panel.add(panelDalam, BorderLayout.CENTER);

        return panel;
    }

    JPanel GameMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel menubar = new JPanel(new GridLayout());
        JPanel gameboard = new JPanel();
        JPanel playerBar = new JPanel(new GridLayout(8,1));
        JPanel foot = new JPanel();

        timerLabel = new JLabel("Time: 0 sec");
        scoreLabel = new JLabel("Score: 100");
        progressBar = new JProgressBar();

        JLabel l1 = new JLabel("Player Name");
        JLabel l2 = new JLabel("Score");
        JLabel l3 = new JLabel("Progres Bar");
        JLabel l4 = new JLabel("Timer");
        name = new JLabel();

        l1.setHorizontalAlignment(SwingConstants.CENTER);
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        l3.setHorizontalAlignment(SwingConstants.CENTER);
        l4.setHorizontalAlignment(SwingConstants.CENTER);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

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
        playerBar.add(name);
        playerBar.add(l2);
        playerBar.add(scoreLabel);
        playerBar.add(l3);
        playerBar.add(progressBar);
        playerBar.add(l4);
        playerBar.add(timerLabel);

        panel.add(menubar, BorderLayout.NORTH);
        panel.add(board, BorderLayout.CENTER);
        panel.add(playerBar, BorderLayout.WEST);
        panel.add(foot, BorderLayout.SOUTH);


        return panel;
    }

    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start) {
                page.show(mainmenu, "gameMenu");
                board.newGame(diff.getSelectedIndex());
                name.setText(nama.getText());
                elapsedTime = 0;
                score = 100;
                updateUIComponents();


                startGameTimer();

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

            if(e.getSource()== newGame){
                page.show(mainmenu, "mainMenu");
            }

            if(e.getSource()== resetGame){
                board.resetGameBoard();
            }

            if(e.getSource()==exit){
                System.exit(0);
            }
        }
    }

    private void updateUIComponents() {
        progressBar.setValue(board.getProgress());
        scoreLabel.setText("Score: " + score);
    }



    public void calculateScore(boolean correctGuess) {
        if (elapsedTime < 30) {
            score = 100;
        } else if (elapsedTime < 60) {
            score = 95;
        } else if (elapsedTime < 90) {
            score = 90;
        } else if (elapsedTime < 120) {
            score = 85;
        } else if (elapsedTime < 300) {
            score = 55 + (5 * (5 - (elapsedTime / 60)));
        } else {
            score = 40;
        }

        if (!correctGuess) {
            score -= 2.5;
        }
        updateUIComponents();
    }

    private void startGameTimer() {
        gameTimer = new Timer(1000, e -> {
            elapsedTime++;
            timerLabel.setText("Time: " + elapsedTime + " sec");
        });
        gameTimer.start();  // Mulai timer
    }



    /** The entry main() entry method */
    public static void main(String[] args) {
        // [TODO 1] Check "Swing program template" on how to run
        //  the constructor of "SudokuMain"
        // .........
        SudokuMain game = new SudokuMain();
    }
}