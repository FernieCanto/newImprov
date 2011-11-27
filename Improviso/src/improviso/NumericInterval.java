package improviso;

import java.util.Random;

/**
 *
 * @author fernando
 */
public class NumericInterval {
    public int valor, valorVar, valorFim, valorFimVar;
    Random rand = null;
    
    NumericInterval(int val, int valMax, int valFim, int valMaxFim) {
        this.valor = val;
        this.valorVar = (valMax - val);

        this.valorFim = valFim;
        this.valorFimVar = (valMaxFim - valFim);
    }
    
    public void defineSemente(long semente) {
        if(rand == null)
            rand = new Random(semente);
        else
            rand.setSeed(semente);
    }
    
    protected Random buscaRandom() {
        if(rand == null)
            rand = new Random();
        return rand;
    }
    
    int geraValor() {
        return geraValor(0.0, buscaRandom());
    }
    
    int geraValor(float posicao) {
        return geraValor(posicao, buscaRandom());
    }
    
    int geraValor(Random rand) {
        return geraValor(0.0, rand);
    }
    
    int geraValor(double posicao, Random rand) {
        int retorno;
        
        retorno  = valor + (int)( (valorFim - valor) * posicao );
        retorno += rand.nextInt(valorVar + (int)( (valorFimVar - valorVar) * posicao) + 1);
        
        return retorno;
    }
}
