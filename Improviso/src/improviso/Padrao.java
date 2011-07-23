package improviso;

import java.util.*;

/**
 *
 * @author fernando
 */
public class Padrao {
    public static final int INTEIRO = -1;
  
    String id;
    ArrayList<DefinicaoNota> notas;
    Compasso compasso;
    int numCompassos;
    
    Padrao(String id, Compasso compasso, int numCompassos) {
        this.id = id;
        this.compasso = compasso;
        this.numCompassos = numCompassos;
        this.notas = new ArrayList<DefinicaoNota>();
    }
    
    public void adicionaNota(DefinicaoNota n) {
        this.notas.add(n);
    }
    
    public ArrayList<Nota> geraNotas(int inicio, float posicao, int duracao) {
        ArrayList<Nota> listaNotas = new ArrayList<Nota>();
        for(DefinicaoNota def : this.notas) {
          if(duracao == Padrao.INTEIRO)
            listaNotas.add(def.geraNota(inicio, posicao));
          else {
            if(def.inicio < duracao) {
              listaNotas.add(def.geraNota(inicio, posicao, duracao - def.inicio));
            }
            else break;
          }
        }
        return listaNotas;
    }
    
    public int recuperaDuracao() {
        return this.compasso.calculaDuracao() * this.numCompassos;
    }
}