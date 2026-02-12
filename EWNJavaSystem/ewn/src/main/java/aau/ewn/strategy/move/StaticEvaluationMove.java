package aau.ewn.strategy.move;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;
import aau.ewn.game.GameState;
import aau.ewn.game.Move;
import aau.ewn.game.MoveGenerator;
import aau.ewn.strategy.evaluation.EvaluationFunction;

import java.util.Map;

public class StaticEvaluationMove extends MoveStrategy{

    private EvaluationFunction evaluateFunction;

    public StaticEvaluationMove(EvaluationFunction function){
        super();
        this.evaluateFunction=function;
        setLabel("StaticEvaluationMove");
    }

    public EvaluationFunction getEvaluateFunction(){
        return this.evaluateFunction;
    }

    public void setEvaluationFunciton(EvaluationFunction evaluateFunction){
        this.evaluateFunction=evaluateFunction;
    }

    @Override
    public Move getMove(GameState gameState, byte dice) {
        // TODO 自动生成的方法存根

        PieceType turn=gameState.getCurrentPlayer().getTurn();
        ChessBoard board=gameState.getCurrentBoard();

        Map<Move,ChessBoard> moves= MoveGenerator.getLegalMovesByDice(board, turn, dice);
        double maxValue=Integer.MIN_VALUE;
        Move bestMove=null;
        for(Map.Entry<Move, ChessBoard> entry:moves.entrySet()){
            if(evaluateFunction.getValue(entry.getValue(), turn)>=maxValue){
                maxValue=evaluateFunction.getValue(entry.getValue(), turn);
                bestMove=entry.getKey();
            }
        }

        this.value=maxValue;

        if(bestMove==null){

        }

        return bestMove;
    }

    @Override
    public void processEnemyMove(Move move) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void processStart(GameState gameState, PieceType myTurn) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void processBack(GameState gameState, Move move) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void processEnd() {
        // TODO 自动生成的方法存根

    }

}
