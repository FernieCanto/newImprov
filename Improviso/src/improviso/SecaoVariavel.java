package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class SecaoVariavel extends Secao {
    protected ArrayList<Boolean> trilhasTerminadas;
    Integer posicaoFinal = null;
    
    SecaoVariavel() {
        super();
        posicaoFinal = null;
        trilhasTerminadas = new ArrayList<Boolean>();
    }
    
    @Override
    public boolean adicionaTrilha(Trilha t) {
        trilhasTerminadas.add(false);
        return super.adicionaTrilha(t);
    }

    @Override
    protected void processaMensagem(MensagemGrupo mensagem) {
        Integer novaPosicaoFinal = null;
        if(mensagem.obrigaFim) {
            novaPosicaoFinal = new Integer(this.trilhaSelecionada.buscaFinal());
        }
        else if(mensagem.sinalizaFim) {
            int maiorFinal = 0;
            boolean todasTrilhasTerminadas = true;
            
            trilhasTerminadas.set(indiceTrilhaSelecionada, true);
            for(int i = 0; i < trilhas.size(); i++) {
                if(trilhasTerminadas.get(i)) {
                    if(trilhas.get(i).buscaFinal() > maiorFinal)
                        maiorFinal = trilhas.get(i).buscaFinal();
                }
                else
                    todasTrilhasTerminadas = false;
            }
            if(todasTrilhasTerminadas)
                novaPosicaoFinal = new Integer(maiorFinal);
        }
        if((novaPosicaoFinal != null) && ((posicaoFinal == null) || (novaPosicaoFinal < posicaoFinal)))
            posicaoFinal = novaPosicaoFinal;
    }

    @Override
    protected Integer obtemFinal() {
        System.out.println("Posição final: "+posicaoFinal);
        return posicaoFinal;
    }
}
