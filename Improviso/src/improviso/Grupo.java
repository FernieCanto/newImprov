package improviso;
import java.util.*;

/**
 * Implementa um grupo genérico, incluindo o algoritmo de adição de grupos
 * filhos. Um grupo pode ser um Nodo, contendo diversos filhos (sub-itens) ou
 * uma Folha, contendo um Padrao. Quando um grupo é executado, um novo sub-item
 * é selecionado, e o Padrao correspondente pode ser obtido com o método
 * recuperaPadrao. Uma MensagemGrupo será retornado, contendo sinalizações de
 * fim de execução ou interrupções da seção.
 * RESPONSABILIDADES DO GRUPO:
 *  - Selecionar sub-itens seguindo um algoritmo próprio
 *  - Retornar mensagem com informações de fim
 * @author fernando
 */
public abstract class Grupo {
    protected String identificador;
    
    protected ArrayList<Grupo> filhos;
    protected Padrao padraoSelecionado = null;
    protected Integer indiceGrupoSelecionado = null;
    protected Random rand = null;
    protected int numExecucoes = 0;
        
    protected Integer   minExecucoesSinaliza    = new Integer(0);
    protected Double    probabilidadeSinaliza   = new Double(1.0);
    protected Integer   maxExecucoesSinaliza    = new Integer(100);
    protected boolean   desabilitaTrilha        = false;
    
    protected Integer   minExecucoesObrigaFim   = null;
    protected Double    probabilidadeObrigaFim  = null;
    protected Integer   maxExecucoesObrigaFim   = null;
    protected boolean   interrompeSecao         = false;
    
    Grupo(String id) {
        identificador = id;
        filhos = new ArrayList<Grupo>();
    }
    
    public void defineSemente() {
        rand = new Random();
    }
    
    public void defineSemente(long semente) {
        if(rand == null)
            rand = new Random(semente);
        else
            rand.setSeed(semente);
    }
    
    public void zeraContadorExecucoes() {
        numExecucoes = 0;
        for(Grupo g : filhos)
            g.zeraContadorExecucoes();
    }
    
    public boolean ehFolha() {
        return false;
    }
    
    public boolean adicionaFilho(Grupo g) {
        if(!this.ehFolha()) {
            this.filhos.add(g);
            return true;
        }
        else
            return false;
    }
    
    public void defineOpcoesSinaliza(int minExecSinaliza, double probSinaliza, int maxExecSinaliza) {
        this.minExecucoesSinaliza = new Integer(minExecSinaliza);
        this.probabilidadeSinaliza = new Double(probSinaliza);
        this.maxExecucoesSinaliza = new Integer(maxExecSinaliza);
    }
    
    /**
     * Seleciona o próximo sub-item de acordo com o algoritmo e
     * configurações internas do grupo, retornando a mensagem
     * para a trilha.
     * @return Mensagem com opções de fim de execução
     */
    public MensagemGrupo executa() {
        MensagemGrupo mensagem;
        
        if(rand == null)
            defineSemente();
        
        if(!this.ehFolha()) {
            Grupo g = this.selecionaGrupo();
            mensagem = g.executa();
            this.padraoSelecionado = g.recuperaPadrao();
        }
        else {
            mensagem = new MensagemGrupo();
        }
        
        numExecucoes++;
        if((this.maxExecucoesSinaliza != null) && (this.maxExecucoesSinaliza <= numExecucoes)) {
            mensagem.sinaliza();
            if(desabilitaTrilha)
                mensagem.desabilita();
        }
        else if ((this.minExecucoesSinaliza != null) && (this.minExecucoesSinaliza <= numExecucoes)
              && (rand.nextDouble() <= this.probabilidadeSinaliza)) {
            mensagem.sinaliza();
            if(desabilitaTrilha)
                mensagem.desabilita();
        }
        
        if((this.maxExecucoesObrigaFim != null) && (this.maxExecucoesObrigaFim <= numExecucoes)) {
            mensagem.obriga();
            if(interrompeSecao)
                mensagem.interrompe();
        }
        else if ((this.minExecucoesObrigaFim != null) && (this.minExecucoesObrigaFim <= numExecucoes)
              && (rand.nextDouble() <= this.probabilidadeObrigaFim)) {
            mensagem.obriga();
            if(interrompeSecao)
                mensagem.interrompe();
        }
        
        return mensagem;
    }
    
    public Padrao recuperaPadrao() {
        return this.padraoSelecionado;
    }
    
    public abstract Grupo selecionaGrupo();
}