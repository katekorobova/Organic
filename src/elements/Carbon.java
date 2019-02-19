package elements;

import drawing.Canvas;

import java.awt.*;

public class Carbon extends Element
{
    public Carbon(Point point)
    {
        valency = 4;
        bounds = new Rectangle(point.x - Canvas.carbonImage.getWidth() / 2, point.y - Canvas.carbonImage.getHeight() / 2, Canvas.carbonImage.getWidth(), Canvas.carbonImage.getHeight());
        addBonds();
    }


    @Override
    public void draw(Graphics g)
    {
        g.drawImage(Canvas.carbonImage, bounds.x, bounds.y, null);
    }
}