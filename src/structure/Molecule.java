package structure;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Molecule implements Serializable
{
    private ArrayList<Element> elements = new ArrayList<>();
    private ArrayList<Bond> bonds = new ArrayList<>();
    private Element active;
    private boolean isCyclic;
    private int carbons;
    private int hydrogens;
    private int oxygens;

    //This is a big and ugly function that gives a description of the molecule
    public String analyse()
    {
        if(isEmpty()) return "Полотно пусто";
        if(!isComplete()) return "Молекула не закончена";
        if (!isSingle()) return "На полотне больше одной молекулы.";
        String s = getFormula();
        if (isH2O()) return s + "\nЭто же просто вода.";
        if (isCO2()) return s + "\nЭто же просто углекислый газ.";
        if (isCyclic)
        {
            s += "\nЭто соединение циклическое.";
        }
        if (oxygens > 0)
        {
            s += "\nСодержит функциональные группы:";
            if (isAcid()) s += "\n  • карбоновых кислот";
            if (isAldehyde()) s += "\n  • альдегидов";
            if (isKetone()) s += "\n  • кетонов";
            if (isAlcohol()) s += "\n  • спиртов";
            if (isEther()) s += "\n  • эфиров";
            s += '.';

            int doubles = doubleBonds();
            int triples = tripleBonds();
            if(doubles > 0 || triples > 0)
            {
                s += "\nСодержит кратные связи между атомами углерода:";
                if(doubles > 0) s += "\n  • двойные";
                if(triples > 0) s += "\n  • тройные";
                s += '.';
            }
        }
        else
        {
            int doubles = doubleBonds();
            int triples = tripleBonds();
            if(hydrogens == 0)
            {
                s += "\nЭто какая-то упоротая аллотропическая модификация углерода";
                if(doubles > 0 || triples > 0)
                {
                    s += "\nСодержит кратные связи:";
                    if(doubles > 0) s += "\n  • двойные";
                    if(triples > 0) s += "\n  • тройные";
                    s += '.';
                }
            }
            else
            {
                if (isCyclic)
                {
                    s += "\nЭто углеводород.";
                    if (doubles > 0 || triples > 0)
                    {
                        s += "\nСодержит кратные связи между атомами углерода:";
                        if (doubles > 0) s += "\n  • двойные";
                        if (triples > 0) s += "\n  • тройные";
                        s += '.';
                    }
                }
                else
                {
                    if (triples == 0)
                    {
                        switch (doubles)
                        {
                            case 0:
                                return s + "\nПринадлежит к классу алканов.";
                            case 1:
                                return s + "\nПринадлежит к классу алкенов.";
                            case 2:
                                return s + "\nПринадлежит к классу алкадиенов.";
                            case 3:
                                return s + "\nПринадлежит к классу алкатриенов.";
                            default:
                                return s + "\nЭто углеводород со множеством двойных связей между атомами углерода.";
                        }
                    }
                    else if (doubles == 0)
                    {
                        switch (triples)
                        {
                            case 1:
                                return s + "\nПринадлежит к классу алкинов.";
                            case 2:
                                return s + "\nПринадлежит к классу алкадиинов.";
                            case 3:
                                return s + "\nПринадлежит к классу алкатриинов.";
                            default:
                                return s + "\nЭто углеводород со множеством тройных связей между атомами углерода.";
                        }
                    }
                    else return s + "\nЭто углеводород с двойными и тройными связями между атомами углерода.";
                }
            }
        }
        return s;
    }

    public void moveActive(Point point)
    {
        if(active != null) active.moveTo(point);
    }

    public void addElement(ElementName name, Point point)
    {
        elements.add(new Element(name, point));
        switch (name)
        {
            case CARBON:
                carbons++;
                break;
            case HYDROGEN:
                hydrogens++;
                break;
            case OXYGEN:
                oxygens++;
                break;
        }
    }

    //Delete an element or a bond that contains this point
    public void deleteByPoint(Point point)
    {
        Element element = elementByPoint(point);
        if(element != null && !element.hasActiveBond())
        {
            for(int i = bonds.size() - 1; i >= 0; i--)
            {
                Bond bond2 = bonds.get(i);
                if(bond2.connects(element))
                {
                    bond2.delete();
                    bonds.remove(bond2);
                }
            }
            elements.remove(element);
            switch (element.elementName)
            {
                case CARBON:
                    carbons--;
                    break;
                case HYDROGEN:
                    hydrogens--;
                    break;
                case OXYGEN:
                    oxygens--;
                    break;
            }
            return;
        }
        Bond bond = bondByPoint(point);
        if(bond != null)
        {
            if(bond.downgrade()) bonds.remove(bond);
        }
    }

    public void setActive(Point point)
    {
        active = elementByPoint(point);
    }

    public void releaseActive(Point point)
    {
        if(active != null && active.hasActiveBond())
        {
            Element element = elementByPoint(point);
            if(element != null && element.hasActiveBond())
            {
                link(active, element);
            }
        }
        active = null;
    }

    public void draw(Graphics g)
    {
        for(Bond bond: bonds) bond.draw(g);
        for(Element element: elements) element.draw(g);
    }

    //This is where bonds are created
    private void link(Element element1, Element element2)
    {
        if(element1.elementName == ElementName.OXYGEN &&
                element2.elementName == ElementName.OXYGEN) return;
        if(element1.elementName == ElementName.HYDROGEN &&
                element2.elementName == ElementName.HYDROGEN) return;
        Bond bond = bondByElements(element1, element2);
        if(bond == null) bonds.add(new Bond(element1, element2));
        else bond.upgrade();
    }

    //Get all bonds that connect this element with others
    private ArrayList<Bond> bondsByElement(Element element)
    {
        ArrayList<Bond> newBonds = new ArrayList<>(bonds);
        newBonds.removeIf(bond -> !bond.connects(element));
        return newBonds;
    }

    private Bond bondByElements(Element element1, Element element2)
    {
        for(Bond bond: bonds)
        {
            if(bond.links(element1, element2)) return bond;
        }
        return null;
    }

    //Get a bond that contains this point
    private Bond bondByPoint(Point point)
    {
        for(Bond bond: bonds)
        {
            if(bond.contains(point)) return bond;
        }
        return null;
    }

    //Get an element that contains this point
    private Element elementByPoint(Point point)
    {
        for(Element element: elements)
        {
            if (element != active && element.contains(point))
            {
                return element;
            }
        }
        return null;
    }
    private boolean isEmpty()
    {
        return elements.isEmpty();
    }

    //Checks if we have any free valencies left
    private boolean isComplete()
    {
        for(Element element: elements)
        {
            if(element.hasHalfBonds()) return false;
        }
        return true;
    }

    //Checks if there is only one single molecule
    private boolean isSingle()
    {
        ArrayList<Element> passed = new ArrayList<>();
        isCyclic = collectConnected(elements.get(0), null, passed);
        return passed.size() == elements.size();
    }

    //Collects all the elements reachable from this one
    private boolean collectConnected(Element element, Bond lastBond, ArrayList<Element> passed)
    {
        passed.add(element);
        boolean cyclic = false;
        ArrayList<Bond> myBonds = bondsByElement(element);
        for(Bond bond: myBonds)
        {
            if(bond == lastBond) continue;
            Element newElement = bond.getAnother(element);
            if(passed.contains(newElement))cyclic = true;
            else cyclic |= collectConnected(newElement, bond, passed);
        }
        return cyclic;
    }

    //Produces a chemical formula of the molecule
    private String getFormula()
    {
        String formula = "Химическая формула: ";
        if(carbons > 0)
        {
            formula += "C";
            if (carbons > 1) formula += carbons;
        }
        if(hydrogens > 0)
        {
            formula += "H";
            if(hydrogens > 1) formula += hydrogens;
        }
        if(oxygens > 0)
        {
            formula += "O";
            if(oxygens > 1) formula += oxygens;
        }
        formula += ".";
        return formula;
    }

    private boolean isH2O()
    {
        return carbons == 0 && oxygens == 1 && hydrogens == 2;
    }

    private boolean isCO2()
    {
        return carbons == 1 && oxygens == 2 && hydrogens == 0;
    }

    private boolean isAcid()
    {
        for(Element element: elements)
        {
            if(isCarboxylGroup(element)) return true;
        }
        return false;
    }

    private boolean isAldehyde()
    {
        for(Element element: elements)
        {
            if(isAldehydeGroup(element)) return true;
        }
        return false;
    }

    private boolean isKetone()
    {
        for(Element element: elements)
        {
            if(isKetoneGroup(element)) return true;
        }
        return false;
    }

    private boolean isAlcohol()
    {
        for(Element element: elements)
        {
            if(isAlcoholGroup(element)) return true;
        }
        return false;
    }

    private boolean isEther()
    {
        for(Element element: elements)
        {
            if(isEtherGroup(element)) return true;
        }
        return false;
    }

    private boolean isHydroxylGroup(Element element)
    {
        if (element.elementName == ElementName.OXYGEN)
        {
            ArrayList<Bond> myBonds = bondsByElement(element);
            if(myBonds.size() != 2) return false;
            myBonds.removeIf(bond -> bond.getAnother(element).elementName == ElementName.HYDROGEN);
            if(myBonds.size() != 1) return false;
            return myBonds.get(0).getAnother(element).elementName == ElementName.CARBON;
        }
        return false;
    }

    private boolean isAlcoholGroup(Element element)
    {
        if(element.elementName == ElementName.CARBON)
        {
            ArrayList<Bond> myBonds = bondsByElement(element);
            int size = myBonds.size();
            myBonds.removeIf(bond -> isHydroxylGroup(bond.getAnother(element)));
            if(size == myBonds.size()) return false;
            myBonds.removeIf(bond -> bond.getAnother(element).elementName != ElementName.OXYGEN);
            return myBonds.isEmpty();
        }
        return false;
    }

    private boolean isEtherGroup(Element element)
    {
        if(element.elementName == ElementName.OXYGEN)
        {
            ArrayList<Bond> myBonds = bondsByElement(element);
            return myBonds.size() == 2 &&
                    myBonds.get(0).getAnother(element).elementName == ElementName.CARBON &&
                    myBonds.get(1).getAnother(element).elementName == ElementName.CARBON;
        }
        return false;
    }

    private boolean isCarbonylGroup(Element element)
    {
        if(element.elementName == ElementName.OXYGEN)
        {
            return bondsByElement(element).size() == 1;
        }
        return false;
    }

    private boolean isAldehydeGroup(Element element)
    {
        if (element.elementName == structure.ElementName.CARBON)
        {
            ArrayList<Bond> myBonds = bondsByElement(element);
            if(myBonds.size() != 3) return false;
            myBonds.removeIf(bond -> isCarbonylGroup(bond.getAnother(element)));
            if(myBonds.size() != 2) return false;
            myBonds.removeIf(bond -> bond.getAnother(element).elementName == ElementName.HYDROGEN);
            return myBonds.size() < 2;
        }
        return false;
    }

    private boolean isKetoneGroup(Element element)
    {
        if(element.elementName == ElementName.CARBON)
        {
            ArrayList<Bond> myBonds = bondsByElement(element);
            if(myBonds.size() != 3) return false;
            myBonds.removeIf(bond -> isCarbonylGroup(bond.getAnother(element)));
            if(myBonds.size() != 2) return false;
            myBonds.removeIf(bond -> bond.getAnother(element).elementName == ElementName.CARBON);
            return myBonds.size() == 0;
        }
        return false;
    }

    private boolean isCarboxylGroup(Element element)
    {
        if(element.elementName == ElementName.CARBON)
        {
            ArrayList<Bond> myBonds = bondsByElement(element);
            if(myBonds.size() != 3) return false;
            myBonds.removeIf(bond -> isCarbonylGroup(bond.getAnother(element)));
            if(myBonds.size() != 2) return false;
            myBonds.removeIf(bond -> isHydroxylGroup(bond.getAnother(element)));
            if (myBonds.size() != 1) return false;
            return myBonds.get(0).getAnother(element).elementName != ElementName.OXYGEN;
        }
        return false;
    }

    private int doubleBonds()
    {
        int k = 0;
        for(Bond bond: bonds)
        {
            if(bond.isDouble()) k++;
        }
        return k;
    }

    private int tripleBonds()
    {
        int k = 0;
        for(Bond bond: bonds)
        {
            if(bond.isTriple()) k++;
        }
        return k;
    }
}
