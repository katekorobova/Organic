package elements;

import bonds.UnfilledBond;
import drawing.Canvas;

import java.awt.*;
import java.util.List;

public class Hydrogen extends Element
{
    public Hydrogen(Point point, java.util.List<UnfilledBond> bonds, List<Element> elements)
    {
        valency = 1;
        bounds = new Rectangle(point.x - Canvas.hydrogenImage.getWidth() / 2, point.y - Canvas.hydrogenImage.getHeight() / 2, Canvas.hydrogenImage.getWidth(), Canvas.hydrogenImage.getHeight());
        addBonds(bonds);
        elements.add(this);
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(Canvas.hydrogenImage, bounds.x, bounds.y, null);
    }
}
