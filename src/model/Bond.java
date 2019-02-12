package model;

import java.awt.*;

public class Bond
{
    private Element element1;
    private Element element2;
    private int bondType;

    private static final int LINE_WIDTH = 5;

    public Bond(int type, Element elem1, Element elem2)
    {
        bondType = type;
        element1 = elem1;
        element2 = elem2;
    }
    public void draw(Graphics g)
    {
        drawGhost(g, bondType, element1, element2.getCenter());
    }

    public static void drawGhost(Graphics g, int type, Element element, Point point)
    {
        Point position = element.getCenter();
        Graphics2D gr = (Graphics2D)g;
        gr.setColor(Color.WHITE);
        switch (type)
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
        element.draw(g);
    }

    public void delete()
    {
        element1.unlink(bondType, element2);
    }
}
