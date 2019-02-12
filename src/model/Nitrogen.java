package model;

import java.awt.*;

public class Nitrogen extends Element
{
    public Nitrogen(Point point)
    {
        elementName = ElementName.NITROGEN;
        image = nitrogenImage;
        valency = 3;
        setBounds(point);
    }
}
