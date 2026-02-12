package aau.ewn.strategy.dice;

abstract public class DiceStrategy{

    protected String label;

    abstract public byte getDice();

    public String getLabel(){
        return this.label;
    }

    public void setLabel(String label){
        this.label=label;
    }

    @Override
    public String toString(){
        return this.label;
    }
}
