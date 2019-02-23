package analysis;

import bonds.FilledBond;
import bonds.UnfilledBond;
import elements.Element;
import elements.ElementName;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Analyser
{
    private boolean isCyclic;

    private String getDescription()
    {
        if (isEmpty())
        {
            return "Полотно пусто.";
        }
        if (!isFinished())
        {
            return "Молекула не закончена.";
        }
        if (!isSingle())
        {
            return "На полотне больше одной молекулы.";
        }
        String s = getFormula();

        if (isWater())
        {
            return s + "\nЭто же просто вода.";
        }

        if (isCO2())
        {
            return s + "\nЭто же просто углекислый газ.";
        }
        if (isCyclic)
        {
            s += "\nЭто соединение циклическое.";
        }
        if (containsOxygen())
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
            if(isCyclic)
            {
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
        return s;
    }

    private String getFormula()
    {
        int carbons = 0, hydrogens = 0, oxygens = 0;
        for(Element element: Element.all)
        {
            switch (element.elementName)
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

    private boolean isEmpty()
    {
        return Element.all.isEmpty();
    }

    private boolean isFinished()
    {
        return UnfilledBond.all.isEmpty();
    }

    private boolean isSingle()
    {
        List<Element> passed = new ArrayList<>();
        isCyclic = collectConnected(Element.all.get(0), null, passed);
        return passed.size() == Element.all.size();
    }

    private boolean collectConnected(Element element, FilledBond lastBond, List<Element> passed)
    {
        passed.add(element);
        boolean cyclic = false;
        for(FilledBond bond: element.getFilledBonds())
        {
            if(bond == lastBond) continue;
            Element newElement = bond.getAnother(element);
            if(passed.contains(newElement))
            {
                cyclic = true;
            }
            else
            {
                cyclic |= collectConnected(newElement, bond, passed);
            }
        }
        return cyclic;
    }

    private boolean isWater()
    {
        if(Element.all.size() == 3)
        {
            for(Element element: Element.all)
            {
                if(element.isWater()) return true;
            }
        }
        return false;
    }

    private boolean isCO2()
    {
        if(Element.all.size() == 3)
        {
            for(Element element: Element.all)
            {
                if(element.isCO2()) return true;
            }
        }
        return false;
    }

    private boolean containsOxygen()
    {
        for(Element element: Element.all)
        {
            if(element.elementName == ElementName.OXYGEN) return true;
        }
        return false;
    }

    private boolean isAcid()
    {
        for(Element element: Element.all)
        {
            if(element.isCarboxylGroup()) return true;
        }
        return false;
    }

    private boolean isAldehyde()
    {
        for(Element element: Element.all)
        {
            if(element.isAldehydeGroup()) return true;
        }
        return false;
    }

    private boolean isKetone()
    {
        for(Element element: Element.all)
        {
            if(element.isKetoneGroup()) return true;
        }
        return false;
    }

    private boolean isAlcohol()
    {
        for(Element element: Element.all)
        {
            if(element.isAlcoholGroup()) return true;
        }
        return false;
    }

    private boolean isEther()
    {
        for(Element element: Element.all)
        {
            if(element.isEtherGroup()) return true;
        }
        return false;
    }

    private int doubleBonds()
    {
        int k = 0;
        for(FilledBond bond: FilledBond.all)
        {
            if(bond.isDouble()) k++;
        }
        return k;
    }

    private int tripleBonds()
    {
        int k = 0;
        for(FilledBond bond: FilledBond.all)
        {
            if(bond.isTriple()) k++;
        }
        return k;
    }

    public void analyse()
    {
        JFrame textFrame = new JFrame();
        JTextArea textArea = new JTextArea(getDescription());
        textArea.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        textFrame.add(textArea);
        textFrame.pack();
        textFrame.setLocationRelativeTo(null);
        textFrame.setVisible(true);
    }
}
