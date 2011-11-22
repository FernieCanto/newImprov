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
    
    static Pattern padraoIntervaloNumerico = Pattern.compile("^(?<val>\\d+)(-(?<valMax>\\d+))?$");
    static Pattern padraoCompassoNumerico = Pattern.compile("^(?<seminimas>\\d):(?<ticks>\\d\\d\\d)$");
    static Pattern padraoCompassoFormula = Pattern.compile("((?<quant>\\d+)\\s)?(?<num>\\d+)/(?<denom>\\d+)\\s?");
    
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
                Node nodoPadrao = listasPadroes.item(indice).getChildNodes().item(indice2);
                if(nodoPadrao.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoPadrao = (Element)nodoPadrao;
                    //System.out.println("Adicionando padrão "+elementoPadrao.getAttribute("id")+" à biblioteca.");
                    String padraoId = elementoPadrao.getAttribute("id");
                    bibXML.padroes.put(padraoId, elementoPadrao);
                }
            }
        }
        
        NodeList listasGrupos = documentoXML.getElementsByTagName("groupList");
        for(int indice = 0; indice < listasGrupos.getLength(); indice++) {
            for(int indice2 = 0; indice2 < listasGrupos.item(indice).getChildNodes().getLength(); indice2++) {
                Node nodoGrupo = listasGrupos.item(indice).getChildNodes().item(indice2);
                if(nodoGrupo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoGrupo = (Element)nodoGrupo;
                    String grupoId = elementoGrupo.getAttribute("id");
                    bibXML.grupos.put(grupoId, elementoGrupo);
                }
            }
        }
        
        NodeList listasTrilhas = documentoXML.getElementsByTagName("trackList");
        for(int indice = 0; indice < listasTrilhas.getLength(); indice++) {
            for(int indice2 = 0; indice2 < listasTrilhas.item(indice).getChildNodes().getLength(); indice2++) {
                Node nodoTrilha = listasTrilhas.item(indice).getChildNodes().item(indice2);
                if(nodoTrilha.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoTrilha = (Element)nodoTrilha;
                    String trilhaId = elementoTrilha.getAttribute("id");
                    bibXML.trilhas.put(trilhaId, elementoTrilha);
                }
            }
        }
        
        NodeList listasSecoes = documentoXML.getElementsByTagName("sectionList");
        for(int indice = 0; indice < listasSecoes.getLength(); indice++) {
            for(int indice2 = 0; indice2 < listasSecoes.item(indice).getChildNodes().getLength(); indice2++) {
                Node nodoSecao = listasSecoes.item(indice).getChildNodes().item(indice2);
                if(nodoSecao.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoSecao = (Element)nodoSecao;
                    String secaoId = elementoSecao.getAttribute("id");
                    bibXML.secoes.put(secaoId, elementoSecao);
                }
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
    
    public static IntervaloNumerico geraIntervaloNumerico(String stringIntervalo) {
        int valMin, valMax, valFimMin, valFimMax;
        
        String[] iniFim = stringIntervalo.split(" | ");
        String[] minMax = iniFim[0].split(" - ");
        valMin = Integer.parseInt(minMax[0]);
        if(minMax.length > 1)
            valMax = Integer.parseInt(minMax[2]);
        else
            valMax = valMin;
        
        if(iniFim.length > 1) {
            String[] minMaxFim = iniFim[2].split(" - ");
            valFimMin = Integer.parseInt(minMaxFim[0]);
            if(minMaxFim.length > 1)
                valFimMax = Integer.parseInt(minMaxFim[2]);
            else
                valFimMax = valFimMin;
        }
        else {
            valFimMin = valMin;
            valFimMax = valMax;
        }
        
        return new IntervaloNumerico(valMin, valMax, valFimMin, valFimMax);
    }
    
    public static IntervaloNumerico geraIntervaloDuracao(String stringIntervalo) {
        int valMin, valMax, valFimMin, valFimMax;
        
        String[] iniFim = stringIntervalo.split(" | ");
        String[] minMax = iniFim[0].split(" - ");
        valMin = interpretaDuracao(minMax[0]);
        if(minMax.length > 1)
            valMax = interpretaDuracao(minMax[2]);
        else
            valMax = valMin;
        
        if(iniFim.length > 1) {
            String[] minMaxFim = iniFim[2].split(" - ");
            valFimMin = interpretaDuracao(minMaxFim[0]);
            if(minMaxFim.length > 1)
                valFimMax = interpretaDuracao(minMaxFim[2]);
            else
                valFimMax = valFimMin;
        }
        else {
            valFimMin = valMin;
            valFimMax = valMax;
        }
        
        return new IntervaloNumerico(valMin, valMax, valFimMin, valFimMax);
    }
    
    public static int interpretaDuracao(String stringDuracao)
           throws NumberFormatException {
        int ticks = 0;
        
        Matcher m = padraoCompassoNumerico.matcher(stringDuracao);
        if(m.matches()) {
            ticks += Integer.parseInt(m.group("seminimas")) * Composicao.TICKS_SEMIBREVE / 4; /* Número antes do : */
            ticks += Integer.parseInt(m.group("ticks")); /* Número após o : */
            return ticks;
        }
        
        Matcher m2 = padraoCompassoFormula.matcher(stringDuracao);
        if(m2.find()) {
            do {
                int quant = 1;
                int numerador, denominador;
                if(m2.group("quant") != null)
                    quant = Integer.parseInt(m2.group("quant"));
                numerador = Integer.parseInt(m2.group("num"));
                denominador = Integer.parseInt(m2.group("denom"));
                
                ticks += quant * numerador * (Composicao.TICKS_SEMIBREVE / denominador);
            } while(m2.find());
            
            return ticks;
        }
        
        ticks = Integer.parseInt(stringDuracao);
        return ticks;
    }
    
    public void adicionaFaixaMIDI(FaixaMIDI faixa) {
        faixasMIDI.add(faixa);
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
    
    public String secoes() {
        return this.secoes.toString();
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
            posicaoAtual = secaoAtual.recuperaPosicaoAtual();
            
            if(destinosSecoes.get(idSecaoAtual).numArestas() > 0)
                idSecaoAtual = destinosSecoes.get(idSecaoAtual).buscaNovoDestino();
            else
                idSecaoAtual = null;
            
        } while(idSecaoAtual != null);
        
        return true;
    }
    
    public boolean geraArquivoMIDI(String nomeArquivo) {
        return true;
    }
}