package model;

import java.awt.*;

public class Phosphorus extends Element
{
    public Phosphorus(Point point)
    {
        elementName = ElementName.PHOSPHORUS;
        image = phosphorusImage;
        valency = 5;
        setBounds(point);
    }
}
