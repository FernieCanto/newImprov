package improviso;

import java.util.*;
import org.w3c.dom.*;
/**
 *
 * @author fernando
 */
public abstract class Secao {
    protected ArrayList<Trilha> trilhas;
    protected Trilha trilhaSelecionada;
    protected int indiceTrilhaSelecionada;
    protected int inicio, posicaoAtual;
    protected int tempo = 120;
    protected boolean interrompeTrilhas = false;
    
    Secao() {
        this.inicio = 0;
        this.posicaoAtual = 0;
        this.trilhas = new ArrayList<Trilha>();
    }

    public static Secao produzSecaoXML(BibliotecaXML bibXML, Element elemento) {
        Secao s;
        NodeList trilhas;
        
        if(elemento.getNodeName().equals("fixedSection"))
            s = new SecaoFixa();
        else
            s = new SecaoVariavel();
        s.configuraSecaoXML(elemento);
        
        trilhas = elemento.getChildNodes();
        for(int indice = 0; indice < trilhas.getLength(); indice++) {
            if(trilhas.item(indice).getNodeType() == Node.ELEMENT_NODE) {
                Element elementoTrilha = (Element)trilhas.item(indice);
                if(elementoTrilha.hasAttribute("after"))
                    s.adicionaTrilha(Trilha.produzTrilhaXML(bibXML, bibXML.trilhas.get(elementoTrilha.getAttribute("after"))));
                else
                    s.adicionaTrilha(Trilha.produzTrilhaXML(bibXML, elementoTrilha));
            }
        }
        
        return s;
    }
    
    public void configuraSecaoXML(Element elemento) {
        if(elemento.hasAttribute("tempo"))
        tempo = Integer.parseInt(elemento.getAttribute("tempo"));
    }
    
    public int retornaInicio() {
        return this.inicio;
    }
    
    public int recuperaPosicaoAtual() {
        return this.posicaoAtual;
    }
    
    public void defineNovaPosicao(int posicao) {
        this.inicio = posicao;
        this.posicaoAtual = posicao;
    }
    
    public boolean adicionaTrilha(Trilha t) {
        this.trilhas.add(t);
        return true;
    }
    
    /**
     * Devolve notas produzidas por todas as trilhas da Secao
     * durante sua execução.
     * @return Vetor de Notas
     */
    public ArrayList<Nota> geraNotas() {
        ArrayList<Nota> notas = new ArrayList<Nota>();
        Integer posicaoFim, novaPosicaoAtual;
        
        /* Inicializa todas as trilhas */
        for(Trilha t : this.trilhas) {
            t.inicializa(inicio);
            this.processaMensagem(t.executa());
        }
        posicaoFim = this.obtemFinal();
        
        /* Enquanto o fim da seção for desconhecido ou maior que a posição atual */
        while(posicaoFim == null || posicaoFim > posicaoAtual) {
          trilhaSelecionada = null;
          /* Buscamos a trilha que termina de executar mais cedo */
          for(int i = 0; i < this.trilhas.size(); i++) {
            if(trilhaSelecionada == null || this.trilhas.get(i).buscaFinal() < trilhaSelecionada.buscaFinal()) {
              trilhaSelecionada = this.trilhas.get(i);
              indiceTrilhaSelecionada = i;
            }
          }
          
          if(posicaoFim == null || !interrompeTrilhas)
            notas.addAll(trilhaSelecionada.geraNotas(0.0));
          else
            notas.addAll(trilhaSelecionada.geraNotas(posicaoFim - trilhaSelecionada.buscaPosicaoAtual(), 0.0));

          this.processaMensagem(trilhaSelecionada.executa());
          novaPosicaoAtual = trilhaSelecionada.buscaPosicaoAtual();
          for(Trilha t : this.trilhas) {
            if(t.buscaPosicaoAtual() < novaPosicaoAtual)
              novaPosicaoAtual = t.buscaPosicaoAtual();
          }
          posicaoAtual = novaPosicaoAtual;
          posicaoFim = this.obtemFinal();
        }
        
        return notas;
    }
    
    protected abstract void processaMensagem(MensagemGrupo mensagem);
    protected abstract Integer obtemFinal();
}
