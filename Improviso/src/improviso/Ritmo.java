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
public class Ritmo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      ArrayList<Nota> notas;
      Trilha t;
      GrupoSorteio g;
      Iterator<Nota> i;

      g = new GrupoSorteio();
      g.adicionaFilho(new Grupo(this.geraPadrao1), 1);
      
      Collections.sort(notas, new OrdenadorNota());
      i = notas.iterator();
      while(i.hasNext()) {
        Nota n = i.next();
        System.out.printf("Nota %d, inicio %d, duracao %d, velocidade %d\n", n.nota, n.inicio, n.duracao, n.velocidade);
      }
    }
    
    private Padrao geraPadrao1() {
      Padrao p1 = new Padrao("teste", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(10);
      nota.defineInicio(0);
      nota.defineDuracao(120);
      nota.defineVelocidade(100);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(20);
      nota.defineInicio(120);
      nota.defineDuracao(120);
      nota.defineVelocidade(100, 10);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(30);
      nota.defineInicio(240);
      nota.defineDuracao(100, 20);
      nota.defineVelocidade(100, 15);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(40);
      nota.defineInicio(360);
      nota.defineDuracao(120);
      nota.defineVelocidade(90, 30);
      p1.adicionaNota(nota);

      return p1;
    }
    
    private Padrao geraPadrao2() {
      Padrao p2 = new Padrao("teste2", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(100);
      nota.defineInicio(0);
      nota.defineDuracao(240);
      nota.defineVelocidade(100);
      p2.adicionaNota(nota);

      nota = new DefinicaoNota(120);
      nota.defineInicio(240);
      nota.defineDuracao(240);
      nota.defineVelocidade(80, 30);
      p2.adicionaNota(nota);
      
      return p2;
    }
}
