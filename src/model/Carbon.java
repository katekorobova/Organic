package model;

import java.awt.*;

public class Carbon extends Element
{
    public Carbon(Point point)
    {
        elementName = ElementName.CARBON;
        image = carbonImage;
        valency = 4;
        setBounds(point);
    }
}
