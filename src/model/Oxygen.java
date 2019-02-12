package model;

import java.awt.*;

public class Oxygen extends Element
{
    public Oxygen(Point point)
    {
        elementName = ElementName.OXYGEN;
        image = oxygenImage;
        valency = 2;
        setBounds(point);
    }
}
