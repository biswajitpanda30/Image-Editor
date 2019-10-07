package demo;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class TRescaleOp extends JFrame {
    DisplayPanel displayPanel;
    JButton brightenButton, darkenButton,
            contIncButton, contDecButton;
    
    public TRescaleOp() {
        super();
        Container container = getContentPane();

        displayPanel = new DisplayPanel();
        container.add(displayPanel);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        panel.setBorder(new TitledBorder(
            "Click a Button to Perform the Associated Operation..."));

        brightenButton = new JButton("Brightness >>");
        brightenButton.addActionListener(new ButtonListener());
        darkenButton = new JButton("Brightness <<");
        darkenButton.addActionListener(new ButtonListener());

        contIncButton = new JButton("Contrast >>");
        contIncButton.addActionListener(new ButtonListener());
        contDecButton = new JButton("Contrast <<");
        contDecButton.addActionListener(new ButtonListener());

        panel.add(brightenButton);
        panel.add(darkenButton);
        panel.add(contIncButton);
        panel.add(contDecButton);

        container.add(BorderLayout.CENTER, panel);

        addWindowListener(new WindowEventHandler());
        setSize(displayPanel.getWidth(), displayPanel.getHeight() + 10);
        show(); // Display the frame
    }

    class WindowEventHandler extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    public static void main(String arg[]) {
        new TRescaleOp();
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton temp = (JButton) e.getSource();

            if (temp.equals(brightenButton)) {
                displayPanel.brighten = true;
                displayPanel.changeOffSet();
                System.out.println(displayPanel.offset + "=offset");
                displayPanel.rescale();
                displayPanel.repaint();
            }
            else if (temp.equals(darkenButton)) {
                displayPanel.brighten = false;
                displayPanel.changeOffSet();
                System.out.println(displayPanel.offset + "=offset");
                displayPanel.rescale();
                displayPanel.repaint();
            }
            else if (temp.equals(contIncButton)) {
                displayPanel.contrastInc = true;
                displayPanel.changeScaleFactor();
                System.out.println(displayPanel.scaleFactor + "=scaleF");
                displayPanel.rescale();
                displayPanel.repaint();
            }
            else if (temp.equals(contDecButton)) {
                displayPanel.contrastInc = false;
                displayPanel.changeScaleFactor();
                System.out.println(displayPanel.scaleFactor + "=scaleF");
                displayPanel.rescale();
                displayPanel.repaint();
            }
        }
    }
}
class DisplayPanel extends JPanel {
    Image displayImage;
    BufferedImage biSrc, biDest, bi; 
    Graphics2D big;
    RescaleOp rescale;
    float scaleFactor = 1.0f;
    float offset = 10;
    boolean brighten, contrastInc;

    DisplayPanel() {
        setBackground(Color.black); 
        loadImage();
        setSize(displayImage.getWidth(this),
                displayImage.getWidth(this));
        createBufferedImages();
    }

    public void loadImage() {
        displayImage = Toolkit.getDefaultToolkit().getImage("E:/Project/Demo/bin/Untitled.jpg");
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(displayImage, 1);
        try {
            mt.waitForAll();
        } catch (Exception e) {
            System.out.println("Exception while loading.");
        }
 
        if (displayImage.getWidth(this) == -1) {
            System.out.println("No jpg file");
            System.exit(0);
        }
    }

    public void createBufferedImages() {
        biSrc = new BufferedImage(displayImage.getWidth(this),
                                  displayImage.getHeight(this),
                                  BufferedImage.TYPE_INT_RGB);

        big = biSrc.createGraphics();
        big.drawImage(displayImage, 0, 0, this);

        biDest = new BufferedImage(displayImage.getWidth(this),
                                   displayImage.getHeight(this),
                                   BufferedImage.TYPE_INT_RGB);
        bi = biSrc;
    }

    public void changeOffSet() {
        if (brighten) {
            if (offset < 255)
               offset = offset+5.0f;
        }
        else {
            if (offset > 0)
               offset = offset-5.0f;
        }
    }

    public void changeScaleFactor() {
        if (contrastInc) {
            if (scaleFactor < 2)
                scaleFactor = scaleFactor+0.1f;
        }
        else {
            if (scaleFactor > 0)
                scaleFactor = scaleFactor-0.1f;
        }
    }

    public void rescale() {
        rescale = new RescaleOp(scaleFactor, offset, null);
        rescale.filter(biSrc, biDest);
        bi = biDest;
    }

    public void update(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        paintComponent(g);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        g2D.drawImage(bi, 0, 0, this);
    }
}
