package aau.ewn.strategy.move;

import aau.ewn.board.PieceType;
import aau.ewn.game.GameState;
import aau.ewn.game.Move;

abstract public class MoveStrategy{

    private String label;

    protected double value;

    protected int maxDepth;

	protected int visitNum;

	protected long runTime;
    public String getLabel(){
        return this.label;
    }

    public void setLabel(String label){
        this.label=label;
    }

    @Override
    public String toString(){
        return label;
    }

    abstract public Move getMove(GameState gameState,byte dice);

    abstract public void processEnemyMove(Move move);

    abstract public void processStart(GameState gameState,PieceType myTurn);

    abstract public void processBack(GameState gameState,Move move);

    abstract public void processEnd();

    public double getMoveValue(){
        return this.value;
    }

    public int getMaxDepth() {
		return this.maxDepth;
	}

	public int getVisitNum() {
		return this.visitNum;
	}

	public long getRunTime() {
		return this.runTime;
	}

    @Override
    public MoveStrategy clone(){
        return this.clone();
    }
}
