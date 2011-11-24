package improviso;

import java.util.*;
import org.w3c.dom.Element;
/**
 * RESPONSABILIDADES DA TRILHA
 *  - Buscar próximo padrão e devolver notas (todas, ou com determinada duração)
 *  - Identificar corretamente o fim de sua execução
 * @author fernando
 */
public class Trilha {
    protected MensagemGrupo mensagem;
    protected Padrao padraoAtual;
    protected Grupo grupoRaiz;
    protected int posicaoAtual;
   
    /**
     * Cria uma nova Trilha.
     * @param g Grupo raíz que será associado à trilha.
     * @param posicao Posição em ticks da composição onde a trilha iniciará a executar.
     */
    Trilha(Grupo g) {
        this.grupoRaiz = g;
        this.posicaoAtual = 0;
    }

    static Trilha produzTrilhaXML(BibliotecaXML bibXML, Element elemento) {
        Trilha t;
        Grupo g;
        
        if(elemento.hasAttribute("group"))
            g = Grupo.produzGrupoXML(bibXML, bibXML.grupos.get(elemento.getAttribute("group")));
        else
            g = Grupo.produzGrupoXML(bibXML, (Element)elemento.getChildNodes().item(1));
        t = new Trilha(g);
        
        return t;
    }
    
    /**
     * Seleciona o próximo padrão a ser executado pela trilha, de acordo
     * com a configuração e organização dos grupos associados.
     */
    public MensagemGrupo executa() {
        mensagem = this.grupoRaiz.executa();
        padraoAtual = this.grupoRaiz.recuperaPadrao();
        return mensagem;
    }
    
    public void inicializa(int posicao) {
        this.posicaoAtual = posicao;
        this.grupoRaiz.zeraContadorExecucoes();
    }
    
    /**
     * Busca a posição da composição onde a trilha se encontra no momento.
     * @return Posição em ticks
     */
    public int buscaPosicaoAtual() {
      return posicaoAtual;
    }
    
    /**
     * Altera a posição onde a trilha deverá executar;
     * @param posicao Posição em ticks
     */
    public void definePosicaoAtual(int posicao) {
      posicaoAtual = posicao;
    }
    
    /**
     * Busca a posição onde o padrão atualmente selecionado irá terminar.
     * @return Posição em ticks
     */
    /* TODO: Final depende da mensagem recebida do grupo raiz */
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
    /* TODO: Notas geradas podem variar, de acordo com conexões ativas e
     * mensagens recebidas do grupo raíz
     */
    public ArrayList<Nota> geraNotas(double posicao) {
        return this.geraNotas(Padrao.INTEIRO, posicao);
    }
    
    /**
     * Produz a sequência de notas do padrão atualmente selecionado, avançando
     * a trilha para a posição onde o padrão terminou.
     * @param duracao A duração em ticks do padrão. As notas que excedam essa 
     * duração serão descartadas.
     * @return Sequência de notas geradas.
     */
    public ArrayList<Nota> geraNotas(int duracao, double posicao) {
        ArrayList<Nota> notas = padraoAtual.geraNotas(this.posicaoAtual, 0, duracao);
        this.posicaoAtual += padraoAtual.recuperaDuracao();
        return notas;
    }
}
