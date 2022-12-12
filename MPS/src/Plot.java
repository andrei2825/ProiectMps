import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class Plot extends JPanel {
    LinkedHashMap<Integer, Double> data;
    TreeMap<Integer, Double> sortedData;

    public Plot(LinkedHashMap<Integer, Double> data) {
        this.data = data;
        sortedData = new TreeMap<>(data);
        sortedData.putAll(data);

        for (Integer key : sortedData.keySet()) {
            System.out.println(key + " " + sortedData.get(key));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        g2.draw(new Line2D.Double(50, 50, 50, height - 50));
        g2.draw(new Line2D.Double(50, height - 50, width - 50, height - 50));
        int xInc = (width - 100) / (sortedData.size() + 1);
        int scale = (height - 100) / 100;
        int x = 50;
        int y = height - 50;
        g2.drawString("0", 30, y);
        int i;
        for (i = 0; i <= 100; i+=10) {
            g2.drawString("" + i, 30, y - i * scale);
        }
        int count = 1;
        int x1;
        int y1;
        for (int key : sortedData.keySet()) {
            x1 = x + count * xInc;
            y1 = height - 50 - (int) Math.round(sortedData.get(key) * scale);
            g2.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
            g2.drawString("" + key, x1 -5, y + 30);
            count++;
        }
    }
}