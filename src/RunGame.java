import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by pisit on 9/2/2017 AD.
 */
public class RunGame implements ActionListener {
    private static JFrame f = new JFrame();
    private static Tower t;
    private static JMenuBar menu_bar = new JMenuBar();
    private static StatusBar sb;

    private JMenuItem
            new_game = new JMenuItem("New Game"),
            best_time = new JMenuItem("Best Time"),
            exit = new JMenuItem("Exit"),
            about = new JMenuItem("About");

    private JMenu
            help = new JMenu("Help"),
            game = new JMenu("Game");

    public RunGame(String title) {
        f.setTitle(title);
        build();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == new_game) {
            Object values[] = {3, 4, 5, 6, 7, 8, 9};
            Object val = JOptionPane.showInputDialog(f, "No. Of Disks : ", "Input",
                    JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
            if ((Integer) val != JOptionPane.CANCEL_OPTION) {
                t.init((Integer) val, sb);
            }
        } else if (event.getSource() == exit) {
            System.exit(0);
        }
    }

    private void build() {
        game.add(new_game);
        game.add(best_time);
        game.add(exit);
        game.add(about);

        menu_bar.add(game);
        menu_bar.add(help);

        new_game.addActionListener(this);
        exit.addActionListener(this);

        f.setJMenuBar(menu_bar);
        //f.setSize(660, 280);
        f.setSize(660, 292);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        sb = new StatusBar();
        SwingUtilities.invokeLater(() -> {
            RunGame obj = new RunGame("Tower Of Hanoi alpha version");
            t = new Tower(sb);
            f.getContentPane().add(sb, BorderLayout.SOUTH);
            f.getContentPane().add(t);
        });
    }
}
