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
    private ArrayList<Integer> probsAcumuladas = new ArrayList<Integer>();
    private int probabilidadeMax = 0;
    
    GrupoSorteio(String id) {
        super(id);
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

    public boolean adicionaFilho(Grupo g, int probabilidade, int repeticao, float inercia) {
        if(super.adicionaFilho(g, repeticao, inercia)) {
            this.probabilidadeMax += probabilidade;
            this.probsAcumuladas.add(new Integer(this.probabilidadeMax));
            return true;
        }
        else {
            return false;
        }
    }    
}
