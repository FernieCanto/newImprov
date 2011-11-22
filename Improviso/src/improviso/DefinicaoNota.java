package improviso;

import java.util.Random;
import java.util.regex.*;

/**
 * Armazena uma definição de uma nota componente de um padrão.
 * @author fernando
 */
public class DefinicaoNota {
    public static int inicioDef = 0, maxInicioDef = 0, inicioFimDef = 0, maxInicioFimDef = 0;
    public static int duracaoDef = 60, maxDuracaoDef = 60, duracaoFimDef = 60, maxDuracaoFimDef = 60;
    public static int velocidadeDef = 100, maxVelocidadeDef = 100, velocidadeFimDef = 100, maxVelocidadeFimDef = 100;
    public static int faixaDef = 1;
    
    int nota;
    int faixa;
    
    protected IntervaloNumerico inicio = null;
    protected IntervaloNumerico duracao = null;
    protected IntervaloNumerico velocidade = null;
    
    protected static Pattern padraoNota = Pattern.compile("^([A-G])([#b])?(-1|\\d)$");
    protected static Pattern padraoIntervalo = Pattern.compile("^(\\d+)(-(\\d+))?(\\|(\\d+)(-(\\d+))?)?$");

    Random rand = null;

    DefinicaoNota(int nota) {
        this.nota = nota;
        faixa = 1;
    }
    
    DefinicaoNota(String nota) {
        this.nota = InterpretaNota(nota);
        faixa = 1;
    }
    
    public static int InterpretaNota(String strNota) {
        int nota = 0;
        Matcher m = padraoNota.matcher(strNota);
        if(m.matches()) {
            switch(m.group(1).charAt(0)) {
                case 'C': nota =  0; break;
                case 'D': nota =  2; break;
                case 'E': nota =  4; break;
                case 'F': nota =  5; break;
                case 'G': nota =  7; break;
                case 'A': nota =  9; break;
                case 'B': nota = 11; break;
            }
            if(m.group(2) != null) {
                if(m.group(2).equals("b"))
                    nota--;
                else
                    nota++;
            }

            if(!m.group(3).equals("-1")) {
                int oitava = Integer.parseInt(m.group(3));
                nota += (oitava+1) * 12;
            }
        }
        
        return nota;
    }
    
    public static DefinicaoNota produzNotaXML(org.w3c.dom.Element elemento) {
        DefinicaoNota def = new DefinicaoNota(elemento.getFirstChild().getNodeValue());
        def.configuraDefinicaoXML(elemento);
        
        return def;
    }
    
    public boolean configuraDefinicaoXML(org.w3c.dom.Element elemento) {
        if(elemento.hasAttribute("start")) {
            this.inicio = Composicao.geraIntervaloDuracao(elemento.getAttribute("start"));
        }
        else this.inicio = new IntervaloNumerico(inicioDef, maxInicioDef, inicioFimDef, maxInicioFimDef);
        
        if(elemento.hasAttribute("length")) {
            this.duracao = Composicao.geraIntervaloDuracao(elemento.getAttribute("length"));
        }
        else this.duracao = new IntervaloNumerico(duracaoDef, maxDuracaoDef, duracaoFimDef, maxDuracaoFimDef);
        
        if(elemento.hasAttribute("velocity")) {
            this.velocidade = Composicao.geraIntervaloNumerico(elemento.getAttribute("velocity"));
        }
        else this.velocidade = new IntervaloNumerico(velocidadeDef, maxVelocidadeDef, velocidadeFimDef, maxVelocidadeFimDef);
        
        if(elemento.hasAttribute("track"))
            this.defineFaixa(Integer.parseInt(elemento.getAttribute("track")));
        else
            this.defineFaixa(faixaDef);
        
        return true;
    }

    public void defineSemente(long semente) {
        if(this.rand == null)
            this.rand = new Random(semente);
        else
            this.rand.setSeed(semente);
    }

    public void defineFaixa(int faixa) {
        this.faixa = faixa;
    }

    public Nota geraNota(int inicio, double posicao) {
        return this.geraNota(inicio, posicao, null);
    }

    public Nota geraNota(int inicioPadrao, double posicao, Integer duracao) {
        int nInicio, nDuracao, nVelocidade;

        if(this.rand == null)
            this.rand = new Random();

        nVelocidade = this.velocidade.geraValor(posicao, rand);
        nInicio     = this.inicio.geraValor(posicao, rand);
        nDuracao    = this.duracao.geraValor(posicao, rand);
        if(duracao != null) {
            if(nInicio + nDuracao > duracao)
                nDuracao = duracao - nInicio;
        }
        
        return new Nota(this.nota, inicioPadrao + nInicio, nDuracao, nVelocidade, this.faixa);
    }
}
