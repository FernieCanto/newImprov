/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso.gui;

import improviso.*;

/**
 *
 * @author User
 */
public class SectionConfiguration implements SectionVisitor {
    final static public int TYPE_FIXED = 1;
    final static public int TYPE_VARIABLE = 2;
    
    private int type;
    private int lengthMin;
    private int lengthMax;
    private int tempo;
    private boolean interruptTracks;
    
    public SectionConfiguration() {
        this.type = TYPE_FIXED;
        this.lengthMin = 0;
        this.lengthMax = 0;
        this.tempo = 120;
        this.interruptTracks = true;
    }

    @Override
    public void visit(FixedSection section) {
        this.type = TYPE_FIXED;
        this.lengthMin = section.getLength().getValueMin();
        this.lengthMax = section.getLength().getValueMax();
        this.tempo = section.getTempo();
        this.interruptTracks = section.getInterruptTracks();
    }

    @Override
    public void visit(VariableSection section) {
        this.type = TYPE_VARIABLE;
        this.lengthMin = 0;
        this.lengthMax = 0;
        this.tempo = section.getTempo();
        this.interruptTracks = section.getInterruptTracks();
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getLengthMin() {
        return lengthMin;
    }

    public void setLengthMin(int lengthMin) {
        this.lengthMin = lengthMin;
    }

    public Integer getLengthMax() {
        return lengthMax;
    }

    public void setLengthMax(int lengthMax) {
        this.lengthMax = lengthMax;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public boolean getInterruptTracks() {
        return interruptTracks;
    }

    public void setInterruptTracks(boolean interruptTracks) {
        this.interruptTracks = interruptTracks;
    }
}
