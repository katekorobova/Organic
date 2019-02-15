import drawing.Canvas;
import elements.ElementName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener
{
    private final static int BUTTON_WIDTH = 128;
    private final static int BUTTON_HEIGHT = 64;

    private JToggleButton mouse = new JToggleButton(new ImageIcon("images/mouse.png"), true);

    private JToggleButton carbon = new JToggleButton("C", false);
    private JToggleButton hydrogen = new JToggleButton("H", false);
    private JToggleButton oxygen = new JToggleButton("O", false);

    private JButton saveImage = new JButton("\uD83D\uDCBE");
    private JButton saveConfiguration = new JButton("save");
    private JButton loadConfiguration = new JButton("load");
    private JButton reset = new JButton("Reset");
    private JButton exit = new JButton("Exit");

    private Canvas canvas = new Canvas();

    MainWindow()
    {
        setTitle("Organic");
        setLayout(new FlowLayout());

        JPanel settings = new JPanel();
        settings.setLayout(new GridLayout(13, 1));

        settings.add(mouse);

        settings.add(carbon);
        settings.add(hydrogen);
        settings.add(oxygen);

        settings.add(saveImage);
        settings.add(saveConfiguration);
        settings.add(loadConfiguration);
        settings.add(reset);
        settings.add(exit);

        for (Component component : settings.getComponents())
        {
            AbstractButton button = (AbstractButton) component;
            button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            button.addActionListener(this);
        }

        add(settings);
        add(canvas);
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object button = e.getSource();
        if(button == saveImage)
        {
            canvas.saveImage();
        }
        else if(button == saveConfiguration)
        {
            canvas.saveConfiguration();
        }
        else if(button == loadConfiguration)
        {
            canvas.loadConfiguration();
        }
        else if(button == reset)
        {
            canvas.reset();
        }
        else if(button == exit)
        {
            setVisible(false);
            dispose();
        }
        else
        {
            carbon.setSelected(false);
            hydrogen.setSelected(false);
            oxygen.setSelected(false);
            mouse.setSelected(false);

            if (button == mouse)
            {
                mouse.setSelected(true);
                canvas.setMouse();
            }
            else if(button == carbon)
            {
                carbon.setSelected(true);
                canvas.setElement(ElementName.CARBON);
            }
            else if (button == hydrogen)
            {
                hydrogen.setSelected(true);
                canvas.setElement(ElementName.HYDROGEN);
            }
            else if (button == oxygen)
            {
                oxygen.setSelected(true);
                canvas.setElement(ElementName.OXYGEN);
            }
        }
    }

    public static void main(String[] args)
    {
        new MainWindow();
    }
}
