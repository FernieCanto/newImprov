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
        
        return notas;
    }
}
