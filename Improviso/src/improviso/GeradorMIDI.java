/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import java.io.IOException;
import java.util.*;
import java.lang.*;
import javax.sound.midi.*;

/**
 *
 * @author fernando
 */
public class GeradorMIDI {
  private Sequence sequencia;
  private Track[] faixas;
  private long tickAtual;
  
  GeradorMIDI(int numFaixas) throws InvalidMidiDataException {
      sequencia = new Sequence(Sequence.PPQ, 120);
      faixas = new Track[numFaixas];
      for(int i = 0; i < numFaixas; i++) {
        faixas[i] = sequencia.createTrack();
      }
      tickAtual = 0;
  }
  
  public void tickAtual(long tick) {
    tickAtual = tick;
  }
  
  public void defineTempo (int tempo) throws InvalidMidiDataException {
    MetaMessage tempoMens = new MetaMessage();
    int microssegundos = (int)(60000000 / tempo);
    byte dados[] = new byte[3];
    dados[0] = (byte)(microssegundos >>> 16);
    dados[1] = (byte)(microssegundos >>> 8);
    dados[2] = (byte)(microssegundos);
    tempoMens.setMessage(0x51, dados, 3);
    faixas[0].add(new MidiEvent(tempoMens, tickAtual));
  }
  
  public void defineCompasso (int numerador, int denominador) throws InvalidMidiDataException {
    MetaMessage compMens = new MetaMessage();
    int denominadorExp = (int) (Math.log((double)denominador) / Math.log(2.0));
    byte dados[] = new byte[4];
    dados[0] = (byte)numerador;
    dados[1] = (byte)denominadorExp;
    dados[2] = (byte)24;
    dados[3] = (byte)8;
    compMens.setMessage(0x58, dados, 4);
    faixas[0].add(new MidiEvent(compMens, tickAtual));
  }
  
  public void insereNotas(ArrayList<Nota> notas) throws InvalidMidiDataException {
    for(Nota nota : notas) {
      MidiEvent evento;
      ShortMessage notaMens = new ShortMessage();
      notaMens.setMessage(ShortMessage.NOTE_ON, nota.nota, nota.velocidade);
      evento = new MidiEvent(notaMens, nota.inicio);
      faixas[nota.faixa].add(evento);
      
      notaMens = new ShortMessage();
      notaMens.setMessage(ShortMessage.NOTE_OFF, nota.nota, nota.velocidade);
      evento = new MidiEvent(notaMens, nota.inicio + nota.duracao);
      faixas[nota.faixa].add(evento);
    }
  }
  
  public void geraArquivo(String nomeArquivo) throws IOException {
    java.io.File arquivo = new java.io.File(nomeArquivo);
    MidiSystem.write(sequencia, 1, arquivo);
  }
}
