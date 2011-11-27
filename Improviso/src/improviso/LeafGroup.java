/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

/**
 * Implementa um Group Folha, que contém um Pattern que será devolvido
 * quando ele for selecionado.
 * @author fernando
 */
public class LeafGroup extends Group {
    LeafGroup(Pattern p) {
        super();
        selectedPattern = p;
    }
    
    @Override
    public boolean getIsLeaf() {
        return true;
    }
    
    @Override
    public void configureGroupXML(org.w3c.dom.Element e) {
        return;
    }
    
    @Override
    public Group selectGroup() {
        return null;
    }
}