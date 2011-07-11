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
    
    
    Random rand = null;
    
    DefinicaoNota(int nota) {
        this.nota = nota;
    }
    
    public void defineSemente(long semente) {
        if(this.rand == null)
            this.rand = new Random(semente);
        else
            this.rand.setSeed(semente);
    }
    
    public void defineVelocidade(int vel) {
        this.velocidade = vel;
        this.varVelocidade = -1;
        this.velocidadeFim = -1;
        this.varVelocidadeFim = -1;
    }
    
    public void defineVelocidade(int vel, int var) {
        this.velocidade = vel;
        this.varVelocidade = var;
        this.velocidadeFim = -1;
        this.varVelocidadeFim = -1;
    }
    
    public void defineVelocidade(int vel, int var, int velFim, int varFim) {
        this.velocidade = vel;
        this.varVelocidade = var;
        this.velocidadeFim = velFim;
        this.varVelocidadeFim = varFim;
    }
    
    public void defineInicio(int ini) {
        this.inicio = ini;
        this.varInicio = -1;
        this.inicioFim = -1;
        this.varInicioFim = -1;
    }
    
    public void defineInicio(int ini, int varIni) {
        this.inicio = ini;
        this.varInicio = varIni;
        this.inicioFim = -1;
        this.varInicioFim = -1;
    }
    
    public void defineInicio(int ini, int varIni, int iniFim, int varIniFim) {
        this.inicio = ini;
        this.varInicio = varIni;
        this.inicioFim = iniFim;
        this.varInicioFim = varIniFim;
    }
    
    public void defineDuracao(int dur) {
        this.duracao = dur;
        this.varDuracao = -1;
        this.duracaoFim = -1;
        this.varDuracaoFim = -1;
    }
    
    public void defineDuracao(int dur, int varDur) {
        this.duracao = dur;
        this.varDuracao = varDur;
        this.duracaoFim = -1;
        this.varDuracaoFim = -1;
    }
    
    public void defineDuracao(int dur, int varDur, int durFim, int varDurFim) {
        this.duracao = dur;
        this.varDuracao = varDur;
        this.duracaoFim = durFim;
        this.varDuracaoFim = varDurFim;
    }
    
    public Nota geraNota(int inicio, double posicao) {
        int nInicio, nDuracao, nVelocidade;
        int nVar;
        
        if(this.rand == null)
            this.rand = new Random();
        
        // TODO: Rever geração de variações
        if(this.inicioFim == -1) {
            nInicio = this.inicio;
            if(this.varInicio != -1)
                nInicio += this.rand.nextInt(varInicio*2 + 1) - varInicio;
        }
        else {
            nInicio = this.inicio + (int)( (this.inicioFim - this.inicio)*posicao );
            if(this.varInicio != -1) {
                nVar = varInicio + (int)((varInicioFim - varInicio)*posicao);
                nInicio += this.rand.nextInt(nVar*2 + 1) - nVar;
            }
        }
        
        if(this.duracaoFim == -1) {
            nDuracao = this.duracao;
            if(this.varDuracao != -1)
                nDuracao += this.rand.nextInt(varDuracao*2 + 1) - varDuracao;
        }
        else {
            nDuracao = this.duracao + (int)( (this.duracaoFim - this.duracao)*posicao );
            if(this.varDuracao != -1) {
                nVar = varDuracao + (int)((varDuracaoFim - varDuracao)*posicao);
                nDuracao += this.rand.nextInt(nVar*2 + 1) - nVar;
            }
        }
        
        if(this.velocidadeFim == -1) {
            nVelocidade = this.velocidade;
            if(this.varVelocidade != -1)
                nVelocidade += this.rand.nextInt(varVelocidade*2 + 1) - varVelocidade;
        }
        else {
            nVelocidade = this.velocidade + (int)( (this.velocidadeFim - this.velocidade)*posicao );
            if(this.varVelocidade != -1) {
                nVar = varVelocidade + (int)((varVelocidadeFim - varVelocidade)*posicao);
                nVelocidade += this.rand.nextInt(nVar*2 + 1) - nVar;
            }
        }
        
        return new Nota(this.nota, inicio + nInicio, nDuracao, nVelocidade);
    }
}
