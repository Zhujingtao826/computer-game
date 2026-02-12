package aau.ewn.strategy.dice;

import java.util.Random;

public class RandomDice extends DiceStrategy {

    public RandomDice(){
        super();
        setLabel("RandomDice");
    }

    @Override
    public byte getDice() {
        Random random = new Random();
        return (byte)(1 + random.nextInt(6));
    }
}
