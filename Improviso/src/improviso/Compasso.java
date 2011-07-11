package improviso;

/**
 *
 * @author fernando
 */
public class Compasso {
    public static final int TICKS_SEMIBREVE = 480;
    private int valorTempo, numTempos;
    
    Compasso(String compasso) throws IllegalArgumentException {
        String[] partes = compasso.split("/");
        if(partes.length != 2)
            throw new IllegalArgumentException("Formato inválido");
        try {
            this.numTempos = Integer.parseInt(partes[0]);
            this.valorTempo = Integer.parseInt(partes[1]);
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Formato inválido");
        }
    }
    
    public int calculaDuracao() {
        return (Compasso.TICKS_SEMIBREVE / this.valorTempo) * this.numTempos;
    }
}