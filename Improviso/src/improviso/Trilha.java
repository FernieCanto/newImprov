package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class Trilha {
    protected Grupo grupoRaiz;
    protected int posicaoAtual;
    
    Trilha(Grupo g, int posicao) {
        this.grupoRaiz = g;
        this.posicaoAtual = posicao;
    }
    
    public boolean selecionaPadrao() {
        this.grupoRaiz.selecionaGrupo();
        return true;
    }
    
    public ArrayList<Nota> geraNotas() {
        Padrao p = this.grupoRaiz.recuperaPadraoSelecionado();
        ArrayList<Nota> notas = p.geraNotas(this.posicaoAtual, 0);
        this.posicaoAtual += p.recuperaDuracao();
        return notas;
    }
}
