package aau.ewn.strategy.initial;


import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;

abstract public class InitialStrategy{

    protected String label;

    public String getLabel(){
        return this.label;
    }

    public void setLabel(String label){
        this.label=label;
    }

    abstract public ChessBoard getBoard(PieceType myTurn);

    public static ChessBoard fuseBoards(ChessBoard initBoard1, ChessBoard initBoard2) {
    	ChessBoard board = initBoard1.clone();
    	for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                byte piece=initBoard2.getPieceByPoint(i, j);
                if(piece!=0) board.setPieceLocation(piece, i, j);
            }
        }
    	return board;
    }
    
    @Override
    public String toString(){
        return label;
    }
}