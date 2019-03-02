package structure;

import java.awt.*;
import java.io.Serializable;

public class Bond implements Serializable
{
    private Element start;
    private Element end;
    private int bondType = 1;
    private static final int LINE_WIDTH = 5;

    public Bond(Element element1, Element element2)
    {
        start = element1;
        end = element2;
        element1.addDependent(element2);
        element2.addDependent(element1);
        element1.removeHalfBond();
        element2.removeHalfBond();
    }

    public boolean upgrade()
    {
        if(bondType < 3)
        {
            bondType++;
            start.removeHalfBond();
            end.removeHalfBond();
            return true;
        }
        return false;
    }

    //true if needs to be deleted from the list
    public boolean downgrade()
    {
        bondType --;
        start.addHalfBond();
        end.addHalfBond();
        if(bondType == 0)
        {
            start.removeDependent(end);
            end.removeDependent(start);
            return true;
        }
        return false;
    }

    public void delete()
    {
        while (!downgrade());
    }

    public void draw(Graphics g)
    {
        Point position = start.getCenter();
        Point point = end.getCenter();
        Graphics2D gr = (Graphics2D)g;
        gr.setColor(Color.WHITE);
        switch (bondType)
        {
            case 2:
                gr.setColor(Color.WHITE);
                gr.setStroke(new BasicStroke(3 * LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                gr.drawLine(position.x, position.y, point.x, point.y);
                gr.setColor(Color.BLACK);
                break;
            case 3:
                gr.setColor(Color.WHITE);
                gr.setStroke(new BasicStroke(5 * LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                gr.drawLine(position.x, position.y, point.x, point.y);
                gr.setColor(Color.BLACK);
                gr.setStroke(new BasicStroke(3 * LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                gr.drawLine(position.x, position.y, point.x, point.y);
                gr.setColor(Color.WHITE);
        }
        gr.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        gr.drawLine(position.x, position.y, point.x, point.y);
    }

    public boolean connects(Element element)
    {
        return element == start || element == end;
    }

    public boolean contains(Point point)
    {
        Point start = this.start.getCenter();
        Point end = this.end.getCenter();
        return point.distance(start) + point.distance(end) < start.distance(end) + LINE_WIDTH;
    }

    public Element getAnother(Element element)
    {
        if(start == element) return end;
        if(end == element) return start;
        return null;
    }

    public boolean isDouble()
    {
        return bondType == 2 &&
                start.elementName == ElementName.CARBON &&
                end.elementName == ElementName.CARBON;
    }

    public boolean isTriple()
    {
        return bondType == 3 &&
                start.elementName == ElementName.CARBON &&
                end.elementName == ElementName.CARBON;
    }

    public boolean links(Element element1, Element element2)
    {
        return start == element1 && end == element2 ||
                start == element2 && end == element1;
    }
}
