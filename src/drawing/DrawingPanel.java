package drawing;

import model.Bond;
import model.Element;
import model.ElementName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener
{
    public static final int PANEL_WIDTH = 1440;
    public static final int PANEL_HEIGHT = 960;

    private Tool tool = Tool.MOUSE;
    private ElementName elementName;
    private int bondType;

    private List<Element> elements = new ArrayList<>();
    private List<Bond> bonds = new ArrayList<>();

    private Element dragged;

    private BufferedImage image = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

    public DrawingPanel()
    {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.black);
        Element.loadImages();
        addMouseListener(this);
        addMouseMotionListener(this);
        //initImage();
    }

    private void initImage()
    {
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
    }

    private void paintImage()
    {
        Graphics g = image.getGraphics();
        super.paintComponent(g);
        for(Bond bond: bonds)
        {
            bond.draw(g);
        }
        for(Element element: elements)
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

    public BufferedImage getImage()
    {
        return image;
    }

    public void setElement(ElementName name)
    {
        tool = Tool.ELEMENT;
        elementName = name;
    }

    public void setBond(int type)
    {
        tool = Tool.BOND;
        bondType = type;
    }

    public void setMouse()
    {
        tool = Tool.MOUSE;
    }

    public void reset()
    {
        elements.clear();
        bonds.clear();
        paintImage();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        //
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() != MouseEvent.BUTTON1) return;
        Point point = e.getPoint();
        switch (tool)
        {
            case MOUSE:
                for(int i = elements.size() - 1; i >= 0; i--)
                {
                    Element element = elements.get(i);
                    if (element.contains(point))
                    {
                        dragged = element;
                        elements.remove(element);
                        elements.add(element);
                        break;
                    }
                }
                break;
            case BOND:
                for(int i = elements.size() - 1; i >= 0; i--)
                {
                    Element element = elements.get(i);
                    if (element.contains(point))
                    {
                        dragged = element;
                        elements.remove(element);
                        elements.add(element);
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton() != MouseEvent.BUTTON1) return;
        Point point = e.getPoint();
        switch (tool)
        {
            case ELEMENT:
                elements.add(new Element(elementName, point));
                paintImage();
                break;
            case MOUSE:
                dragged = null;
                break;
            case BOND:
                if(dragged != null)
                {
                    for(Element element: elements)
                    {
                        if (element.contains(point))
                        {
                            Bond bond = dragged.link(bondType, element);
                            if(bond != null) bonds.add(bond);
                            break;
                        }
                    }
                    paintImage();
                }
                dragged = null;
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        //empty
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        //empty
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        Point point = e.getPoint();
        switch (tool)
        {
            case MOUSE:
                if(dragged != null)
                {
                    dragged.moveTo(point);
                    paintImage();
                }
                break;
            case BOND:
                if(dragged != null)
                {
                    paintImage();
                    Bond.drawGhost(image.getGraphics(), bondType, dragged, point);
                }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        //empty
    }
}