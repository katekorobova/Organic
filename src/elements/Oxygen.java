package elements;

import bonds.UnfilledBond;
import drawing.Canvas;

import java.awt.*;
import java.util.List;

public class Oxygen extends Element
{
    public Oxygen(Point point, java.util.List<UnfilledBond> bonds, List<Element> elements)
    {
        valency = 2;
        bounds = new Rectangle(point.x - Canvas.oxygenImage.getWidth() / 2, point.y - Canvas.oxygenImage.getHeight() / 2, Canvas.oxygenImage.getWidth(), Canvas.oxygenImage.getHeight());
        addBonds(bonds);
        elements.add(this);
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(Canvas.oxygenImage, bounds.x, bounds.y, null);
    }
}
