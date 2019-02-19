package elements;

import drawing.Canvas;

import java.awt.*;

public class Oxygen extends Element
{
    public Oxygen(Point point)
    {
        valency = 2;
        bounds = new Rectangle(point.x - Canvas.oxygenImage.getWidth() / 2, point.y - Canvas.oxygenImage.getHeight() / 2, Canvas.oxygenImage.getWidth(), Canvas.oxygenImage.getHeight());
        addBonds();
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(Canvas.oxygenImage, bounds.x, bounds.y, null);
    }
}
