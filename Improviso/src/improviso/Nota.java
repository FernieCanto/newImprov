package improviso;

/**
 *
 * @author fernando
 */
public class Nota {
    public int nota, inicio, duracao, velocidade, faixa;
    
    Nota(int nota, int inicio, int duracao, int velocidade) {
        this.nota = nota;
        this.inicio = inicio;
        this.duracao = duracao;
        this.velocidade = velocidade;
        this.faixa = 0;
    }
    
    Nota(int nota, int inicio, int duracao, int velocidade, int faixa) {
        this.nota = nota;
        this.inicio = inicio;
        this.duracao = duracao;
        this.velocidade = velocidade;
        this.faixa = faixa;
    }
}
