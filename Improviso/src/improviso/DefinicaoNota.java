package improviso;

import java.util.Random;

/**
 * Armazena uma definição de uma nota componente de um padrão.
 * @author fernando
 */
public class DefinicaoNota {
    int nota;
    int inicio, varInicio, inicioFim, varInicioFim;
    int duracao, varDuracao, duracaoFim, varDuracaoFim;
    int velocidade, varVelocidade, velocidadeFim, varVelocidadeFim;
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
