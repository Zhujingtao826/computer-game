package aau.ewn.strategy.evaluation;

import java.util.List;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;

public class MySuperEvaluate extends EvaluationFunction{

	@Override
	public double getValue(ChessBoard board, PieceType type) {
		// TODO 自动生成的方法存根

		double boardvred[][] = { { 0, 1, 1, 1, 1 }, { 1, 2, 2, 2, 2.5 }, { 1, 2, 4, 4, 5 }, { 1, 2, 4, 8, 10 }, { 1, 2.5, 5, 10, 16 } };
		double boardvblue[][] = { { 16, 10, 5, 2.5, 1 }, { 10, 8, 4, 2, 1 }, { 5, 4, 4, 2, 1 }, { 2.5, 2, 2, 2, 1 }, { 1, 1, 1, 1, 0 } };

		int p[] = new int[6];
		int p2[] = new int[6];

		double attackred = 0.0,threatred = 0.0;
		double attackblue = 0.0,threatblue = 0.0;

		double exp1=0.0,exp2=0.0,theat1 = 0.0,theat2 = 0.0;

		double value[] = new double [6];
		double value2[] = new double [6];

		double a=0,b=0,c=0;

		double maxvalue[] = new double[6];

		if(type == PieceType.BLUE){

			List<Byte> pieces = board.getPieces(type);

			for (int i =0; i<pieces.size();i++){

				List<Byte> count = board.getDicesByPiece(pieces.get(i));
				p[i] = count.size();

				int[] rowcol = board.getPointByPiece(pieces.get(i));

				value[i] = boardvblue[rowcol[0]][rowcol[1]];

				if(rowcol[0] >0 && rowcol[1] > 0 && board.getBoard()[rowcol[0] -1][rowcol[1]] > 20){
					a = boardvred[rowcol[0] -1][rowcol[1]];
				}else if(rowcol[0] >0 && rowcol[1] > 0 && board.getBoard()[rowcol[0]][rowcol[1] -1] > 20 ){
					b = boardvred[rowcol[0] -1][rowcol[1] - 1];
				}else if(rowcol[0] >0 && rowcol[1] > 0 && board.getBoard()[rowcol[0] -1][rowcol[1] -1] > 20){
					c = boardvred[rowcol[0] -1][rowcol[1] -1];
				}
				double max = (a > b) ? a : b;
				max = (max > c) ? max : c;
				maxvalue[i] = max;


			}

			for (int i =0; i<pieces.size();i++){
				attackblue = attackblue + p[i]*value[i];
				threatblue = threatblue + maxvalue[i]*p[i];
			}


			List<Byte> pieces2 = board.getPieces(PieceType.RED);

			for (int i =0; i<pieces2.size();i++){

				List<Byte> count2 = board.getDicesByPiece(pieces2.get(i));
				p2[i] = count2.size();

				int[] rowcol2 = board.getPointByPiece(pieces2.get(i));

				value2[i] = boardvred[rowcol2[0]][rowcol2[1]];
			}

			for (int i =0; i<pieces2.size();i++){
				attackred = attackred + p2[i]*value2[i];

			}
		}
		else if(type == PieceType.RED){

			List<Byte> pieces = board.getPieces(type);

			for (int i =0; i<pieces.size();i++){
				List<Byte> count = board.getDicesByPiece(pieces.get(i));
				p[i] = count.size();

				int[] rowcol = board.getPointByPiece(pieces.get(i));

				value[i] = boardvred[rowcol[0]][rowcol[1]];

				if(rowcol[0] <4 && rowcol[1] <4 && board.getBoard()[rowcol[0] + 1][rowcol[1]] > 10 && board.getBoard()[rowcol[0] + 1][rowcol[1]] < 20){
					a = boardvblue[rowcol[0] +1][rowcol[1]];
				}else if(rowcol[0] <4 && rowcol[1] <4 && board.getBoard()[rowcol[0]][rowcol[1] + 1] > 10 && board.getBoard()[rowcol[0]][rowcol[1] + 1] < 20){
					b = boardvblue[rowcol[0]][rowcol[1] + 1];
				}else if(rowcol[0] <4 && rowcol[1] <4 && board.getBoard()[rowcol[0] + 1][rowcol[1] + 1] > 10 && board.getBoard()[rowcol[0] + 1][rowcol[1] + 1] < 20){
					c = boardvblue[rowcol[0] +1][rowcol[1] +1];
				}
				double max = (a > b) ? a : b;
				max = (max > c) ? max : c;
				maxvalue[i] = max;
			}

			for (int i =0; i<pieces.size();i++){
				attackred  = attackred  + p[i]*value[i];
				threatred = threatred + maxvalue[i]*p[i];
			}

			List<Byte> pieces2 = board.getPieces(PieceType.BLUE);

			for (int i =0; i<pieces2.size();i++){

				List<Byte> count = board.getDicesByPiece(pieces2.get(i));
				p2[i] = count.size();

				int[] rowcol2 = board.getPointByPiece(pieces2.get(i));

				value2[i] = boardvblue[rowcol2[0]][rowcol2[1]];
			}

			for (int i =0; i<pieces2.size();i++){
				attackblue = attackblue + p2[i]*value2[i];

			}
		}

		if(type == PieceType.RED){
			exp1 = attackred;
			exp2 = -attackblue;
			theat1 = -threatred;
			theat2 = threatblue;
		}else if(type == PieceType.BLUE){
			exp1 = attackblue;
			exp2 = -attackred;
			theat1 = -threatblue;
			theat2 = threatred;
		}
		return 15*exp1 + 5*exp2 + 1*theat1 + 0*theat2;
	}


}
