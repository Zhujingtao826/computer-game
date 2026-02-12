package aau.ewn.game;

import aau.ewn.board.Piece;

public class Move {

    private byte piece;

    private MoveDirection direction;

    public Move(byte piece, MoveDirection direction) {
        this.piece = piece;
        this.direction = direction;
    }

    public byte getPiece() {
        return piece;
    }

    public void setPiece(byte piece) {
        this.piece = piece;
    }

    public MoveDirection getDirection() {
        return direction;
    }

    public void setDirection(MoveDirection direction) {
        this.direction = direction;
    }

    public boolean compareTo(Move move) {
        // TODO 自动生成的方法存根
        if(piece == move.piece && direction==move.direction) return true;
        else return false;
    }

    @Override
    public Move clone(){
        Move move=new Move(this.piece,this.direction);
        return move;

    }

    public void printMove(int tabNum) {
        // TODO 自动生成的方法存根
        for(int i=0;i<tabNum;i++) System.out.print("	");
        System.out.println("Move Info：");
        for(int i=0;i<tabNum;i++) System.out.print("	");
        System.out.println("Piece:"+Piece.toString(piece));
        for(int i=0;i<tabNum;i++) System.out.print("	");
        System.out.println("MoveDirection:"+direction);
    }

    @Override
    public String toString(){
        return Piece.toString(piece).trim()+" "+direction;
    }

    @Override
    public boolean equals(Object object){
        Move move=(Move) object;
        return compareTo(move);
    }

    public int hashCode() {
    	return this.piece * 10 + this.direction.hashCode();
    }
}
