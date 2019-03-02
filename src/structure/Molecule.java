package structure;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Molecule implements Serializable
{
    private ArrayList<Element> elements = new ArrayList<>();
    private ArrayList<Bond> bonds = new ArrayList<>();
    private Element active;

    public boolean isComplete()
    {
        for(Element element: elements)
        {
            if(element.hasHalfBonds()) return false;
        }
        return true;
    }

    public void draw(Graphics g)
    {
        for(Bond bond: bonds)
        {
            bond.draw(g);
        }
        for(Element element: elements)
        {
            element.draw(g);
        }
    }

    public void moveActive(Point point)
    {
        if(active != null) active.moveTo(point);
    }

    public void addElement(ElementName name, Point point)
    {
        elements.add(new Element(name, point));
    }

    public void deleteByPoint(Point point)
    {
        Element element = elementByPoint(point);
        if(element != null && !element.hasActiveBond())
        {
            for(int i = bonds.size() - 1; i >= 0; i--)
            {
                Bond bond2 = bonds.get(i);
                if(bond2.connects(element))
                {
                    bond2.delete();
                    bonds.remove(bond2);
                }
            }
            elements.remove(element);
            return;
        }
        Bond bond = bondByPoint(point);
        if(bond != null)
        {
            if(bond.downgrade()) bonds.remove(bond);
        }
    }

    public void setActive(Point point)
    {
        active = elementByPoint(point);
    }

    public void releaseActive(Point point)
    {
        if(active != null && active.hasActiveBond())
        {
            Element element = elementByPoint(point);
            if(element != null && element.hasActiveBond())
            {
                link(active, element);
            }
        }
        active = null;
    }

    private void link(Element element1, Element element2)
    {
        if(element1.elementName == ElementName.OXYGEN &&
                element2.elementName == ElementName.OXYGEN) return;
        if(element1.elementName == ElementName.HYDROGEN &&
                element2.elementName == ElementName.HYDROGEN) return;
        Bond bond = bondByElements(element1, element2);
        if(bond == null) bonds.add(new Bond(element1, element2));
        else bond.upgrade();
    }

    private Bond bondByElements(Element element1, Element element2)
    {
        for(Bond bond: bonds)
        {
            if(bond.links(element1, element2)) return bond;
        }
        return null;
    }

    private Bond bondByPoint(Point point)
    {
        for(Bond bond: bonds)
        {
            if(bond.contains(point)) return bond;
        }
        return null;
    }

    private Element elementByPoint(Point point)
    {
        for(Element element: elements)
        {
            if (element != active && element.contains(point))
            {
                return element;
            }
        }
        return null;
    }
}
