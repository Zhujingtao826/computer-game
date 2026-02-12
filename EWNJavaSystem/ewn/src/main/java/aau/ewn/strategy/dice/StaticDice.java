package aau.ewn.strategy.dice;

import java.util.ArrayList;
import java.util.List;

public class StaticDice extends DiceStrategy{

    private List<Byte> diceList;

    private int index=0;

    public StaticDice(){
        super();
        setLabel("StaticDice");

        diceList=new ArrayList<Byte>();
        index=0;
    }

    @Override
    public byte getDice() {
        // TODO 自动生成的方法存根
        byte dice=diceList.get(index);
        index=(index+1)%diceList.size();
        return dice;
    }

    public List<Byte> getDiceList(){
        return this.diceList;
    }

    public void setDiceList(List<Byte> diceList){
        this.diceList=diceList;
    }

    public void addDice(byte dice){
        diceList.add(dice);
    }

    public void addDice(byte... dices){
        for(byte dice:dices){
            diceList.add(dice);
        }
    }

    public int getNextIndex(){
        return this.index;
    }

    public void setNextIndex(int index){
        this.index=index;
    }

    public void clear(){
        diceList.clear();
        index=0;
    }
}
