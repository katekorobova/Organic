import drawing.DrawingPanel;
import model.ElementName;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow extends JFrame implements ActionListener
{
    private final static int BUTTON_WIDTH = 128;
    private final static int BUTTON_HEIGHT = 64;

    private JButton carbon = new JButton("C");
    private JButton hydrogen = new JButton("H");
    private JButton oxygen = new JButton("O");
    private JButton nitrogen = new JButton("N");
    private JButton phosphorus = new JButton("P");
    private JButton sulphur = new JButton("S");

    private JButton singleBond = new JButton("|");
    private JButton doubleBond = new JButton("||");
    private JButton tripleBond = new JButton("|||");

    private JButton mouse = new JButton("Mouse");
    private JButton save = new JButton("\uD83D\uDCBE");
    private JButton reset = new JButton("Reset");
    private JButton exit = new JButton("Exit");

    DrawingPanel drawingPanel = new DrawingPanel();

    MainWindow()
    {
        setTitle("Organic");
        setLayout(new FlowLayout());

        JPanel settings = new JPanel();
        settings.setLayout(new GridLayout(13, 1));

        settings.add(carbon);
        settings.add(hydrogen);
        settings.add(oxygen);
        settings.add(nitrogen);
        settings.add(phosphorus);
        settings.add(sulphur);

        settings.add(singleBond);
        settings.add(doubleBond);
        settings.add(tripleBond);

        settings.add(mouse);
        settings.add(save);
        settings.add(reset);
        settings.add(exit);

        for (Component component : settings.getComponents())
        {
            JButton button = (JButton) component;
            button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            button.addActionListener(this);
        }

        add(settings);
        add(drawingPanel);
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton button = (JButton) e.getSource();
        if(button == carbon)
        {
            drawingPanel.setElement(ElementName.CARBON);
        }
        else if (button == hydrogen)
        {
            drawingPanel.setElement(ElementName.HYDROGEN);
        }
        else if (button == oxygen)
        {
            drawingPanel.setElement(ElementName.OXYGEN);
        }
        else if (button == nitrogen)
        {
            drawingPanel.setElement(ElementName.NITROGEN);
        }
        else if (button == phosphorus)
        {
            drawingPanel.setElement(ElementName.PHOSPHORUS);
        }
        else if (button == sulphur)
        {
            drawingPanel.setElement(ElementName.SULPHUR);
        }
        else if(button == singleBond)
        {
            drawingPanel.setBond(1);
        }
        else if(button == doubleBond)
        {
            drawingPanel.setBond(2);
        }
        else if(button == tripleBond)
        {
            drawingPanel.setBond(3);
        }
        else if (button == mouse)
        {
            drawingPanel.setMouse();
        }
        else if(button == save)
        {
            JFrame textFrame = new JFrame("Enter the name of the file");
            JTextField textField = new JTextField(30);
            textFrame.add(textField);
            textFrame.pack();
            textField.addActionListener(e1 ->
            {
                String path = textField.getText();
                try
                {
                    File file = new File(path + ".png");
                    ImageIO.write(drawingPanel.getImage(), "png", file);
                    textFrame.setVisible(false);
                    textFrame.dispose();
                } catch (Exception exp)
                {
                    textField.setText("");
                }
            });
            textFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            textFrame.setLocationRelativeTo(null);
            textFrame.setVisible(true);
        }
        else if(button == reset)
        {
            drawingPanel.reset();
        }
        else if(button == exit)
        {
            setVisible(false);
            dispose();
        }
    }

    public static void main(String[] args)
    {
        new MainWindow();
    }
}
