package model;

import java.awt.*;

public class Hydrogen extends Element
{
    public Hydrogen(Point point)
    {
        elementName = ElementName.HYDROGEN;
        image = hydrogenImage;
        valency = 1;
        setBounds(point);
    }
}
