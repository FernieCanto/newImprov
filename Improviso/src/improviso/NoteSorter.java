/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

/**
 *
 * @author fernando
 */
public class NoteSorter implements java.util.Comparator<Note> {
    @Override
    public int compare(Note n1, Note n2) {
        if(n1.start < n2.start)
            return -1;
        else if(n1.start > n2.start)
            return 1;
        else return 0;
    }
    
    public boolean equals(Note n1, Note n2) {
        return n1.start == n2.start;
    }
}
