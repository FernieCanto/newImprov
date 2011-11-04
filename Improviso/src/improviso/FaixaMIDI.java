package improviso;

public class FaixaMIDI {
    public int canal, instrumento, volume, pan;
    
    public FaixaMIDI(int canal, int instrumento, int volume, int pan) {
        this.canal = canal;
        this.instrumento = instrumento;
        this.volume = volume;
        this.pan = pan;
    }
}
