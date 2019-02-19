package bonds;

import elements.Element;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class FilledBond implements Serializable
{
    private Element element1;
    private Element element2;
    private int bondType = 1;
    private static final int LINE_WIDTH = 5;
    public static ArrayList<FilledBond> all = new ArrayList<>();

    public FilledBond(UnfilledBond bond1, UnfilledBond bond2)
    {
        element1 = bond1.getElement();
        element2 = bond2.getElement();
        element1.addFilledBond(this);
        element2.addFilledBond(this);
        bond1.delete();
        bond2.delete();
        all.add(this);
    }

    public boolean upgrade(UnfilledBond bond1, UnfilledBond bond2)
    {
        if(bondType < 3)
        {
            bondType++;
            bond1.delete();
            bond2.delete();
            return true;
        }
        return false;
    }

    public boolean downgrade()
    {
        if(bondType > 0)
        {
            bondType --;
            element1.addUnfilledBond();
            element2.addUnfilledBond();
            if(bondType == 0)
            {
                element1.removeDependent(element2);
                element2.removeDependent(element1);
                element1.removeFilledBond(this);
                element2.removeFilledBond(this);
                all.remove(this);
            }
            return true;
        }
        return false;
    }

    public void draw(Graphics g)
    {
        Point position = element1.getCenter();
        Point point = element2.getCenter();
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
        return element == element1 || element == element2;
    }

    public void delete()
    {
        while (downgrade());
    }

    public boolean contains(Point point)
    {
        Point start = element1.getCenter();
        Point end = element2.getCenter();
        return point.distance(start) + point.distance(end) < start.distance(end) + LINE_WIDTH;
    }
}
