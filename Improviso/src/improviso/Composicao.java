package improviso;
import java.util.*;
import java.util.regex.*;
import org.w3c.dom.*;

/**
 *
 * @author fernando
 */
public class Composicao {
    public static final int TICKS_SEMIBREVE = 480;
    
    protected ArrayList<Nota> notas;
    protected ArrayList<FaixaMIDI> faixasMIDI;
    protected HashMap<String, Secao> secoes;
    protected ListaArestas secoesIniciais = new ListaArestas();
    protected HashMap<String, ListaArestas> destinosSecoes;
    
    static Pattern padraoCompassoNumerico = Pattern.compile("^(\\d):(\\d\\d\\d)$");
    static Pattern padraoCompassoFormulaTotal = Pattern.compile("^(\\d+\\s)?\\d+/\\d+(\\|(\\d+\\s)?\\d+/\\d+)*$");
    static Pattern padraoCompassoFormula = Pattern.compile("(\\d+)\\s?(\\d+)/(\\d+)");
    
    public Composicao() {
        notas = new ArrayList<Nota>();
        faixasMIDI = new ArrayList<FaixaMIDI>();
        secoes = new HashMap<String, Secao>();
        destinosSecoes = new HashMap<String, ListaArestas>();
    }
    
    public static Composicao processaXML(Document documentoXML) {
        Composicao c = new Composicao();
        BibliotecaXML bibXML = new BibliotecaXML();
        
        NodeList listasPadroes = documentoXML.getElementsByTagName("patternList");
        for(int indice = 0; indice < listasPadroes.getLength(); indice++) {
            for(int indice2 = 0; indice2 < listasPadroes.item(indice).getChildNodes().getLength(); indice2++) {
                Element elementoPadrao = (Element)listasPadroes.item(indice).getChildNodes().item(indice2);
                String padraoId = elementoPadrao.getAttribute("id");
                bibXML.padroes.put(padraoId, elementoPadrao);
            }
        }
        
        NodeList listasGrupos = documentoXML.getElementsByTagName("groupList");
        for(int indice = 0; indice < listasGrupos.getLength(); indice++) {
            for(int indice2 = 0; indice2 < listasGrupos.item(indice).getChildNodes().getLength(); indice2++) {
                Element elementoGrupo = (Element)listasGrupos.item(indice).getChildNodes().item(indice2);
                String grupoId = elementoGrupo.getAttribute("id");
                bibXML.grupos.put(grupoId, elementoGrupo);
            }
        }
        
        NodeList listasTrilhas = documentoXML.getElementsByTagName("trackList");
        for(int indice = 0; indice < listasTrilhas.getLength(); indice++) {
            for(int indice2 = 0; indice2 < listasTrilhas.item(indice).getChildNodes().getLength(); indice2++) {
                Element elementoTrilha = (Element)listasTrilhas.item(indice).getChildNodes().item(indice2);
                String trilhaId = elementoTrilha.getAttribute("id");
                bibXML.trilhas.put(trilhaId, elementoTrilha);
            }
        }
        
        NodeList listasSecoes = documentoXML.getElementsByTagName("sectionList");
        for(int indice = 0; indice < listasSecoes.getLength(); indice++) {
            for(int indice2 = 0; indice2 < listasSecoes.item(indice).getChildNodes().getLength(); indice2++) {
                Element elementoTrilha = (Element)listasSecoes.item(indice).getChildNodes().item(indice2);
                String trilhaId = elementoTrilha.getAttribute("id");
                bibXML.trilhas.put(trilhaId, elementoTrilha);
            }
        }
        
        Element estrutura = (Element)documentoXML.getElementsByTagName("structure").item(0);
        NodeList elementosSecoes = estrutura.getElementsByTagName("section");
        for(int indice = 0; indice < elementosSecoes.getLength(); indice++) {
            Element elementoSecao = (Element)elementosSecoes.item(indice);
            c.adicionaSecao(elementoSecao.getAttribute("id"), Secao.produzSecaoXML(bibXML, bibXML.secoes.get(elementoSecao.getAttribute("after"))));
        }
        NodeList elementosArestas = estrutura.getElementsByTagName("arrows");
        for(int indice = 0; indice < elementosArestas.getLength(); indice++) {
            Element elementoAresta = (Element)elementosArestas.item(indice);
            Aresta a = Aresta.produzArestaXML(elementoAresta);
            if(elementoAresta.hasAttribute("from"))
                c.adicionaAresta(elementoAresta.getAttribute("from"), a);
            else if(a.recuperaDestino() != null)
                c.adicionaAresta(null, a);
        }
        
        return c;
    }
    
    public static int interpretaDuracao(String stringDuracao)
           throws NumberFormatException {
        int ticks = 0;
        
        Matcher m = padraoCompassoNumerico.matcher(stringDuracao);
        if(m.matches()) {
            ticks += Integer.parseInt(m.group(1)) * Composicao.TICKS_SEMIBREVE; /* Número antes do : */
            ticks += Integer.parseInt(m.group(2)); /* Número antes do : */
            return ticks;
        }
        
        Matcher m2 = padraoCompassoFormulaTotal.matcher(stringDuracao);
        if(m2.matches()) {
            Matcher formulas = padraoCompassoFormula.matcher(stringDuracao);
            while(formulas.find()) {
                int vezes = 1;
                int numerador, denominador;
                if(formulas.group(1) != null)
                    vezes = Integer.parseInt(formulas.group(1));
                numerador = Integer.parseInt(formulas.group(2));
                denominador = Integer.parseInt(formulas.group(3));
                
                ticks += vezes * numerador * (Composicao.TICKS_SEMIBREVE /denominador);
            }
            return ticks;
        }
        
        ticks = Integer.parseInt(stringDuracao);
        return ticks;
    }
    
    public void adicionaSecao(String identificador, Secao secao) {
        secoes.put(identificador, secao);
        destinosSecoes.put(identificador, new ListaArestas());
    }
    
    public void adicionaAresta(String origem, Aresta aresta) {
        if(origem == null)
            secoesIniciais.adicionaAresta(aresta);
        else
            destinosSecoes.get(origem).adicionaAresta(aresta);
    }

    void adicionaFaixa(FaixaMIDI faixaMIDI) {
        faixasMIDI.add(faixaMIDI);
    }
    
    public boolean executa() {
        String idSecaoAtual;
        Secao secaoAtual;
        int posicaoAtual = 0;
        if(secoesIniciais.numArestas() > 0)
            idSecaoAtual = secoesIniciais.buscaNovoDestino();
        else {
            if(secoes.isEmpty())
                return false;
            else
                idSecaoAtual = secoes.keySet().iterator().next();
        }
        
        do {
            secaoAtual = secoes.get(idSecaoAtual);
            
            secaoAtual.defineNovaPosicao(posicaoAtual);
            notas.addAll(secaoAtual.geraNotas());
            posicaoAtual = secaoAtual.obtemFinal();
            
            if(destinosSecoes.get(idSecaoAtual).numArestas() > 0)
                idSecaoAtual = destinosSecoes.get(idSecaoAtual).buscaNovoDestino();
            else
                idSecaoAtual = null;
            
        } while(idSecaoAtual != null);
        
        return true;
    }
}