package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class Secao {
    protected String id;
    protected ArrayList<Trilha> trilhas;
    protected int inicio, posicaoAtual;
    protected CondicaoTerminoSecao condicaoTermino;
    
    Secao(String id, int posicao) {
        this.id = id;
        this.inicio = posicao;
        this.posicaoAtual = posicao;
        this.trilhas = new ArrayList<Trilha>();
        this.condicaoTermino = null;
    }
    
    public void defineCondicaoTermino(CondicaoTerminoSecao c) {
        this.condicaoTermino = c;
        this.condicaoTermino.defineSecao(this);
    }
    
    public int retornaInicio() {
        return this.inicio;
    }
    
    public void defineNovaPosicao(int posicao) {
        this.posicaoAtual = posicao;
    }
    
    public boolean adicionaTrilha(Trilha t) {
        this.trilhas.add(t);
        return true;
    }
    
    public ArrayList<Nota> geraNotas() {
        ArrayList<Nota> notas = new ArrayList<Nota>();
        Trilha trilhaSelecionada;
        int posicaoFim, novaPosicaoAtual;
        
        for(Trilha t : this.trilhas) {
          t.selecionaPadrao();
        }
        posicaoFim = condicaoTermino.obtemFinal();
        while(posicaoFim == -1 || posicaoFim > posicaoAtual) {
          trilhaSelecionada = null;
          for(Trilha t : this.trilhas) {
            if(trilhaSelecionada == null || t.buscaFinal() < trilhaSelecionada.buscaFinal())
              trilhaSelecionada = t;
          }
          
          if(posicaoFim == -1 || !condicaoTermino.interrompeTrilhas())
            notas.addAll(trilhaSelecionada.geraNotas());
          else
            notas.addAll(trilhaSelecionada.geraNotas(posicaoFim - trilhaSelecionada.buscaPosicaoAtual()));

          trilhaSelecionada.selecionaPadrao();
          novaPosicaoAtual = trilhaSelecionada.buscaPosicaoAtual();
          for(Trilha t : this.trilhas) {
            if(t.buscaPosicaoAtual() < novaPosicaoAtual)
              novaPosicaoAtual = t.buscaPosicaoAtual();
          }
          posicaoAtual = novaPosicaoAtual;
        }
        
        return notas;
    }
}
