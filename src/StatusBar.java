import javax.swing.*;
import java.awt.*;

/**
 * Created by pisit on 9/3/2017 AD.
 */
public class StatusBar extends JLabel {

    /** Creates a new instance of StatusBar */
    public StatusBar() {
        super();
        super.setPreferredSize(new Dimension(100, 16));
        setCount(0);
    }

    public void setCount(int count) {
        setText("Count : " + count);
    }
}