package improviso;
import java.util.*;
import org.w3c.dom.*;

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
    
    Grupo() {
        filhos = new ArrayList<Grupo>();
    }
    
    public static Grupo produzGrupoXML(BibliotecaXML bibXML, Element elemento) {
        Grupo g;
        NodeList filhos;
        
        if(elemento.getNodeName().equals("sequenceGroup"))
            g = new GrupoSequencia();
        else if(elemento.getNodeName().equals("randomGroup"))
            g = new GrupoSorteio();
        else {
            Padrao p;
            if(elemento.hasAttribute("pattern"))
                p = Padrao.produzPadraoXML(bibXML.grupos.get(elemento.getAttribute("pattern")));
            else
                p = Padrao.produzPadraoXML((Element)elemento.getFirstChild());
            
            g = new GrupoFolha(p);
            g.configuraGrupoXML(elemento);
            return g;
        }
        
        filhos = elemento.getChildNodes();
        for(int indice = 0; indice < filhos.getLength(); indice++) {
            Element filho = (Element)filhos.item(indice);
            g.configuraGrupoXML(filho);
            g.adicionaFilho(Grupo.produzGrupoXML(bibXML, filho));
        }
        
        return g;
    }
    
    public void configuraGrupoXML(Element elemento) {
        if(elemento.hasAttribute("minExecutionsSignal"))
            this.minExecucoesSinaliza = Integer.parseInt(elemento.getAttribute("minExecutionsSignal"));
        if(elemento.hasAttribute("probabilitySignal"))
            this.probabilidadeSinaliza = Double.parseDouble(elemento.getAttribute("probabilitySignal"));
        if(elemento.hasAttribute("maxExecutionsSignal"))
            this.maxExecucoesSinaliza = Integer.parseInt(elemento.getAttribute("maxExecutionsSignal"));

        if(elemento.hasAttribute("minExecutionsFinish"))
            this.minExecucoesObrigaFim = Integer.parseInt(elemento.getAttribute("minExecutionsFinish"));
        if(elemento.hasAttribute("probabilityFinish"))
            this.probabilidadeObrigaFim = Double.parseDouble(elemento.getAttribute("probabilityFinish"));
        if(elemento.hasAttribute("maxExecutionsFinish"))
            this.maxExecucoesObrigaFim = Integer.parseInt(elemento.getAttribute("maxExecutionsFinish"));
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