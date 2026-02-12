package aau.ewn.strategy.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;
import aau.ewn.game.GameState;
import aau.ewn.game.Move;
import aau.ewn.game.MoveGenerator;
import aau.ewn.game.Player;
import aau.ewn.strategy.evaluation.MySuperEvaluate;
import aau.ewn.strategy.initial.StaticInitial;

public class RandomMove1 extends MoveStrategy{

    public RandomMove1(){
        super();
        setLabel("RandomMove");
    }

    @Override
    public Move getMove(GameState gameState, byte dice) {
    	
        this.value = 0.5;
        ChessBoard board = gameState.getCurrentBoard();
        PieceType turn = gameState.getCurrentPlayer().getTurn();
        Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, turn, dice);
        List<Move> keyList = new ArrayList<Move>(legalMoves.keySet());
        Move randomKey = keyList.get(new Random().nextInt(keyList.size()));
        return randomKey;
			
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
