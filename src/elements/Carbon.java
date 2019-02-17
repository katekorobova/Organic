package elements;

import bonds.UnfilledBond;
import drawing.Canvas;

import java.awt.*;
import java.util.List;

public class Carbon extends Element
{
    public Carbon(Point point, List<UnfilledBond> bonds, List<Element> elements)
    {
        valency = 4;
        bounds = new Rectangle(point.x - Canvas.carbonImage.getWidth() / 2, point.y - Canvas.carbonImage.getHeight() / 2, Canvas.carbonImage.getWidth(), Canvas.carbonImage.getHeight());
        addBonds(bonds);
        elements.add(this);
    }


    @Override
    public void draw(Graphics g)
    {
        g.drawImage(Canvas.carbonImage, bounds.x, bounds.y, null);
    }
}