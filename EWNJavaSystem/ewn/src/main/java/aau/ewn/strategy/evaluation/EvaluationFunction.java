package aau.ewn.strategy.evaluation;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;

abstract public class EvaluationFunction {

    private String label;

    public abstract double getValue(ChessBoard board,PieceType type);

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
