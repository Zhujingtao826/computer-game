package aau.ewn.strategy.initial;

import java.util.HashMap;
import java.util.Map;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.Piece;
import aau.ewn.board.PieceType;

public class StaticInitial extends InitialStrategy{

    private Map<PieceType, ChessBoard> boards;

    public StaticInitial() {
        super();
        setLabel("StaticInitial");

        boards=new HashMap<PieceType, ChessBoard>();

        ChessBoard redBoard=new ChessBoard();
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 1), 1, 0);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 2), 0, 1);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 3), 2, 0);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 4), 0, 2);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 5), 1, 1);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 6), 0, 0);

        ChessBoard blueBoard=new ChessBoard();
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 1), 3, 4);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 2), 4, 3);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 3), 2, 4);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 4), 4, 2);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 5), 3, 3);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 6), 4, 4);

        boards.put(PieceType.BLUE, blueBoard);
        boards.put(PieceType.RED, redBoard);
    }

    public StaticInitial(ChessBoard blueBoard, ChessBoard redBoard) {
        boards=new HashMap<PieceType, ChessBoard>();
        boards.put(PieceType.BLUE, blueBoard);
        boards.put(PieceType.RED, redBoard);
    }

    @Override
    public ChessBoard getBoard(PieceType myTurn) {
        // TODO 自动生成的方法存根
        return boards.get(myTurn);
    }

    public void setBoard(PieceType type, ChessBoard board) {
        boards.put(type, board);
    }
}
