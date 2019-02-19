package elements;

import bonds.FilledBond;
import bonds.UnfilledBond;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Element implements Serializable
{
    int valency;
    Rectangle bounds;
    private List<FilledBond> filledBonds = new ArrayList<>();
    private List<UnfilledBond> unfilledBonds = new ArrayList<>();
    private List<Element> dependent = new ArrayList<>();
    private static Random random = new Random();
    public static ArrayList<Element> all = new ArrayList<>();

    Element()
    {
        all.add(this);
    }

    public boolean contains(Point point)
    {
        return bounds.contains(point);
    }

    public void moveTo(Point point)
    {
        int deltaX = point.x - bounds.width / 2 - bounds.x;
        int deltaY = point.y - bounds.height / 2 - bounds.y;
        bounds.setLocation(point.x - bounds.width / 2, point.y - bounds.height / 2);
        for(UnfilledBond bond: unfilledBonds)
        {
            bond.adjustEnd();
        }
        for(Element element: dependent)
        {
            element.moveBy(deltaX, deltaY);
        }
    }

    private void moveBy(int x, int y)
    {
        bounds.setLocation(bounds.x + x, bounds.y + y);
        for(UnfilledBond bond: unfilledBonds)
        {
            bond.adjustEnd();
        }
        for(Element element: dependent)
        {
            element.moveBy(x, y);
        }
    }

    public abstract void draw(Graphics g);

    public Point getCenter()
    {
        return new Point((int)bounds.getCenterX(), (int)bounds.getCenterY());
    }

    public void link(Element element, UnfilledBond myBond, UnfilledBond theirBond)
    {
        if(this == element) return;

        for(FilledBond bond: filledBonds)
        {
            if(bond.connects(element))
            {
                bond.upgrade(myBond, theirBond);
                return;
            }
        }
        new FilledBond(myBond, theirBond);
        if(valency > element.valency) dependent.add(element);
        else if(valency < element.valency) element.dependent.add(this);
    }

    void addBonds()
    {
        for(int i = 0; i < valency; i++)
        {
            unfilledBonds.add(new UnfilledBond(this, 2 * Math.PI * i / valency));
        }
    }

    public void addFilledBond(FilledBond bond)
    {
        filledBonds.add(bond);
    }
    public void addUnfilledBond()
    {
        unfilledBonds.add(new UnfilledBond(this, 2 * Math.PI * random.nextDouble()));
    }

    public void delete()
    {
        for(int i = filledBonds.size() - 1; i >= 0; i--)
        {
            FilledBond bond = filledBonds.get(i);
            bond.delete();
        }
        Element.all.remove(this);
        UnfilledBond.all.removeAll(unfilledBonds);
}

    public void removeFilledBond(FilledBond bond)
    {
        filledBonds.remove(bond);
    }

    public void removeUnfilledBond(UnfilledBond bond)
    {
        unfilledBonds.remove(bond);
    }

    public void removeDependent(Element element)
    {
        dependent.remove(element);
    }
}
