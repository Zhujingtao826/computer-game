package aau.ewn.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aau.ewn.game.Move;
import aau.ewn.game.MoveDirection;

public class ChessBoard {

    public static final short SIZE = 5;

    private byte[][] board;

    private byte pieceNum_blue = 0;

    private byte pieceNum_red = 0;

    public ChessBoard() {
        board = new byte[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(board[i], (byte) 0);
        }

        pieceNum_blue = 0;
        pieceNum_red = 0;
    }

    public void setPieceLocation(byte id, int row, int col) {
        this.board[row][col] = id;
        if (Piece.getPieceType(id) == PieceType.BLUE) pieceNum_blue++;
        else if (Piece.getPieceType(id) == PieceType.RED) pieceNum_red++;
    }

    public byte getPieceByPoint(int row, int col) {
        return this.board[row][col];
    }

    public int[] getPointByPiece(byte piece) {
        int[] rowcol = {0, 0};
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == piece) {
                    rowcol[0] = i;
                    rowcol[1] = j;
                    return rowcol;
                }
            }
        }
        return rowcol;
    }

    public byte[][] getBoard() {
        return this.board;
    }

    public int getPieceCount_RED() {
        return this.pieceNum_red;
    }

    public int getPieceCount_BLUE() {
        return this.pieceNum_blue;
    }

    public int getPieceCount(PieceType type) {
        if (type == PieceType.BLUE) return getPieceCount_BLUE();
        else if (type == PieceType.RED) return getPieceCount_RED();
        else return 25 - getPieceCount_BLUE() - getPieceCount_RED();
    }

    public List<Byte> getPiecesByDice(PieceType type, byte dice) {
        List<Byte> vector = new ArrayList<Byte>(2);
        byte[] allPiece = new byte[7];
        Arrays.fill(allPiece, (byte) 0);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Piece.getPieceType(board[i][j]) == type) allPiece[Piece.getNumber(board[i][j])] = 1;
            }
        }

        if (allPiece[dice] == 1) {
            vector.add(Piece.create(type, dice));
        } else {
            for (byte i = (byte) (dice + 1); i < 7; i++) {
                if (allPiece[i] == 1) {
                    vector.add(Piece.create(type, i));
                    break;
                }
            }
            for (byte i = (byte) (dice - 1); i > 0; i--) {
                if (allPiece[i] == 1) {
                    vector.add(Piece.create(type, i));
                    break;
                }
            }
        }

        return vector;
    }

    public List<Byte> getPieces(PieceType type) {
        List<Byte> vector = new ArrayList<Byte>(6);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Piece.getPieceType(board[i][j]) == type) vector.add(board[i][j]);
            }
        }

        return vector;
    }

    public List<Byte> getDicesByPiece(byte piece) {
        List<Byte> dices = new ArrayList<Byte>(6);
        byte pieceNumber = Piece.getNumber(piece);

        byte[] allPiece = new byte[7];
        Arrays.fill(allPiece, (byte) 0);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Piece.getPieceType(board[i][j]) == Piece.getPieceType(piece))
                    allPiece[Piece.getNumber(board[i][j])] = 1;
            }
        }

        //若该棋子不在棋盘上，返回空集
        if (allPiece[pieceNumber] == 0) return dices;

        dices.add(pieceNumber);

        for (int i = pieceNumber + 1; i < 7 && allPiece[i] == 0; i++) {
            dices.add((byte) i);
        }

        for (int i = pieceNumber - 1; i > 0 && allPiece[i] == 0; i--) {
            dices.add((byte) i);
        }

        return dices;
    }

    public byte processMove(Move move) {
        byte piece = move.getPiece();
        MoveDirection dir = move.getDirection();

        int[] p_old = getPointByPiece(piece);
        int[] p_new = getPointByPiece(piece);
        if (Piece.getPieceType(piece) == PieceType.BLUE) {
            if (dir == MoveDirection.FORWARD) {
                p_new[0]--;
                p_new[1]--;
            } else if (dir == MoveDirection.LEFT) {
                p_new[1]--;
            } else {
                p_new[0]--;
            }
        } else {
            if (dir == MoveDirection.FORWARD) {
                p_new[0]++;
                p_new[1]++;
            } else if (dir == MoveDirection.LEFT) {
                p_new[1]++;
            } else {
                p_new[0]++;
            }
        }

        if (p_new[0] >= SIZE || p_new[0] < 0 || p_new[1] >= SIZE || p_new[1] < 0) {
            return -1;
        }

        board[p_old[0]][p_old[1]] = 0;
        byte id = board[p_new[0]][p_new[1]];
        board[p_new[0]][p_new[1]] = piece;

        if (Piece.getPieceType(id) == PieceType.BLUE) pieceNum_blue--;
        else if (Piece.getPieceType(id) == PieceType.RED) pieceNum_red--;

        return id;
    }

    public final boolean isWin(PieceType turn) {
        if(turn == PieceType.BLUE){
            if (Piece.getPieceType(getPieceByPoint(0, 0)) == PieceType.BLUE) return true;
            if (pieceNum_red == 0) return true;
        }
        else{
            if (Piece.getPieceType(getPieceByPoint(4, 4)) == PieceType.RED) return true;
            if (pieceNum_blue == 0) return true;
        }
        return false;
    }

    public final boolean isEnd(){
        return isWin(PieceType.BLUE) || isWin(PieceType.RED);
    }

    public final PieceType getWinner(){
        if(isWin(PieceType.RED)) return PieceType.RED;
        if(isWin(PieceType.BLUE)) return PieceType.BLUE;
        return PieceType.NULL;
    }

    public void removePieceByPoint(int row, int col) {
        byte id = board[row][col];
        board[row][col] = 0;

        if (Piece.getPieceType(id) == PieceType.BLUE) pieceNum_blue--;
        else if (Piece.getPieceType(id) == PieceType.RED) pieceNum_red--;
    }

    public void printBoard(int tabNum) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < tabNum; k++) System.out.print("	");
                System.out.print(Piece.toString(this.board[i][j]) + "	");
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
    	
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                string.append(Piece.toString(board[i][j]));
            }
            if (i != 4) string.append("\n");
        }
        return string.toString();
    }

    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 0;
            }
        }
        pieceNum_red = 0;
        pieceNum_blue = 0;
    }

    @Override
    public ChessBoard clone() {
        ChessBoard newBoard = new ChessBoard();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newBoard.board[i][j] = this.board[i][j];
            }
        }
        newBoard.pieceNum_blue = this.pieceNum_blue;
        newBoard.pieceNum_red = this.pieceNum_red;
        return newBoard;
    }

    public boolean compareTo(ChessBoard board) {
        // TODO 自动生成的方法存根
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (this.board[i][j] != board.board[i][j]) return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object object) {
        ChessBoard board = (ChessBoard) object;
        return this.compareTo(board);
    }
}
