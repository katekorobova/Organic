package bonds;

import elements.Element;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class UnfilledBond implements Serializable
{
    private Element element;
    private Point end = new Point();
    private double angle;
    private double length;
    private static final int INITIAL_LENGTH = 100;
    private static final int RADIUS = 10;
    private static final int LINE_WIDTH = 5;

    public UnfilledBond(Element el, double ang)
    {
        element = el;
        angle = ang;
        length = INITIAL_LENGTH;
        adjustEnd();
    }

    public void draw(Graphics g)
    {
        Point position = element.getCenter();
        Graphics2D gr = (Graphics2D)g;
        gr.setColor(Color.WHITE);
        gr.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        gr.drawLine(position.x, position.y, end.x, end.y);
        gr.fillOval(end.x - RADIUS, end.y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }

    public void adjustEnd()
    {
        Point position = element.getCenter();
        end.x = position.x + (int)(length * Math.cos(angle));
        end.y = position.y + (int)(length * Math.sin(angle));
    }

    private void adjustAngle()
    {
        Point position = element.getCenter();
        angle = Math.atan2(end.y - position.y, end.x - position.x);
        length = position.distance(end);
    }

    public void moveTo(Point point)
    {

        end = point;
        adjustAngle();
    }

    public Element getElement()
    {
        return element;
    }

    public boolean contains(Point point)
    {
        return end.distance(point) < RADIUS;
    }

    public boolean mergeable(UnfilledBond bond)
    {
        return element != bond.element && element.linkable(bond.element.elementName);
    }

    public FilledBond merge(UnfilledBond bond)
    {
        element.getUnfilledBonds().remove(this);
        bond.element.getUnfilledBonds().remove(bond);
        for(FilledBond filledBond: element.getFilledBonds())
        {
            if(filledBond.connects(bond.element))
            {
                filledBond.upgrade();
                return null;
            }
        }
        return new FilledBond(element, bond.element);
    }

    public double getAngle()
    {
        return angle;
    }
}
