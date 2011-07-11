package improviso;

/**
 *
 * @author fernando
 */
public abstract class CondicaoTerminoSecao {
    protected Secao secao;

    CondicaoTerminoSecao(Secao s) {
        this.secao = s;
    }

    public abstract int obtemFinal();
}
