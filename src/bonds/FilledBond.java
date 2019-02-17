package bonds;

import elements.Element;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilledBond implements Serializable
{
    private Element element1;
    private Element element2;
    private int bondType = 1;
    private static final int LINE_WIDTH = 5;
    public static ArrayList<FilledBond> all = new ArrayList<>();

    public FilledBond(Element el1, Element el2)
    {
        element1 = el1;
        element2 = el2;
        element1.addFilledBond(this);
        element2.addFilledBond(this);
        all.add(this);
    }

    public boolean upgrade()
    {
        if(bondType < 3)
        {
            bondType++;
            return true;
        }
        return false;
    }

    public boolean downgrade(List<UnfilledBond> unfilledBonds)
    {
        if(bondType > 1)
        {
            bondType --;
            element1.addUnfilledBond(unfilledBonds);
            element2.addUnfilledBond(unfilledBonds);
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

    public void delete(List<FilledBond> filledBonds, List<UnfilledBond> unfilledBonds)
    {
        while (downgrade(unfilledBonds));
        element1.deleteFilledBond(this, unfilledBonds);
        element2.deleteFilledBond(this, unfilledBonds);
        filledBonds.remove(this);
    }
}
