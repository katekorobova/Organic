package structure;

import java.awt.*;
import java.io.Serializable;

public class HalfBond implements Serializable
{
    private Point start;
    private Point end = new Point();
    private double angle;
    private double length;
    private static final int INITIAL_LENGTH = 100;
    private static final int RADIUS = 10;
    private static final int LINE_WIDTH = 5;

    public HalfBond(Point point, double ang)
    {
        start = point;
        angle = ang;
        length = INITIAL_LENGTH;
        adjustEnd();
    }

    public void draw(Graphics g)
    {
        Graphics2D gr = (Graphics2D)g;
        gr.setColor(Color.WHITE);
        gr.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        gr.drawLine(start.x, start.y, end.x, end.y);
        gr.fillOval(end.x - RADIUS, end.y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }

    public void adjustEnd()
    {
        end.x = start.x + (int)(length * Math.cos(angle));
        end.y = start.y + (int)(length * Math.sin(angle));
    }

    private void adjustAngle()
    {
        angle = Math.atan2(end.y - start.y, end.x - start.x);
        length = start.distance(end);
    }

    public void moveStart(Point point)
    {
        start = point;
        adjustEnd();
    }

    public void moveEnd(Point point)
    {

        end = point;
        adjustAngle();
    }

    public boolean contains(Point point)
    {
        return end.distance(point) < RADIUS;
    }

}
