package elements;

import bonds.FilledBond;
import bonds.UnfilledBond;
import drawing.Canvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Element implements Serializable
{
    public ElementName elementName;
    private int valency;
    private Rectangle bounds;
    private List<FilledBond> filledBonds = new ArrayList<>();
    private List<UnfilledBond> unfilledBonds = new ArrayList<>();
    private List<Element> dependent = new ArrayList<>();
    private static Random random = new Random();
    public static ArrayList<Element> all = new ArrayList<>();

    public Element(ElementName name, Point point)
    {
        elementName = name;
        switch (name)
        {
            case CARBON:
                valency = 4;
                bounds = new Rectangle(point.x - drawing.Canvas.carbonImage.getWidth() / 2, point.y - drawing.Canvas.carbonImage.getHeight() / 2, drawing.Canvas.carbonImage.getWidth(), Canvas.carbonImage.getHeight());
                break;
            case HYDROGEN:
                valency = 1;
                bounds = new Rectangle(point.x - Canvas.hydrogenImage.getWidth() / 2, point.y - Canvas.hydrogenImage.getHeight() / 2, Canvas.hydrogenImage.getWidth(), Canvas.hydrogenImage.getHeight());
                break;
            case OXYGEN:
                valency = 2;
                bounds = new Rectangle(point.x - Canvas.oxygenImage.getWidth() / 2, point.y - Canvas.oxygenImage.getHeight() / 2, Canvas.oxygenImage.getWidth(), Canvas.oxygenImage.getHeight());
                break;
        }
        for(int i = 0; i < valency; i++)
        {
            unfilledBonds.add(new UnfilledBond(this, 2 * Math.PI * i / valency));
        }
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

    public void draw(Graphics g)
    {
        BufferedImage image;
        switch (elementName)
        {
            case CARBON:
                image = Canvas.carbonImage;
                break;
            case HYDROGEN:
                image = Canvas.hydrogenImage;
                break;
            default:
                image = Canvas.oxygenImage;
                break;
        }
        g.drawImage(image, bounds.x, bounds.y, null);
    }

    public Point getCenter()
    {
        return new Point((int)bounds.getCenterX(), (int)bounds.getCenterY());
    }

    public void link(Element element, UnfilledBond myBond, UnfilledBond theirBond)
    {
        if(this == element ||
        elementName == element.elementName && elementName != ElementName.CARBON) return;

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

    public List<FilledBond> getFilledBonds()
    {
        return filledBonds;
    }

    private boolean isHydroxylGroup()
    {
        if (elementName == ElementName.OXYGEN && filledBonds.size() == 2)
        {
            List<FilledBond> newBonds = new ArrayList<>(filledBonds);
            newBonds.removeIf(bond -> bond.getAnother(this).elementName == ElementName.HYDROGEN);
            if(newBonds.size() != 1) return false;
            return newBonds.get(0).getAnother(this).elementName == ElementName.CARBON;
        }
        return false;
    }

    public boolean isAlcoholGroup()
    {
        if(elementName == ElementName.CARBON)
        {
            List<FilledBond> newBonds = new ArrayList<>(filledBonds);
            newBonds.removeIf(bond -> bond.getAnother(this).isHydroxylGroup());
            if(filledBonds.size() == newBonds.size()) return false;
            newBonds.removeIf(bond -> bond.getAnother(this).elementName != ElementName.OXYGEN);
            return newBonds.isEmpty();
        }
        return false;
    }

    public boolean isEtherGroup()
    {
        return elementName == ElementName.OXYGEN &&
                filledBonds.size() == 2 &&
                filledBonds.get(0).getAnother(this).elementName == ElementName.CARBON &&
                filledBonds.get(1).getAnother(this).elementName == ElementName.CARBON;
    }

    private boolean isCarbonylGroup()
    {
        return elementName == ElementName.OXYGEN &&
                filledBonds.size() == 1;
    }

    public boolean isAldehydeGroup()
    {
        if (elementName == ElementName.CARBON && filledBonds.size() == 3)
        {
            List<FilledBond> newBonds = new ArrayList<>(filledBonds);
            newBonds.removeIf(bond -> bond.getAnother(this).isCarbonylGroup());
            if(newBonds.size() != 2) return false;
            newBonds.removeIf(bond -> bond.getAnother(this).elementName == ElementName.HYDROGEN);
            return newBonds.size() < 2;
        }
        return false;
    }

    public boolean isKetoneGroup()
    {
        if(elementName == ElementName.CARBON && filledBonds.size() == 3)
        {
            List<FilledBond> newBonds = new ArrayList<>(filledBonds);
            newBonds.removeIf((bond) -> bond.getAnother(this).isCarbonylGroup());
            if(newBonds.size() != 2) return false;
            newBonds.removeIf((bond) -> bond.getAnother(this).elementName == ElementName.CARBON);
            return newBonds.size() == 0;
        }
        return false;
    }

    public boolean isCarboxylGroup()
    {
        if(elementName == ElementName.CARBON && filledBonds.size() == 3)
        {
            List<FilledBond> newBonds = new ArrayList<>(filledBonds);
            newBonds.removeIf((bond) -> bond.getAnother(this).isCarbonylGroup());
            if(newBonds.size() != 2) return false;
            newBonds.removeIf((bond) -> bond.getAnother(this).isHydroxylGroup());
            if (newBonds.size() != 1) return false;
            return newBonds.get(0).getAnother(this).elementName != ElementName.OXYGEN;
        }
        return false;
    }

    public boolean isWater()
    {
        return elementName == ElementName.OXYGEN &&
                filledBonds.size() == 2 &&
                filledBonds.get(0).getAnother(this).elementName == ElementName.HYDROGEN &&
                filledBonds.get(1).getAnother(this).elementName == ElementName.HYDROGEN;
    }

    public boolean isCO2()
    {
        return elementName == ElementName.CARBON &&
                filledBonds.size() == 2 &&
                filledBonds.get(0).getAnother(this).elementName == ElementName.OXYGEN &&
                filledBonds.get(1).getAnother(this).elementName == ElementName.OXYGEN;
    }
}
