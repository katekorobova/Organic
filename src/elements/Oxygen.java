package elements;

import drawing.Canvas;

import java.awt.*;

public class Oxygen extends Element
{
    public Oxygen(Point point, double angle)
    {
        elementName = ElementName.OXYGEN;
        valency = 2;
        bounds = new Rectangle(point.x - Canvas.oxygenImage.getWidth() / 2, point.y - Canvas.oxygenImage.getHeight() / 2, Canvas.oxygenImage.getWidth(), Canvas.oxygenImage.getHeight());
        createBonds(angle);
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(Canvas.oxygenImage, bounds.x, bounds.y, null);
    }

    @Override
    public boolean linkable(ElementName name)
    {
        switch (name)
        {
            case CARBON:
            case HYDROGEN:
                return true;
            default: return false;
        }
    }
}
