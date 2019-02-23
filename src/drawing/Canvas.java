package drawing;

import elements.Element;
import bonds.*;
import elements.ElementName;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener, Serializable
{
    private static final int PANEL_WIDTH = 1440;
    private static final int PANEL_HEIGHT = 960;
    private BufferedImage image = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

    private Tool tool = Tool.MOUSE;
    private Element draggedElement;
    private UnfilledBond draggedBond;

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
        reset();
    }

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

    private void paintImage()
    {
        Graphics g = image.getGraphics();
        super.paintComponent(g);
        for(FilledBond bond: FilledBond.all)
        {
            bond.draw(g);
        }
        for(UnfilledBond bond: UnfilledBond.all)
        {
            bond.draw(g);
        }
        for(Element element: Element.all)
        {
            element.draw(g);
        }
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

    public void reset()
    {
        Element.all.clear();
        FilledBond.all.clear();
        UnfilledBond.all.clear();
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
                boolean cont = true;
                for(int i = Element.all.size() - 1; i >= 0; i--)
                {
                    Element element = Element.all.get(i);
                    if (element.contains(point))
                    {
                        draggedElement = element;
                        Element.all.remove(element);
                        Element.all.add(element);
                        cont = false;
                        break;
                    }
                }
                if(cont)
                {
                    for (UnfilledBond bond : UnfilledBond.all)
                    {
                        if (bond.contains(point))
                        {
                            draggedBond = bond;
                            UnfilledBond.all.remove(bond);
                            UnfilledBond.all.add(bond);
                            break;
                        }
                    }
                }
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
                if(draggedElement != null)
                {
                    draggedElement.moveTo(point);
                }
                else if(draggedBond != null)
                {
                    draggedBond.moveTo(point);
                }
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
                if(draggedBond != null)
                {
                    for(UnfilledBond bond: UnfilledBond.all)
                    {
                        if(bond.contains(point))
                        {
                            draggedBond.merge(bond);
                            break;
                        }
                    }
                }
                draggedElement = null;
                draggedBond = null;
                break;
            case DELETE:
                boolean cont = true;
                for(int i = Element.all.size() - 1; i >= 0; i--)
                {
                    Element element = Element.all.get(i);
                    if (element.contains(point))
                    {
                        element.delete();
                        cont = false;
                        break;
                    }
                }
                if(cont)
                {
                    for (FilledBond bond : FilledBond.all)
                    {
                        if (bond.contains(point))
                        {
                            bond.downgrade();
                            break;
                        }
                    }
                }
                break;
            case CARBON:
                new Element(ElementName.CARBON, point);
                break;
            case HYDROGEN:
                new Element(ElementName.HYDROGEN, point);
                break;
            case OXYGEN:
                new Element(ElementName.OXYGEN, point);
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
        textFrame.setLocationRelativeTo(null);
        textFrame.setVisible(true);
    }

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
                oos.writeObject(Element.all);
                oos.writeObject(FilledBond.all);
                oos.writeObject(UnfilledBond.all);
                oos.close();
                textFrame.setVisible(false);
                textFrame.dispose();
            } catch (Exception exp)
            {
                exp.printStackTrace();
                textField.setText("");
            }
        });
        textFrame.setLocationRelativeTo(null);
        textFrame.setVisible(true);
    }

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
                Element.all = (ArrayList<Element>)ois.readObject();
                FilledBond.all = (ArrayList<FilledBond>)ois.readObject();
                UnfilledBond.all = (ArrayList<UnfilledBond>)ois.readObject();
                textFrame.setVisible(false);
                textFrame.dispose();
                paintImage();
            } catch (Exception exp)
            {
                textField.setText("");
            }
        });
        textFrame.setLocationRelativeTo(null);
        textFrame.setVisible(true);
    }
}