package improviso;

import java.util.*;

/**
 *
 * @author fernando
 */
public class Padrao implements Cloneable {
    public static final int INTEIRO = -1;
  
    ArrayList<DefinicaoNota> notas;
    int duracao;
    
    Padrao(int duracao) {
        this.duracao = duracao;
        this.notas = new ArrayList<DefinicaoNota>();
    }
    
    public void adicionaNota(DefinicaoNota n) {
        this.notas.add(n);
    }
    
    public ArrayList<Nota> geraNotas(int inicio, double posicao, int duracao) {
        ArrayList<Nota> listaNotas = new ArrayList<Nota>();
        for(DefinicaoNota def : this.notas) {
          if(duracao == Padrao.INTEIRO)
            listaNotas.add(def.geraNota(inicio, posicao));
          else {
            if(def.inicio < duracao) {
              listaNotas.add(def.geraNota(inicio, posicao, duracao - (int)def.inicio));
            }
            else break;
          }
        }
        return listaNotas;
    }
    
    public int recuperaDuracao() {
        return this.duracao;
    }
}