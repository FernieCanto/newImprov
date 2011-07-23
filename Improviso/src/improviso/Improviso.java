/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class Improviso {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      ArrayList<Nota> notas;
      Secao secao;
      Trilha t1, t2;
      GrupoSorteio g1, g2;
      GeradorMIDI gerador;

      g1 = new GrupoSorteio();
      g1.adicionaFilho(new GrupoFolha(geraPadrao1()), 3);
      g1.adicionaFilho(new GrupoFolha(geraPadrao2()), 4);
      
      g2 = new GrupoSorteio();
      g2.adicionaFilho(new GrupoFolha(geraPadrao3()), 3);
      g2.adicionaFilho(new GrupoFolha(geraPadrao4()), 2);
      g2.adicionaFilho(new GrupoFolha(geraPadrao5()), 5);
      
      t1 = new Trilha(g1, 0);
      t2 = new Trilha(g2, 0);
      secao = new Secao("secao1", 0);
      secao.adicionaTrilha(t1);
      secao.adicionaTrilha(t2);
      secao.defineCondicaoTermino(new TerminoDuracao(1100, true));
      
      notas = secao.geraNotas();
      Collections.sort(notas, new OrdenadorNota());
      
      try {
        gerador = new GeradorMIDI(1);
        gerador.defineTempo(130);
        gerador.defineCompasso(4, 4);
        gerador.insereNotas(notas);
        gerador.geraArquivo("D:\\musica.mid");

        /*
        i = notas.iterator();
        while(i.hasNext()) {
          Nota n = i.next();
          System.out.printf("Nota %d, inicio %d, duracao %d, velocidade %d\n", n.nota, n.inicio, n.duracao, n.velocidade);
        }
        */
      }
      catch(Exception e) {
        System.out.println("Erro! "+e.getMessage());
      }
    }
    
    private static Padrao geraPadrao1() {
      Padrao p1 = new Padrao("teste", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(60);
      nota.defineInicio(0);
      nota.defineDuracao(120);
      nota.defineVelocidade(100);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(64);
      nota.defineInicio(120);
      nota.defineDuracao(120);
      nota.defineVelocidade(100, 10);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(67);
      nota.defineInicio(240);
      nota.defineDuracao(100, 20);
      nota.defineVelocidade(100, 15);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(72);
      nota.defineInicio(360);
      nota.defineDuracao(120);
      nota.defineVelocidade(90, 30);
      p1.adicionaNota(nota);

      return p1;
    }
    
    private static Padrao geraPadrao2() {
      Padrao p2 = new Padrao("teste2", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(81);
      nota.defineInicio(0);
      nota.defineDuracao(240);
      nota.defineVelocidade(100);
      p2.adicionaNota(nota);

      nota = new DefinicaoNota(77);
      nota.defineInicio(240);
      nota.defineDuracao(240);
      nota.defineVelocidade(80, 30);
      p2.adicionaNota(nota);
      
      return p2;
    }
    
    private static Padrao geraPadrao3() {
      Padrao p2 = new Padrao("teste2", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(48);
      nota.defineInicio(0);
      nota.defineDuracao(240);
      nota.defineVelocidade(100);
      p2.adicionaNota(nota);

      nota = new DefinicaoNota(55);
      nota.defineInicio(240);
      nota.defineDuracao(240);
      nota.defineVelocidade(80, 30);
      p2.adicionaNota(nota);
      
      return p2;
    }
    
    private static Padrao geraPadrao4() {
      Padrao p1 = new Padrao("teste", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(59);
      nota.defineInicio(0);
      nota.defineDuracao(120);
      nota.defineVelocidade(100);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(55);
      nota.defineInicio(120);
      nota.defineDuracao(120);
      nota.defineVelocidade(100, 10);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(50);
      nota.defineInicio(240);
      nota.defineDuracao(100, 20);
      nota.defineVelocidade(100, 15);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(47);
      nota.defineInicio(360);
      nota.defineDuracao(120);
      nota.defineVelocidade(90, 30);
      p1.adicionaNota(nota);

      return p1;
    }
    
    private static Padrao geraPadrao5() {
      Padrao p2 = new Padrao("teste2", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(36);
      nota.defineInicio(0);
      nota.defineDuracao(450);
      nota.defineVelocidade(100);
      p2.adicionaNota(nota);

      nota = new DefinicaoNota(40);
      nota.defineInicio(0);
      nota.defineDuracao(450);
      nota.defineVelocidade(80, 30);
      p2.adicionaNota(nota);

      nota = new DefinicaoNota(43);
      nota.defineInicio(0);
      nota.defineDuracao(450);
      nota.defineVelocidade(80, 30);
      p2.adicionaNota(nota);
      
      return p2;
    }
}
