package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class Trilha {
    protected Padrao padraoAtual;
    protected Grupo grupoRaiz;
    protected int posicaoAtual;
   
    /**
     * Cria uma nova Trilha.
     * @param g Grupo raíz que será associado à trilha.
     * @param posicao Posição em ticks da composição onde a trilha iniciará a executar.
     */
    Trilha(Grupo g, int posicao) {
        this.grupoRaiz = g;
        this.posicaoAtual = posicao;
    }
    
    /**
     * Seleciona o próximo padrão a ser executado pela trilha, de acordo
     * com a configuração e organização dos grupos associados.
     */
    public void selecionaPadrao() {
        this.grupoRaiz.selecionaGrupo();
        padraoAtual = this.grupoRaiz.recuperaPadraoSelecionado();
    }
    
    /**
     * Busca a posição da composição onde a trilha se encontra no momento.
     * @return Posição em ticks
     */
    public int buscaPosicaoAtual() {
      return posicaoAtual;
    }
    
    /**
     * Busca a posição onde o padrão atualmente selecionado irá terminar.
     * @return Posição em ticks
     */
    public int buscaFinal() {
      return posicaoAtual + padraoAtual.recuperaDuracao();
    }
    
    /**
     * Produz a sequência de notas do padrão atualmente selecionado, avançando
     * a trilha para a posição onde o padrão terminou.
     * @param duracao A duração em ticks do padrão. As notas que excedam essa 
     * duração serão descartadas.
     * @return Sequência de notas geradas.
     */
    public ArrayList<Nota> geraNotas() {
        return this.geraNotas(Padrao.INTEIRO);
    }
    
    /**
     * Produz a sequência de notas do padrão atualmente selecionado, avançando
     * a trilha para a posição onde o padrão terminou.
     * @param duracao A duração em ticks do padrão. As notas que excedam essa 
     * duração serão descartadas.
     * @return Sequência de notas geradas.
     */
    public ArrayList<Nota> geraNotas(int duracao) {
        ArrayList<Nota> notas = padraoAtual.geraNotas(this.posicaoAtual, 0, duracao);
        this.posicaoAtual += padraoAtual.recuperaDuracao();
        return notas;
    }
}
