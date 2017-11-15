/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class MIDINoteList extends ArrayList<MIDINote> {
    final private int length;
    public MIDINoteList() {
        super();
        this.length = 0;
    }
    
    public MIDINoteList(MIDINote note) {
        super();
        this.length = note.getLength();
        this.add(note);
    }
    
    public MIDINoteList offsetNotes(int offset) {
        MIDINoteList newList = new MIDINoteList();
        this.forEach((note) -> {
            newList.add(new MIDINote(note, offset));
        });
        return newList;
    }
}
