package elements;

import drawing.Canvas;

import java.awt.*;

public class Hydrogen extends Element
{
    public Hydrogen(Point point, double angle)
    {
        elementName = ElementName.HYDROGEN;
        valency = 1;
        bounds = new Rectangle(point.x - Canvas.hydrogenImage.getWidth() / 2, point.y - Canvas.hydrogenImage.getHeight() / 2, Canvas.hydrogenImage.getWidth(), Canvas.hydrogenImage.getHeight());
        createBonds(angle);
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(Canvas.hydrogenImage, bounds.x, bounds.y, null);
    }

    @Override
    public boolean linkable(ElementName name)
    {
        return name != ElementName.HYDROGEN;
    }
}
