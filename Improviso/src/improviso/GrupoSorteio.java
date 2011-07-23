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
    private ArrayList<Integer> probsAcumuladas = new ArrayList<Integer>();
    private int probabilidadeMax = 0;
    private Random rand = null;
    
    @Override
    public boolean selecionaGrupo() {
        int selecao;
        int index = 0;
        if(this.rand == null)
          this.rand = new Random();

        selecao = this.rand.nextInt(this.probabilidadeMax);
        for(int probAtual : this.probsAcumuladas) {
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
