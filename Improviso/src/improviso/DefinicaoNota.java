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
    int inicio, varInicio, inicioFim, varInicioFim;
    int duracao, varDuracao, duracaoFim, varDuracaoFim;
    int velocidade, varVelocidade, velocidadeFim, varVelocidadeFim;
    int faixa;
    
    protected static Pattern padraoNota = Pattern.compile("^([ABCDEFG])([b#])?((\\-1)|[0-9])$");
    protected static Pattern padraoIntervalo = Pattern.compile("^(\\d)(-(\\d))?(\\|(\\d)(-(\\d))?)?$");

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
        switch(m.group(1).charAt(0)) {
            case 'C': nota =  0;
            case 'D': nota =  2;
            case 'E': nota =  4;
            case 'F': nota =  5;
            case 'G': nota =  7;
            case 'A': nota =  9;
            case 'B': nota = 11;
        }
        if(m.group(2).equals("b"))
            nota--;
        else if(m.group(2).equals("#"))
            nota++;
        
        if(!m.group(3).equals("-1")) {
            int oitava = Integer.parseInt(m.group(3));
            nota += (oitava+1) * 12;
        }
        
        return nota;
    }
    
    public static DefinicaoNota produzNotaXML(org.w3c.dom.Element elemento) {
        DefinicaoNota def = new DefinicaoNota(elemento.getNodeValue());
        def.configuraDefinicaoXML(elemento);
        
        return def;
    }
    
    public boolean configuraDefinicaoXML(org.w3c.dom.Element elemento) {
        if(elemento.hasAttribute("start")) {
            Matcher m = padraoIntervalo.matcher(elemento.getAttribute("start"));
            int ini = Integer.parseInt(m.group(1));
            int iniMax = ini, iniFim = ini, iniMaxFim = ini;
            if(m.group(3) != null)
                iniMax = Integer.parseInt(m.group(3));
            if(m.group(5) != null)
                iniFim = Integer.parseInt(m.group(5));
            if(m.group(7) != null)
                iniMaxFim = Integer.parseInt(m.group(7));
            this.defineInicio(ini, iniMax, iniFim, iniMaxFim);
        }
        else this.defineInicio(inicioDef, maxInicioDef, inicioFimDef, maxInicioFimDef);
        
        if(elemento.hasAttribute("length")) {
            Matcher m = padraoIntervalo.matcher(elemento.getAttribute("length"));
            int dur = Integer.parseInt(m.group(1));
            int durMax = dur, durFim = dur, durMaxFim = dur;
            if(m.group(3) != null)
                durMax = Integer.parseInt(m.group(3));
            if(m.group(5) != null)
                durFim = Integer.parseInt(m.group(5));
            if(m.group(7) != null)
                durMaxFim = Integer.parseInt(m.group(7));
            this.defineDuracao(dur, durMax, durFim, durMaxFim);
        }
        else this.defineDuracao(duracaoDef, maxDuracaoDef, duracaoFimDef, maxDuracaoFimDef);
        
        if(elemento.hasAttribute("velocity")) {
            Matcher m = padraoIntervalo.matcher(elemento.getAttribute("velocity"));
            int vel = Integer.parseInt(m.group(1));
            int velMax = vel, velFim = vel, velMaxFim = vel;
            if(m.group(3) != null)
                velMax = Integer.parseInt(m.group(3));
            if(m.group(5) != null)
                velFim = Integer.parseInt(m.group(5));
            if(m.group(7) != null)
                velMaxFim = Integer.parseInt(m.group(7));
            this.defineVelocidade(vel, velMax, velFim, velMaxFim);
        }
        else this.defineDuracao(velocidadeDef, maxVelocidadeDef, velocidadeFimDef, maxVelocidadeFimDef);
        
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

    public void defineVelocidade(int vel) {
        this.velocidade = vel;
        this.varVelocidade = 0;

        this.velocidadeFim = vel;
        this.varVelocidadeFim = 0;
    }

    public void defineVelocidade(int vel, int velMax) {
        this.velocidade = vel;
        this.varVelocidade = (velMax - vel);

        this.velocidadeFim = vel;
        this.varVelocidadeFim = (velMax - vel);
    }

    public void defineVelocidade(int vel, int velMax, int velFim, int velMaxFim) {
        this.velocidade = vel;
        this.varVelocidade = (velMax - vel);

        this.velocidadeFim = velFim;
        this.varVelocidadeFim = (velMaxFim - velFim);
    }

    public void defineInicio(Integer ini) {
        this.inicio = ini;
        this.varInicio = 0;

        this.inicioFim = ini;
        this.varInicioFim = 0;
    }

    public void defineInicio(Integer ini, Integer iniMax) {
        this.inicio = ini;
        this.varInicio = (iniMax - ini);

        this.inicioFim = ini;
        this.varInicioFim = (iniMax - ini);
    }

    public void defineInicio(Integer ini, Integer iniMax, Integer iniFim, Integer iniMaxFim) {
        this.inicio = ini;
        this.varInicio = (iniMax - ini);

        this.inicioFim = iniFim;
        this.varInicioFim = (iniMaxFim - iniFim);
    }

    public void defineDuracao(Integer dur) {
        this.duracao = dur;
        this.varDuracao = 0;

        this.duracaoFim = dur;
        this.varDuracaoFim = 0;
    }

    public void defineDuracao(Integer dur, Integer durMax) {
        this.duracao = dur;
        this.varDuracao = (durMax - dur);

        this.duracaoFim = dur;
        this.varDuracaoFim = (durMax - dur);
    }

    public void defineDuracao(Integer dur, Integer durMax, Integer durFim, Integer durMaxFim) {
        this.duracao = dur;
        this.varDuracao = (durMax - dur);

        this.duracaoFim = durFim;
        this.varDuracaoFim = (durMaxFim - durFim);
    }

    public void defineFaixa(int faixa) {
        this.faixa = faixa;
    }

    public Nota geraNota(int inicio, double posicao) {
        return this.geraNota(inicio, posicao, null);
    }

    public Nota geraNota(int inicio, double posicao, Integer duracao) {
        int nInicio, nDuracao, nVelocidade;

        if(this.rand == null)
            this.rand = new Random();

        nInicio  = this.inicio + (int)( (this.inicioFim - this.inicio) * posicao );
        nInicio += this.rand.nextInt(varInicio + (int)( (this.varInicioFim - this.varInicio) * posicao) + 1);

        nDuracao  = this.duracao + (int)( (this.duracaoFim - this.duracao) * posicao );
        nDuracao += this.rand.nextInt(varDuracao + (int)( (this.varDuracaoFim - this.varDuracao) * posicao) + 1);

        nVelocidade  = this.velocidade + (int)( (this.velocidadeFim - this.velocidade) * posicao );
        nVelocidade += this.rand.nextInt(varVelocidade + (int)( (this.varVelocidadeFim - this.varVelocidade) * posicao) + 1);

        return new Nota(this.nota, inicio + nInicio, nDuracao, nVelocidade, this.faixa);
    }
}
