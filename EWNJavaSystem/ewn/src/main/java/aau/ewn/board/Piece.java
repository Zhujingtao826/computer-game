package aau.ewn.board;

public class Piece{

	public static byte create(PieceType type, byte number) {
		byte id = 0;
		if (type == PieceType.RED)
			id = (byte) (20 + number);
		else if (type == PieceType.BLUE)
			id = (byte) (10 + number);
		else id=0;
		return id;
	}

	public static final PieceType getPieceType(byte piece){
		if (piece / 10 == 2)
			return PieceType.RED;
		else if (piece / 10 == 1)
			return PieceType.BLUE;
		else
			return PieceType.NULL;
	}

	public static final byte getNumber(byte piece) {
		return (byte) (piece % 10);
	}

	public static final byte getID(PieceType type, byte number) {
		byte id=0;
		if (type == PieceType.RED)
			id = (byte) (20 + number);
		if (type == PieceType.BLUE)
			id = (byte) (10 + number);
		else
			id = 0;
		return id;
	}

	public static String toString(byte piece){
		if(Piece.getPieceType(piece) == PieceType.BLUE){
			return new String("B"+String.valueOf(Piece.getNumber(piece))+"\t");
		}
		else if(Piece.getPieceType(piece) == PieceType.RED){
			return new String("R"+String.valueOf(Piece.getNumber(piece))+"\t");
		}
		else return new String("-" + "\t");
	}
}