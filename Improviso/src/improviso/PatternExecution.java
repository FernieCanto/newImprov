/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import java.util.Random;

/**
 *
 * @author fernando
 */
public class PatternExecution {
    
    private final Pattern pattern;
    private final int length;

    public PatternExecution(Pattern pattern, int length) {
        this.pattern = pattern;
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }

    public MIDINoteList execute(Random random, double finalPosition, Integer length) {
        MIDINoteList noteList = new MIDINoteList();
        this.pattern.getNoteIterator().forEachRemaining((improviso.Note note) -> {
            noteList.addAll(note.execute(random, this.length, finalPosition, length != null ? length : Integer.MAX_VALUE));
        });
        return noteList;
    }
    
}
