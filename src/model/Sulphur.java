package model;

import java.awt.*;

public class Sulphur extends Element
{
    public Sulphur(Point point)
    {
        elementName = ElementName.SULPHUR;
        image = sulphurImage;
        valency = 2;
        setBounds(point);
    }
}
