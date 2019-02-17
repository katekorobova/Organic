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

    public boolean link(Element element, List<FilledBond> bonds)
    {
        if(this == element) return false;

        for(FilledBond bond: filledBonds)
        {
            if(bond.connects(element))
            {
                return bond.upgrade();
            }
        }
        FilledBond newBond = new FilledBond(this, element);
        bonds.add(newBond);
        if(valency > element.valency) dependent.add(element);
        else if(valency < element.valency) element.dependent.add(this);
        return true;
    }

    void addBonds(List<UnfilledBond> bonds)
    {
        for(int i = 0; i < valency; i++)
        {
            unfilledBonds.add(new UnfilledBond(this, 2 * Math.PI * i / valency));
        }
        bonds.addAll(unfilledBonds);
    }

    public void addFilledBond(FilledBond bond)
    {
        filledBonds.add(bond);
    }
    public void addUnfilledBond(List<UnfilledBond> uBonds)
    {
        UnfilledBond newBond = new UnfilledBond(this, 2 * Math.PI * random.nextDouble());
        unfilledBonds.add(newBond);
        uBonds.add(newBond);
    }

    public void delete(List<Element> elements, List<FilledBond> fBonds, List<UnfilledBond> uBonds)
    {
        for(FilledBond bond: filledBonds)
        {
            bond.delete(fBonds, uBonds);
        }
        elements.remove(this);
        uBonds.removeAll(unfilledBonds);
}

    public void deleteFilledBond(FilledBond bond, List<UnfilledBond> uBonds)
    {
        filledBonds.remove(bond);
        addUnfilledBond(uBonds);
    }

    public void deleteUnfilledBond(UnfilledBond bond)
    {
        unfilledBonds.remove(bond);
    }
}
