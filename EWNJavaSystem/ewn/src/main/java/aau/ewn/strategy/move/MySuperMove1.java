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
import aau.ewn.strategy.evaluation.*;
import java.lang.Math;

public class MySuperMove1 extends MoveStrategy{

	private static final int DEPTH = 2;
	private static final double INNF = 99999999;

	@Override
	public Move getMove(GameState gameState, byte dice) {

		ChessBoard board = gameState.getCurrentBoard();

		PieceType player = gameState.getCurrentPlayer().getTurn();

		Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, player, dice);

		List<Move> keyList = new ArrayList<Move>(legalMoves.keySet());

		Move bestMove = null;

		double best_code = -INNF;
		double code = 0.0;

		for (int i = 0;i<keyList.size();i++){

			ChessBoard board1 = board.clone();
			Move move = keyList.get(i);

			board1.processMove(move);

			if(player == PieceType.RED){
				code = odem(1,0,1,move,board1);
			}else{
				code = odem(1,0,0,move,board1);
			}

			if(code > best_code){
				best_code = code;
				bestMove = move;
			}
		}
			this.value = best_code;
			this.maxDepth = DEPTH;
           

		return bestMove;
		
	}
	

	double odem(int max,int dep,int side,Move move,ChessBoard board){
		double value=0;
		if(dep == DEPTH){
			MySuperEvaluate evl = new MySuperEvaluate();
			PieceType type = null;
			if(side == 0){
				type = PieceType.RED;
			}else{
				type = PieceType.BLUE;
			}
			value = evl.getValue(board, type);
			return value;
		}
		
		if(max == 1 && side ==1){
			PieceType type = null;
			if(side == 0){
				type = PieceType.RED;
			}else{
				type = PieceType.BLUE;
			}
			double valuemin = INNF;
			List<Byte> pieces = board.getPieces(type);				
			for (int i =0; i<pieces.size();i++){
				double maxvalue = -INNF;

				List<Byte> count = board.getDicesByPiece(pieces.get(i));
				int p = count.size();

				Map<Move,ChessBoard> moves = MoveGenerator.getLegalMovesByPiece(board,pieces.get(i));
				List<Move> keyList = new ArrayList<Move>(moves.keySet());
				for (int j = 0;j<keyList.size();j++){
					ChessBoard	boardtemp = board.clone();						
					Move moveblue = keyList.get(j);
					if(board.isWin(type)){
						value = INNF;
						return value;
					}
					boardtemp.processMove(moveblue);
					double valueBlue = odem(1,dep+1,0,moveblue,boardtemp);
					if(valueBlue > maxvalue)
						maxvalue = valueBlue;
					
				}
				double value1 = maxvalue * p;
				if(value1 < valuemin)
					valuemin = value1;
			}
		value = valuemin;
		}else if(max == 1 && side ==0){

			PieceType type = null;
			if(side == 0){
				type = PieceType.RED;
			}else{
				type = PieceType.BLUE;
			}
			double minvalue = INNF;
			List<Byte> pieces = board.getPieces(type);
			for (int i =0; i<pieces.size();i++){			
				double maxvalue = -INNF;

				List<Byte> count = board.getDicesByPiece(pieces.get(i));
				int p = count.size();
				Map<Move,ChessBoard> moves = MoveGenerator.getLegalMovesByPiece(board,pieces.get(i));
				List<Move> keyList = new ArrayList<Move>(moves.keySet());
				for (int j = 0;j<keyList.size();j++){			
					ChessBoard	boardtemp = board.clone();	
					Move movered = keyList.get(j);
					boardtemp.processMove(movered);
					if(board.isWin(type)){
						value = INNF;
						return value;
					}
					double valuered = odem(1,dep+1,1,movered,boardtemp);	
					if(valuered > maxvalue)
						maxvalue = valuered;
				}
				double value1 = maxvalue * p;
				if(value1 < minvalue)
					minvalue = value1;
			}
			value = minvalue;
		}
					
		return value;
		
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
