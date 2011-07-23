package improviso;

/**
 *
 * @author fernando
 */
public abstract class CondicaoTerminoSecao {
    protected Secao secao;

    CondicaoTerminoSecao() {
    }
    
    public void defineSecao(Secao s) {
      secao = s;
    }

    public abstract int obtemFinal();
    public abstract boolean interrompeTrilhas();
}
