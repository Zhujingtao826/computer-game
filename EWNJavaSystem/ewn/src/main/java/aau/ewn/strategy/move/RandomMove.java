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

public class RandomMove extends MoveStrategy{

    public RandomMove(){
        super();
        setLabel("RandomMove");
    }

    @Override
    public Move getMove(GameState gameState, byte dice) {


		ChessBoard board = gameState.getCurrentBoard();

		PieceType player = gameState.getCurrentPlayer().getTurn();

		Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, player, dice);
		List<Move> keyList = new ArrayList<Move>(legalMoves.keySet());
		Move bestMove = null;

		int max_winNum = -1;
		
		for (int i = 0;i<keyList.size();i++){
			ChessBoard boardStep = legalMoves.get(keyList.get(i));
			int winNum = MonCa(boardStep, player);
			if(winNum > max_winNum){
				max_winNum = winNum;
				bestMove = keyList.get(i);
			}
		}
		this.value = max_winNum;
		this.maxDepth = 15;
		return bestMove;
			
    }

    private int MonCa(ChessBoard board, PieceType player) {

			int gameNum = 12000;

			int pieceWinNum = 0;
			

			PieceType firstPlayer = Reverse(player);
			

			Player play1 = new Player(player, new StaticInitial(),new StaticEvaluationMove(new MySuperEvaluate()));

			Player play2 = new Player(Reverse(player), new StaticInitial(), new RandomMove1());
			

			Player bluePlayer = null;
			Player redPlayer  = null;
			if (player == PieceType.BLUE) {
				bluePlayer = play1;
				redPlayer  = play2;
			}else{
				bluePlayer = play2;
				redPlayer  = play1;
			}
			

			GameState game1 = new GameState();
			game1.setPlayer(bluePlayer);
			game1.setPlayer(redPlayer);


			for(int cnt = 1; cnt <= gameNum; cnt++) {

				ChessBoard board1 = board.clone();

				game1.reset(firstPlayer, board1);

				while(game1.isEnd()==false) {

					byte dice = game1.getDice();

					Move move = game1.getCurrentPlayer().getMoveStrategy().getMove(game1, dice);

					game1.step(dice, move);
				}

				if (player == PieceType.BLUE) {
					if(game1.getWinner()==PieceType.BLUE) {
						pieceWinNum += 1;
					}
				}else {
					if(game1.getWinner()==PieceType.RED) {
						pieceWinNum += 1;
					}
				}
			}
			return pieceWinNum;
	}

	private PieceType Reverse(PieceType player) {
		return (player == PieceType.BLUE ? PieceType.RED : PieceType.BLUE);
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
