package sudoku;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GameApp extends JFrame{
    public static JMenuBar menuB;
    public static JMenu menu;
    public static JMenuItem m1, m2, m3;
    private CardLayout page;
    Container cp = getContentPane();

    JPanel mainmenu;

    JButton btnNewGame = new JButton("New Game");

    public GameApp(){
        page = new CardLayout();
        mainmenu = new JPanel(page);
        //JPanel mainMenu = MainMenu();
        JPanel gameMenu = GameMenu();

        //mainmenu.add(mainMenu, "mainMenu");
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

    //JPanel MainMenu(){

    //}

    JPanel GameMenu() {
        JPanel panel1 = new JPanel(new BorderLayout());
        GameBoardPanel board = new GameBoardPanel();

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

        panel1.add(menubar, BorderLayout.NORTH);
        panel1.add(board, BorderLayout.CENTER);

        board.newGame();

        return panel1;


    }


}
