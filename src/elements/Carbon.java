package elements;

import drawing.Canvas;

import java.awt.*;

public class Carbon extends Element
{
    public Carbon(Point point, double angle)
    {
        elementName = ElementName.CARBON;
        valency = 4;
        bounds = new Rectangle(point.x - Canvas.carbonImage.getWidth() / 2, point.y - Canvas.carbonImage.getHeight() / 2, Canvas.carbonImage.getWidth(), Canvas.carbonImage.getHeight());
        createBonds(angle);
    }


    @Override
    public void draw(Graphics g)
    {
        g.drawImage(Canvas.carbonImage, bounds.x, bounds.y, null);
    }

    @Override
    public boolean linkable(ElementName name)
    {
        return true;
    }
}