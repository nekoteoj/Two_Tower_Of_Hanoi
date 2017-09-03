import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Created by pisit on 9/1/2017 AD.
 */

public class Tower extends JPanel implements MouseListener, MouseMotionListener {

    public static final long serialVersionUID = 0xFF;

    @SuppressWarnings("unchecked")
    private ArrayDeque<Rectangle2D.Double>[] s = (ArrayDeque<Rectangle2D.Double>[]) new ArrayDeque<?>[3];
    @SuppressWarnings("unchecked")
    private ArrayDeque<Color>[] disk_color = (ArrayDeque<Color>[]) new ArrayDeque<?>[3];
    private static Rectangle2D.Double top = null;
    private Color top_color = null;
    private double ax;
    private double ay;
    private double w;
    private double h;
    private boolean draggable = false;
    private boolean firstTime = false;
    private StatusBar sb;
    private int count = 0;

    public Tower(StatusBar sb) {
        firstTime = true;
        init(4, sb);
        addMouseListener(this);
        addMouseMotionListener(this);
        this.sb = sb;
    }

    public void init(int val, StatusBar status) {
        Color c[] = {Color.yellow, Color.red, Color.blue, Color.pink,
                Color.cyan, Color.magenta, Color.green, Color.orange, Color.lightGray};
        s[0] = new ArrayDeque<>();
        s[1] = new ArrayDeque<>();
        s[2] = new ArrayDeque<>();

        disk_color[0] = new ArrayDeque<>();
        disk_color[1] = new ArrayDeque<>();
        disk_color[2] = new ArrayDeque<>();

        for (int i = 0; i < val; i++) {
            Rectangle2D.Double r = new Rectangle2D.Double();
            double x = getWidth() / 6;
            x = (x == 0) ? 109 : x;
            double wr = val * 25 - 20 * i;
            r.setFrame(x - wr / 2, 190 - i * 20, wr, 20);
            s[0].add(r);
            disk_color[0].add(c[i]);
        }

        //Disk 2
        for (int i = 0; i < val; i++) {
            Rectangle2D.Double r = new Rectangle2D.Double();
            double x = getWidth() / 6;
            x = (x == 0) ? 109 : x;
            double wr = val * 25 - 20 * i;
            r.setFrame(3 * x - wr / 2 + 3, 190 - i * 20, wr, 20);
            s[1].add(r);
            disk_color[1].add(c[i]);
        }

        top = null;
        top_color = null;
        ax = 0.0;
        ay = 0.0;
        w = 0.0;
        h = 0.0;
        draggable = false;
        repaint();

        count = 0;
        status.setCount(count);
    }

    public void mouseClicked(MouseEvent event) {

    }

    public void mousePressed(MouseEvent event) {
        Point pos = event.getPoint();
        int n = currentTower(pos);
        if (!s[n].isEmpty()) {
            top = s[n].peekLast();
            if (top.contains(pos)) {
                top = s[n].removeLast();
                top_color = disk_color[n].removeLast();
                ax = top.getX();
                ay = top.getY();
                w = pos.getX() - ax;
                h = pos.getY() - ay;
                draggable = true;
            } else {
                top = null;
                top_color = Color.black;
                draggable = false;
            }
        }
    }

    public void mouseReleased(MouseEvent event) {
        if (top != null && draggable) {
            int tower = currentTower(event.getPoint());
            double x, y;
            if (!s[tower].isEmpty()) {
                if (s[tower].peekLast().getWidth() >= top.getWidth()) {
                    //calculating ay for dragged disk for placement
                    y = s[tower].peekLast().getY() - 20;
                    sb.setCount(++count);
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong Move",
                            "Tower Of Hanoi", JOptionPane.ERROR_MESSAGE);
                    tower = currentTower(new Point((int) ax, (int) ay));
                    if (!s[tower].isEmpty()) {
                        y = s[tower].peekLast().getY() - 20;
                    } else {
                        y = getHeight() - 40;
                    }
                }
            } else {
                // If no previous disk in tower
                y = getHeight() - 40;
            }
            x = (int) (getWidth() / 6 + (getWidth() / 3) * tower - top.getWidth() / 2);
            top.setFrame(x, y, top.getWidth(), top.getHeight());
            s[tower].add(top);
            disk_color[tower].add(top_color);

            top = null;
            top_color = Color.black;
            draggable = false;
            repaint();
        }
    }

    public void mouseEntered(MouseEvent event) {

    }

    public void mouseExited(MouseEvent event) {

    }

    public void mouseMoved(MouseEvent event) {

    }

    public void mouseDragged(MouseEvent event) {
        // Getting current mouse position
        int cx = event.getX();
        int cy = event.getY();
        if (top != null && draggable) {
            top.setFrame(cx - w, cy - h, top.getWidth(), top.getHeight());
            // repainting if dragging a disk
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int holder_x = getWidth() / 6;
        int holder_y1 = getHeight() - 10 * 20;
        int holder_y2 = getHeight() - 20;

        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(holder_x, holder_y1, holder_x, holder_y2);
        g2d.drawLine(3 * holder_x, holder_y1, 3 * holder_x, holder_y2);
        g2d.drawLine(5 * holder_x, holder_y1, 5 * holder_x, holder_y2);
        g2d.drawLine(0, holder_y2, getWidth(), holder_y2);

        g2d.setStroke(new BasicStroke(1));

        g2d.setColor(top_color);

        if (draggable && top != null) {
            // drawing dragged disk
            g2d.fill(top);
        }

        // drawing disk of each tower
        drawtower(g2d, 0);
        drawtower(g2d, 1);
        drawtower(g2d, 2);
    }

    private void drawtower(Graphics2D g2d, int n) {
        if (!s[n].isEmpty()) {
            Iterator<Color> citt = disk_color[n].iterator();
            Iterator<Rectangle2D.Double> ritt = s[n].iterator();
            for (int i = 0; i < s[n].size(); i++) {
                g2d.setColor(citt.next());
                g2d.fill(ritt.next());
            }
        }
    }

    private int currentTower(Point p) {
        // return current tower position with respect to Point p
        Rectangle2D.Double rA = new Rectangle2D.Double();
        Rectangle2D.Double rB = new Rectangle2D.Double();
        Rectangle2D.Double rC = new Rectangle2D.Double();

        rA.setFrame(0, 0, getWidth() / 3, getHeight());
        rB.setFrame(getWidth() / 3, 0, getWidth() / 3, getHeight());
        rC.setFrame(2 * getWidth() / 3, 0, getWidth() / 3, getHeight());

        if (rA.contains(p)) {
            return 0;
        } else if (rB.contains(p)) {
            return 1;
        } else if (rC.contains(p)) {
            return 2;
        } else {
            return -1;
        }
    }
}
