/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

/**
 *
 * @author fernando
 */
public class OrdenadorNota implements java.util.Comparator<Nota> {
    @Override
    public int compare(Nota n1, Nota n2) {
        if(n1.inicio < n2.inicio)
            return -1;
        else if(n1.inicio > n2.inicio)
            return 1;
        else return 0;
    }
    
    public boolean equals(Nota n1, Nota n2) {
        return n1.inicio == n2.inicio;
    }
}
