/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;
import java.util.*;
/**
 *
 * @author fernando
 */
public class GrupoSorteio extends GrupoRepeticao {
    protected int probabilidadeFilho = 1;
    private ArrayList<Integer> probsAcumuladas = new ArrayList<Integer>();
    private int probabilidadeMax = 0;
    
    GrupoSorteio() {
        super();
    }
    
    @Override
    public void configuraGrupoXML(org.w3c.dom.Element elemento) {
        if(elemento.hasAttribute("probability"))
            probabilidadeFilho = Integer.parseInt(elemento.getAttribute("probability"));
        super.configuraGrupoXML(elemento);
    }
    
    @Override
    public boolean selecionaProximoGrupo() {
        int selecao;
        int index = 0;

        selecao = this.rand.nextInt(this.probabilidadeMax);
        for(int probAtual : this.probsAcumuladas) {
            if(selecao < probAtual)
                break;
            else
                index++;
        }
        this.grupoSelecionado = this.filhos.get(index);
        this.indiceGrupoSelecionado = index;
        return true;
    }

    @Override
    public boolean adicionaFilho(Grupo g) {
        if(super.adicionaFilho(g)) {
            this.probabilidadeMax += probabilidadeFilho;
            this.probsAcumuladas.add(new Integer(this.probabilidadeMax));
            return true;
        }
        else {
            return false;
        }
    }
}