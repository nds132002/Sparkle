import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ShinyPanel extends JPanel {
    Image bg;
    public ShinyPanel(Image bg) {
        this.bg = bg;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, null);
    }
}
