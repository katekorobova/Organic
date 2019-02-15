package elements;

import drawing.Canvas;
import bonds.FilledBond;
import bonds.UnfilledBond;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Element implements Serializable
{
    public ElementName elementName;
    int valency;
    Rectangle bounds;
    List<FilledBond> filledBonds = new ArrayList<>();
    List<UnfilledBond> unfilledBonds = new ArrayList<>();

    public List<FilledBond> getFilledBonds()
    {
        return filledBonds;
    }

    public List<UnfilledBond> getUnfilledBonds()
    {
        return unfilledBonds;
    }

    public boolean contains(Point point)
    {
        return bounds.contains(point);
    }

    public void moveTo(Point point)
    {
        if (point.x < bounds.width / 2) point.x = bounds.width / 2;
        if (point.x >= Canvas.PANEL_WIDTH - bounds.width / 2) point.x = Canvas.PANEL_WIDTH - bounds.width / 2 - 1;
        if (point.y < bounds.height / 2) point.y = bounds.height / 2;
        if (point.y >= Canvas.PANEL_HEIGHT - bounds.height / 2) point.y = Canvas.PANEL_HEIGHT - bounds.height / 2 - 1;

        bounds.setLocation(point.x - bounds.width / 2, point.y - bounds.height / 2);

        for(UnfilledBond bond: unfilledBonds)
        {
            bond.adjustEnd();
        }
    }

    public abstract void draw(Graphics g);

    public Point getCenter()
    {
        return new Point((int)bounds.getCenterX(), (int)bounds.getCenterY());
    }

    public FilledBond link(UnfilledBond bond, Element element)
    {
        unfilledBonds.remove(bond);
        element.unfilledBonds.remove(0);
        return new FilledBond(this, element);
    }

    public boolean linkable(ElementName name)
    {
        return true;
    }

    public static Element elementByName(ElementName name, Point point, double angle)
    {
        switch (name)
        {
            case CARBON: return new Carbon(point, angle);
            case HYDROGEN:return new Hydrogen(point, angle);
            case OXYGEN: return new Oxygen(point, angle);
            default: return null;
        }
    }

    void createBonds(double angle)
    {
        for(int i = 0; i < valency; i++)
        {
            unfilledBonds.add(new UnfilledBond(this, 2 * Math.PI * i / valency + angle));
        }
    }
}
