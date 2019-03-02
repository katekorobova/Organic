package drawing;

import structure.ElementName;
import structure.Molecule;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener, Serializable
{
    private static final int PANEL_WIDTH = 1440;
    private static final int PANEL_HEIGHT = 960;
    private BufferedImage image = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

    private Tool tool = Tool.MOUSE;
    private Molecule molecule = new Molecule();

    public static BufferedImage carbonImage;
    public static BufferedImage hydrogenImage;
    public static BufferedImage oxygenImage;

    public Canvas()
    {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.black);
        loadImages();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    //I store images here as I need Molecule to be Serializable
    private static void loadImages()
    {
        try
        {
            carbonImage = ImageIO.read(new File("images/carbon.png"));
            hydrogenImage = ImageIO.read(new File("images/hydrogen.png"));
            oxygenImage = ImageIO.read(new File("images/oxygen.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //I draw the molecule on a BitMap image first and then draw this image on the canvas
    private void paintImage()
    {
        Graphics g = image.getGraphics();
        super.paintComponent(g);
        molecule.draw(g);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, null);
    }

    public void setTool(Tool t)
    {
        tool = t;
    }

    //Clear the canvas
    public void reset()
    {
        molecule = new Molecule();
        paintImage();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() != MouseEvent.BUTTON1) return;
        Point point = e.getPoint();
        switch (tool)
        {
            case MOUSE:
                molecule.setActive(point);
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        Point point = e.getPoint();
        switch (tool)
        {
            case MOUSE:
                molecule.moveActive(point);
                break;
        }
        paintImage();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton() != MouseEvent.BUTTON1) return;
        Point point = e.getPoint();
        switch (tool)
        {
            case MOUSE:
                molecule.releaseActive(point);
                break;
            case DELETE:
                molecule.deleteByPoint(point);
                break;
            case CARBON:
                molecule.addElement(ElementName.CARBON, point);
                break;
            case HYDROGEN:
                molecule.addElement(ElementName.HYDROGEN, point);
                break;
            case OXYGEN:
                molecule.addElement(ElementName.OXYGEN, point);
                break;
        }
        paintImage();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    //Save the picture of canvas
    public void saveImage()
    {
        JFrame textFrame = new JFrame("Enter the name of the file");
        JTextField textField = new JTextField(30);
        textField.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        textFrame.add(textField);
        textFrame.pack();
        textField.addActionListener(e1 ->
        {
            String path = textField.getText();
            try
            {
                File file = new File(path + ".png");
                ImageIO.write(image, "png", file);
                textFrame.setVisible(false);
                textFrame.dispose();
            } catch (Exception exp)
            {
                textField.setText("");
            }
        });
        BufferedImage icon;
        try
        {
            icon = ImageIO.read(new File("images/icon.png"));
            textFrame.setIconImage(icon);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        textFrame.setLocationRelativeTo(null);
        textFrame.setVisible(true);
    }

    //Save the molecule to file
    public void saveConfiguration()
    {
        JFrame textFrame = new JFrame("Enter the name of the file");
        JTextField textField = new JTextField(30);
        textField.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        textFrame.add(textField);
        textFrame.pack();
        textField.addActionListener(e1 ->
        {
            String path = textField.getText();
            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + ".txt"));
                oos.writeObject(molecule);
                oos.close();
                textFrame.setVisible(false);
                textFrame.dispose();
            } catch (Exception exp)
            {
                exp.printStackTrace();
                textField.setText("");
            }
        });
        BufferedImage icon;
        try
        {
            icon = ImageIO.read(new File("images/icon.png"));
            textFrame.setIconImage(icon);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        textFrame.setLocationRelativeTo(null);
        textFrame.setVisible(true);
    }

    //Load a molecule from file
    public void loadConfiguration()
    {
        JFrame textFrame = new JFrame("Enter the name of the file");
        JTextField textField = new JTextField(30);
        textField.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        textFrame.add(textField);
        textFrame.pack();
        textField.addActionListener(e1 ->
        {
            String path = textField.getText();
            try
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path + ".txt"));
                molecule = (Molecule) ois.readObject();
                textFrame.setVisible(false);
                textFrame.dispose();
                paintImage();
            } catch (Exception exp)
            {
                textField.setText("");
            }
        });
        BufferedImage icon;
        try
        {
            icon = ImageIO.read(new File("images/icon.png"));
            textFrame.setIconImage(icon);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        textFrame.setLocationRelativeTo(null);
        textFrame.setVisible(true);
    }

    //Analyse the molecule
    public void analyse()
    {
        JFrame textFrame = new JFrame("Analysis");
        JTextArea textArea = new JTextArea(molecule.analyse());
        textArea.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        textFrame.add(textArea);
        textFrame.pack();
        BufferedImage icon;
        try
        {
            icon = ImageIO.read(new File("images/icon.png"));
            textFrame.setIconImage(icon);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        textFrame.setLocationRelativeTo(null);
        textFrame.setVisible(true);
    }
}