package structure;

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
    private Point center;
    private List<HalfBond> halfBonds = new ArrayList<>();
    private List<Element> dependent = new ArrayList<>();
    private static Random random = new Random();
    private static int RADIUS = 32;
    private HalfBond active;

    public Element(ElementName name, Point point)
    {
        elementName = name;
        center = point;
        switch (name)
        {
            case CARBON:
                valency = 4;
                break;
            case HYDROGEN:
                valency = 1;
                break;
            case OXYGEN:
                valency = 2;
                break;
        }
        for(int i = 0; i < valency; i++)
        {
            halfBonds.add(new HalfBond(this.center, 2 * Math.PI * i / valency));
        }
    }

    public boolean contains(Point point)
    {
        for(HalfBond bond: halfBonds)
        {
            if(bond.contains(point))
            {
                active = bond;
                return true;
            }
        }
        if(center.distance(point) < 32)
        {
            active = null;
            return true;
        }
        return false;
    }

    public boolean hasActiveBond()
    {
        return active != null;
    }

    public void moveTo(Point point)
    {
        if(active == null)
        {
            int deltaX = point.x - center.x;
            int deltaY = point.y - center.y;
            center = point;
            for (HalfBond bond : halfBonds)
            {
                bond.moveStart(point);
            }
            for (Element element : dependent)
            {
                element.moveBy(deltaX, deltaY);
            }
        }
        else active.moveEnd(point);
    }

    private void moveBy(int x, int y)
    {
        center.translate(x, y);
        for(HalfBond bond: halfBonds)
        {
            bond.moveStart(center);
        }
        for(Element element: dependent)
        {
            element.moveBy(x, y);
        }
    }

    public void draw(Graphics g)
    {
        for(HalfBond bond: halfBonds)
        {
            bond.draw(g);
        }
        BufferedImage image;
        switch (elementName)
        {
            case CARBON:
                image = drawing.Canvas.carbonImage;
                break;
            case HYDROGEN:
                image = drawing.Canvas.hydrogenImage;
                break;
            default:
                image = Canvas.oxygenImage;
                break;
        }
        g.drawImage(image, center.x - RADIUS, center.y - RADIUS, null);
    }

    public void addHalfBond()
    {
        halfBonds.add(new HalfBond(this.center, 2 * Math.PI * random.nextDouble()));
    }

    public void removeHalfBond()
    {
        halfBonds.remove(active);
        active = null;
    }

    public void addDependent(Element element)
    {
        if(valency > element.valency) dependent.add(element);
    }

    public void removeDependent(Element element)
    {
        dependent.remove(element);
    }

    public Point getCenter()
    {
        return center;
    }

    public boolean hasHalfBonds()
    {
        return !halfBonds.isEmpty();
    }

    /*private boolean isHydroxylGroup()
    {
        if (elementName == structure.ElementName.OXYGEN && bonds.size() == 2)
        {
            List<structure.Bond> newBonds = new ArrayList<>(bonds);
            newBonds.removeIf(bond -> bond.getAnother(this).elementName == structure.ElementName.HYDROGEN);
            if(newBonds.size() != 1) return false;
            return newBonds.get(0).getAnother(this).elementName == structure.ElementName.CARBON;
        }
        return false;
    }

    public boolean isAlcoholGroup()
    {
        if(elementName == structure.ElementName.CARBON)
        {
            List<structure.Bond> newBonds = new ArrayList<>(bonds);
            newBonds.removeIf(bond -> bond.getAnother(this).isHydroxylGroup());
            if(bonds.size() == newBonds.size()) return false;
            newBonds.removeIf(bond -> bond.getAnother(this).elementName != structure.ElementName.OXYGEN);
            return newBonds.isEmpty();
        }
        return false;
    }

    public boolean isEtherGroup()
    {
        return elementName == structure.ElementName.OXYGEN &&
                bonds.size() == 2 &&
                bonds.get(0).getAnother(this).elementName == structure.ElementName.CARBON &&
                bonds.get(1).getAnother(this).elementName == structure.ElementName.CARBON;
    }

    private boolean isCarbonylGroup()
    {
        return elementName == structure.ElementName.OXYGEN &&
                bonds.size() == 1;
    }

    public boolean isAldehydeGroup()
    {
        if (elementName == structure.ElementName.CARBON && bonds.size() == 3)
        {
            List<structure.Bond> newBonds = new ArrayList<>(bonds);
            newBonds.removeIf(bond -> bond.getAnother(this).isCarbonylGroup());
            if(newBonds.size() != 2) return false;
            newBonds.removeIf(bond -> bond.getAnother(this).elementName == structure.ElementName.HYDROGEN);
            return newBonds.size() < 2;
        }
        return false;
    }

    public boolean isKetoneGroup()
    {
        if(elementName == structure.ElementName.CARBON && bonds.size() == 3)
        {
            List<structure.Bond> newBonds = new ArrayList<>(bonds);
            newBonds.removeIf((bond) -> bond.getAnother(this).isCarbonylGroup());
            if(newBonds.size() != 2) return false;
            newBonds.removeIf((bond) -> bond.getAnother(this).elementName == structure.ElementName.CARBON);
            return newBonds.size() == 0;
        }
        return false;
    }

    public boolean isCarboxylGroup()
    {
        if(elementName == structure.ElementName.CARBON && bonds.size() == 3)
        {
            List<structure.Bond> newBonds = new ArrayList<>(bonds);
            newBonds.removeIf((bond) -> bond.getAnother(this).isCarbonylGroup());
            if(newBonds.size() != 2) return false;
            newBonds.removeIf((bond) -> bond.getAnother(this).isHydroxylGroup());
            if (newBonds.size() != 1) return false;
            return newBonds.get(0).getAnother(this).elementName != structure.ElementName.OXYGEN;
        }
        return false;
    }

    public boolean isWater()
    {
        return elementName == structure.ElementName.OXYGEN &&
                bonds.size() == 2 &&
                bonds.get(0).getAnother(this).elementName == structure.ElementName.HYDROGEN &&
                bonds.get(1).getAnother(this).elementName == structure.ElementName.HYDROGEN;
    }

    public boolean isCO2()
    {
        return elementName == structure.ElementName.CARBON &&
                bonds.size() == 2 &&
                bonds.get(0).getAnother(this).elementName == structure.ElementName.OXYGEN &&
                bonds.get(1).getAnother(this).elementName == structure.ElementName.OXYGEN;
    }*/
}
