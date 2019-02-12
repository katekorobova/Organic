package model;

import drawing.DrawingPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Element
{
    ElementName elementName;
    int valency;
    private int occupied = 0;
    private Rectangle bounds;
    BufferedImage image;

    static BufferedImage carbonImage;
    static BufferedImage hydrogenImage;
    static BufferedImage oxygenImage;
    static BufferedImage nitrogenImage;
    static BufferedImage phosphorusImage;
    static BufferedImage sulphurImage;

    public static void loadImages()
    {
        try
        {
            carbonImage = ImageIO.read(new File("images/carbon.png"));
            hydrogenImage = ImageIO.read(new File("images/hydrogen.png"));
            oxygenImage = ImageIO.read(new File("images/oxygen.png"));
            nitrogenImage = ImageIO.read(new File("images/nitrogen.png"));
            phosphorusImage = ImageIO.read(new File("images/phosphorus.png"));
            sulphurImage = ImageIO.read(new File("images/sulphur.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void setBounds(Point point)
    {
        bounds = new Rectangle(point.x - image.getWidth() / 2, point.y - image.getHeight() / 2, image.getWidth(), image.getHeight());
    }

    public boolean contains(Point point)
    {
        return bounds.contains(point);
    }

    public void moveTo(Point point)
    {
        if (point.x < bounds.width / 2) point.x = bounds.width / 2;
        if (point.x >= DrawingPanel.PANEL_WIDTH - bounds.width / 2) point.x = DrawingPanel.PANEL_WIDTH - bounds.width / 2 - 1;
        if (point.y < bounds.height / 2) point.y = bounds.height / 2;
        if (point.y >= DrawingPanel.PANEL_HEIGHT - bounds.height / 2) point.y = DrawingPanel.PANEL_HEIGHT - bounds.height / 2 - 1;

        bounds.setLocation(point.x - bounds.width / 2, point.y - bounds.height / 2);
    }

    public void draw(Graphics g)
    {
        g.drawImage(image, bounds.x, bounds.y, null);
    }

    public Point getCenter()
    {
        return new Point((int)bounds.getCenterX(), (int)bounds.getCenterY());
    }

    public Bond link(int bondType, Element element)
    {
        if(element != this && occupied + bondType <= valency && element.occupied + bondType <= element.valency)
        {
            occupied += bondType;
            element.occupied += bondType;
            return new Bond(bondType, this, element);
        }
        return null;
    }

    public void unlink(int bondType, Element element)
    {
        occupied -= bondType;
        element.occupied -= bondType;
    }
}
