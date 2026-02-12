package aau.ewn.game;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MoveGenerator {

    public static Map<Move, ChessBoard> getLegalMoves(ChessBoard board, PieceType turn){
        Map<Move,ChessBoard> moves=new HashMap<Move,ChessBoard>();

        List<Byte> allPieces=board.getPieces(turn);
        for(byte piece:allPieces){
            moves.putAll(getLegalMovesByPiece(board,piece));
        }

        return moves;
    }

    public static Map<Move,ChessBoard> getLegalMovesByPiece(ChessBoard board,byte piece){
        Map<Move,ChessBoard> moves=new HashMap<Move,ChessBoard>();

        Vector<MoveDirection> allDirections=new Vector<MoveDirection>();
        allDirections.add(MoveDirection.FORWARD);
        allDirections.add(MoveDirection.LEFT);
        allDirections.add(MoveDirection.RIGHT);

        while(allDirections.isEmpty()==false){
            MoveDirection dir=allDirections.remove(0);
            Move move=new Move(piece,dir);

            ChessBoard newBoard=board.clone();
            if(newBoard.processMove(move)!=-1) moves.put(move, newBoard);
        }

        return moves;
    }

    public static Map<Move,ChessBoard> getLegalMovesByDice(ChessBoard board,PieceType turn,byte dice){
        Map<Move,ChessBoard> moves=new HashMap<Move,ChessBoard>();
        List<Byte> pieces=board.getPiecesByDice(turn, dice);
        for(byte piece:pieces){
            moves.putAll(getLegalMovesByPiece(board, piece));
        }

        return moves;
    }
}
