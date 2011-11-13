/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

/**
 * Implementa um Grupo Folha, que contém um Padrao que será devolvido
 * quando ele for selecionado.
 * @author fernando
 */
public class GrupoFolha extends Grupo {
    GrupoFolha(Padrao p) {
        super();
        padraoSelecionado = p;
    }
    
    @Override
    public boolean ehFolha() {
        return true;
    }
    
    @Override
    public void configuraGrupoXML(org.w3c.dom.Element e) {
        return;
    }
    
    @Override
    public Grupo selecionaGrupo() {
        return null;
    }
}