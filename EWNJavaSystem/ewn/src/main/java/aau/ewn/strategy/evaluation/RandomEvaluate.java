package aau.ewn.strategy.evaluation;

import java.util.Random;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;

public class RandomEvaluate extends EvaluationFunction {

	public RandomEvaluate() {
		setLabel("RandomEvaluate");
	}

	@Override
	public double getValue(ChessBoard board, PieceType type) {
		// TODO 自动生成的方法存根
		

		return new Random().nextDouble()*2 - 1;
	}

}
