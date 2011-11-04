package improviso;
import java.util.*;
import java.util.regex.*;

/**
 *
 * @author fernando
 */
public class Composicao {
    public static final int TICKS_SEMIBREVE = 480;
    
    ArrayList<FaixaMIDI> faixas;
    HashMap<String, Padrao> padroes;
    HashMap<String, Grupo> grupos;
    HashMap<String, Trilha> trilhas;
    HashMap<String, Secao> secoes;
    ListaArestas secoesIniciais = new ListaArestas();
    HashMap<String, ListaArestas> destinosSecoes;
    
    static Pattern padraoCompassoNumerico = Pattern.compile("^(\\d):(\\d\\d\\d)$");
    static Pattern padraoCompassoFormulaTotal = Pattern.compile("^(\\d+\\s)?\\d+/\\d+(\\|(\\d+\\s)?\\d+/\\d+)*$");
    static Pattern padraoCompassoFormula = Pattern.compile("(\\d+)\\s?(\\d+)/(\\d+)");
    
    public Composicao() {
        faixas = new ArrayList<FaixaMIDI>();
        padroes = new HashMap<String, Padrao>();
        secoes = new HashMap<String, Secao>();
        destinosSecoes = new HashMap<String, ListaArestas>();
    }
    
    public static Composicao processaXML() {
        Composicao c = new Composicao();
        
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
    
    public void adicionaPadrao(String identificador, Padrao padrao)
           throws Exception {
        if(!padroes.containsKey(identificador))
            padroes.put(identificador, padrao);
        else
            throw new Exception("Seção repetida");
    }
    
    public Padrao buscaPadrao(String identificador) {
        return padroes.get(identificador);
    }
    
    public void adicionaSecao(String identificador, Secao secao) {
        secoes.put(identificador, secao);
        destinosSecoes.put(identificador, new ListaArestas());
    }
    
    public void criaAresta(String origem, String destino, int probabilidade, int maxExecucoes, boolean encerra) {
        Aresta a;
        if(origem != null && !destinosSecoes.containsKey(origem))
            return;
        if( (origem == null && destino == null)
         || probabilidade < 1
         || maxExecucoes < 1)
            return;

        a = new Aresta(destino, probabilidade, maxExecucoes, encerra);
        if(origem == null)
            secoesIniciais.adicionaAresta(a);
        else
            destinosSecoes.get(origem).adicionaAresta(a);
    }

    void adicionaFaixa(FaixaMIDI faixaMIDI) {
        faixas.add(faixaMIDI);
    }
}