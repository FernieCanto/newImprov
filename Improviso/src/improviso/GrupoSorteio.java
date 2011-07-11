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
public class GrupoSorteio extends Grupo {
    ArrayList<Integer> probsAcumuladas = new ArrayList<Integer>();
    int probabilidadeMax = 0;
    Random rand = null;
    
    @Override
    public boolean selecionaGrupo() {
        int probAtual;
        int selecao = this.rand.nextInt(this.probabilidadeMax);
        Iterator<Integer> iterProbs = this.probsAcumuladas.iterator();
        int index = 0;
        while(iterProbs.hasNext()) {
            probAtual = iterProbs.next().intValue();
            if(selecao < probAtual)
                break;
            else
                index++;
        }
        this.grupoSelecionado = this.filhos.get(index);
        if(!this.grupoSelecionado.ehFolha())
            this.grupoSelecionado.selecionaGrupo();
        return true;
    }
    
    public void defineSemente(long semente) {
        if(this.rand == null)
            this.rand = new Random(semente);
        else
            this.rand.setSeed(semente);
    }

    public boolean adicionaFilho(Grupo g, int probabilidade) {
        if(super.adicionaFilho(g)) {
            this.probabilidadeMax += probabilidade;
            this.probsAcumuladas.add(new Integer(this.probabilidadeMax));
            return true;
        }
        else
            return false;
    }    
}
