import analysis.Analyser;
import drawing.Canvas;
import drawing.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener
{
    private final static int BUTTON_WIDTH = 128;
    private final static int BUTTON_HEIGHT = 64;

    private JToggleButton mouse = new JToggleButton(new ImageIcon("images/mouse.png"), true);
    private JToggleButton delete = new JToggleButton(new ImageIcon("images/scissors.png"), false);

    private JToggleButton carbon = new JToggleButton(new ImageIcon("images/carbon-button.png"), false);
    private JToggleButton hydrogen = new JToggleButton(new ImageIcon("images/hydrogen-button.png"), false);
    private JToggleButton oxygen = new JToggleButton(new ImageIcon("images/oxygen-button.png"), false);

    private JButton analyse = new JButton(new ImageIcon("images/information.png"));
    private JButton saveImage = new JButton(new ImageIcon("images/photo.png"));
    private JButton saveConfiguration = new JButton(new ImageIcon("images/save.png"));
    private JButton loadConfiguration = new JButton(new ImageIcon("images/load.png"));
    private JButton reset = new JButton(new ImageIcon("images/reset.png"));
    private JButton exit = new JButton(new ImageIcon("images/exit.png"));

    private Canvas canvas = new Canvas();
    private Analyser analyser = new Analyser();

    private MainWindow()
    {
        setTitle("Organic");
        setLayout(new FlowLayout());

        JPanel settings = new JPanel();
        settings.setLayout(new GridLayout(11, 1));

        settings.add(mouse);
        settings.add(delete);

        settings.add(carbon);
        settings.add(hydrogen);
        settings.add(oxygen);

        settings.add(analyse);
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
        if(button == analyse)
        {
            analyser.analyse();
        }
        else if(button == saveImage)
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
            System.exit(0);
        }
        else
        {
            carbon.setSelected(false);
            hydrogen.setSelected(false);
            oxygen.setSelected(false);
            mouse.setSelected(false);
            delete.setSelected(false);

            if (button == mouse)
            {
                mouse.setSelected(true);
                canvas.setTool(Tool.MOUSE);
            }
            else if(button == delete)
            {
                delete.setSelected(true);
                canvas.setTool(Tool.DELETE);
            }
            else if(button == carbon)
            {
                carbon.setSelected(true);
                canvas.setTool(Tool.CARBON);
            }
            else if (button == hydrogen)
            {
                hydrogen.setSelected(true);
                canvas.setTool(Tool.HYDROGEN);
            }
            else if (button == oxygen)
            {
                oxygen.setSelected(true);
                canvas.setTool(Tool.OXYGEN);
            }
        }
    }

    public static void main(String[] args)
    {
        new MainWindow();
    }
}
