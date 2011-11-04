package improviso;

import java.util.Random;

/**
 * Armazena uma definição de uma nota componente de um padrão.
 * @author fernando
 */
public class DefinicaoNota {
    int nota;
    Integer inicio, varInicio = null, inicioFim = null, varInicioFim = null;
    Integer duracao, varDuracao = null, duracaoFim = null, varDuracaoFim = null;
    Integer velocidade, varVelocidade = null, velocidadeFim = null, varVelocidadeFim = null;
    int faixa;
    
    Random rand = null;
    
    DefinicaoNota(int nota) {
        this.nota = nota;
        faixa = 1;
    }
    
    public void defineSemente(long semente) {
        if(this.rand == null)
            this.rand = new Random(semente);
        else
            this.rand.setSeed(semente);
    }
    
    public void defineVelocidade(Integer vel) {
        this.velocidade = vel;
    }
    
    public void defineVelocidade(Integer vel, Integer velMax) {
        this.varVelocidade = (velMax - vel);
        this.velocidade = vel;
    }
    
    public void defineVelocidade(Integer vel, Integer velMax, Integer velFim, Integer velMaxFim) {
        this.velocidade = vel;
        if(velMax != null)
            this.varVelocidade = (velMax - vel);
        
        if(velFim != null) {
            this.velocidadeFim = velFim;
            if(velMaxFim != null)
                this.varVelocidadeFim = (velMaxFim - velFim);
        }
    }
    
    public void defineInicio(Integer ini) {
        this.inicio = ini;
    }
    
    public void defineInicio(Integer ini, Integer iniMax) {
        this.inicio = ini;
        if(iniMax != null)
            this.varInicio = (iniMax - ini);
    }
    
    public void defineInicio(Integer ini, Integer iniMax, Integer iniFim, Integer iniMaxFim) {
        this.inicio = ini;
        if(iniMax != null)
            this.varInicio = (iniMax - ini);
        
        if(iniFim != null) {
            this.inicioFim = iniFim;
            if(iniMaxFim != null)
                this.varInicioFim = (iniMaxFim - iniFim);
        }
    }
    
    public void defineDuracao(Integer dur) {
        this.duracao = dur;
    }
    
    public void defineDuracao(Integer dur, Integer durMax) {
        this.duracao = dur;
        if(durMax != null)
            this.varDuracao = (durMax - dur);
    }
    
    public void defineDuracao(Integer dur, Integer durMax, Integer durFim, Integer durMaxFim) {
        this.duracao = dur;
        if(durMax != null)
            this.varDuracao = (durMax - dur);
        
        if(durFim != null) {
            this.duracaoFim = durFim;
            if(durMaxFim != null)
                this.varDuracaoFim = (durMaxFim - durFim);
        }
    }
    
    public void defineFaixa(int faixa) {
        this.faixa = faixa;
    }
    
    public Nota geraNota(int inicio, double posicao) {
      return this.geraNota(inicio, posicao, null);
    }
    
    public Nota geraNota(int inicio, double posicao, Integer duracao) {
        int nInicio, nDuracao, nVelocidade;
        int nVar;
        
        if(this.rand == null)
            this.rand = new Random();
        
        if(this.inicioFim == null) {
            nInicio = this.inicio;
            if(this.varInicio != null)
                nInicio += this.rand.nextInt(varInicio + 1);
        }
        else {
            nInicio = (this.inicio + (int)( (this.inicioFim - this.inicio) * posicao ));
            if(this.varInicio != null) {
                nVar = varInicio + (int)((varInicioFim - varInicio)*posicao);
                nInicio += this.rand.nextInt(nVar + 1);
            }
        }
        
        if(duracao == null)
          nDuracao = this.duracao;
        else
          nDuracao = duracao;
        
        if(this.duracaoFim == null) {
            if(this.varDuracao != null)
                nDuracao += this.rand.nextInt(varDuracao + 1);
        }
        else {
            nDuracao += (int)( (this.duracaoFim - nDuracao)*posicao );
            if(this.varDuracao != null) {
                nVar = varDuracao + (int)((varDuracaoFim - varDuracao)*posicao);
                nDuracao += this.rand.nextInt(varDuracao + 1);
            }
        }
        
        if(this.velocidadeFim == null) {
            nVelocidade = this.velocidade;
            if(this.varVelocidade != null)
                nVelocidade += this.rand.nextInt(varVelocidade + 1);
        }
        else {
            nVelocidade = this.velocidade + (int)( (this.velocidadeFim - this.velocidade)*posicao );
            if(this.varVelocidade != null) {
                nVar = varVelocidade + (int)((varVelocidadeFim - varVelocidade)*posicao);
                nVelocidade += this.rand.nextInt(nVar + 1);
            }
        }
        
        return new Nota(this.nota, inicio + nInicio, nDuracao, nVelocidade, this.faixa);
    }
}
