 package aau.ewn.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import aau.ewn.board.ChessBoard;
import aau.ewn.board.Piece;
import aau.ewn.board.PieceType;
import aau.ewn.game.Move;
import aau.ewn.game.MoveDirection;

import javax.swing.JLayeredPane;

public class ChessBoardPanel extends JPanel {

	private static final long serialVersionUID = 9018364710864079724L;

	private JLabel boardBG;

	private JLayeredPane layeredPane;

	private ConcurrentHashMap<Byte, PieceLabel> allPieces;

	private Move move;

	public ChessBoardPanel() {
		this.setBounds(0, 0, 400, 400);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 400, 400);
		add(layeredPane);
		

		boardBG=new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("res/chessBoard3.png")));
		boardBG.setBounds(0, 0, 400, 400);
		layeredPane.add(boardBG);
		layeredPane.setLayer(boardBG, 1);
		

		allPieces = new ConcurrentHashMap<Byte, PieceLabel>();
		for(int i=1;i<=2;i++){
			for(int j=1;j<=6;j++){
				PieceLabel piece=new PieceLabel((byte)(i*10+j), this);
				piece.setBounds(col2x(-1),row2y(-1),80,80);
				piece.setVisible(false);
				allPieces.put((byte)(i*10+j), piece);
				layeredPane.add(piece);
				layeredPane.setLayer(piece, 2);
			}
		}
	}

	public void updateBoardPanel(ChessBoard board) {
		setVisible(false);
		
		for(PieceLabel piece: allPieces.values()) {
			piece.setVisible(false);
		}
		
		for(int row=0;row<5; row++) {
			for(int col=0; col<5; col++) {
				byte id = board.getPieceByPoint(row, col);
				if(id==0) continue;
				allPieces.get(id).setVisible(true);
				allPieces.get(id).setBounds(col2x(col),row2y(row),80,80);
			}
		}
		
		setVisible(true);
	}

	public ChessBoard getChessBoard() {
		int[][] minPositions = new int[5][5];
		for(int[] arr: minPositions) Arrays.fill(arr, 100);
		
		for(PieceLabel piece: allPieces.values()) {
			int position = layeredPane.getPosition(piece);
			int row = y2row(piece.getY());
			int col = x2col(piece.getX());
			if(position < minPositions[row][col]) {
				minPositions[row][col] = position;
			}
		}
		
		ChessBoard board = new ChessBoard();
		
		for(PieceLabel piece: allPieces.values()) {
			int position = layeredPane.getPosition(piece);
			int row = y2row(piece.getY());
			int col = x2col(piece.getX());
			if(position == minPositions[row][col]) {
				board.setPieceLocation(piece.id, row, col);
			}
		}
		
		return board;
	}

	public void moveUp(byte piece) {
		this.layeredPane.moveToFront(allPieces.get(piece));
	}

	public void moveDown(byte piece) {
		this.layeredPane.moveToBack(allPieces.get(piece));
	}

	public void openHumanInitMode(PieceType turn) {
		for(PieceLabel piece: allPieces.values()) {
			if(Piece.getPieceType(piece.id) == turn) piece.setInitable(true);
		}
	}

	public void closeHumanInitMode(PieceType turn) {
		for(PieceLabel piece: allPieces.values()) {
			if(Piece.getPieceType(piece.id) == turn) piece.setInitable(false);
		}
	}

	public void openHumanMoveMode(PieceType turn, byte dice) {
		closeHumanMoveMode(turn);
		ChessBoard board = this.getChessBoard();
		List<Byte> pieces = board.getPieces(turn);
		for(Byte piece: pieces) {
			this.allPieces.get(piece).setMoveable(true);
		}
	}

	public void closeHumanMoveMode(PieceType turn) {
		for(PieceLabel piece: allPieces.values()) {
			if(Piece.getPieceType(piece.id) == turn) piece.setMoveable(false);
		}
		this.move=null;
	}

	private int col2x(int col){
		return 80*col;
	}

	private int row2y(int row){
		return 80*row;
	}

	private int y2row(int y) {
		return y / 80;
	}

	private int x2col(int x) {
		return x / 80;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}
	
}

class PieceLabel extends JLabel implements MouseMotionListener, MouseListener{

	private static final long serialVersionUID = -5600918267493658251L;

	public byte id;

	private boolean initable;

	private boolean moveable;

	private ChessBoardPanel boardPanel;

	private int[] fromPoint;

	private int[] movedVector;

	public PieceLabel(byte id, ChessBoardPanel parent) {
		super();
		this.id = id;
		this.initable = false;
		this.moveable = false;
		this.boardPanel = parent;
		this.fromPoint = new int[2];
		this.movedVector = new int[2];
		setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("res/piece"+id+".png")));
		setName(String.valueOf(id));
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	public void mouseDragged(MouseEvent e) {
		// TODO 自动生成的方法存根
		if(this.initable == true) {
			int newX = this.getX() + e.getX();
			int newY = this.getY() + e.getY();
			int row = y2row(newY);
			int col = x2col(newX);
			if(0<=row && row<=4 && 0<=col && col<=4) {
				this.setLocation(col2x(col), row2y(row));
			}
		}
		else if(this.moveable == true) {
			int newX = this.getX() + e.getX();
			int newY = this.getY() + e.getY();
			int row = y2row(newY);
			int col = x2col(newX);
			int movedRow = row - fromPoint[0];
			int movedCol = col - fromPoint[1];
			if(Piece.getPieceType(id) == PieceType.BLUE) {
				if((movedRow == 0 && movedCol == 0) || (movedRow == 0 && movedCol == -1) || (movedRow == -1 && movedCol == -1) || (movedRow == -1 && movedCol == 0)) {
					if(0<=row && row<=4 && 0<=col && col<=4) {
						movedVector[0] = movedRow;
						movedVector[1] = movedCol;
						this.setLocation(col2x(col), row2y(row));
					}
				}
			}
			else {
				if((movedRow == 0 && movedCol == 0) || (movedRow == 0 && movedCol == 1) || (movedRow == 1 && movedCol == 1) || (movedRow == 1 && movedCol == 0)) {
					if(0<=row && row<=4 && 0<=col && col<=4) {
						movedVector[0] = movedRow;
						movedVector[1] = movedCol;
						this.setLocation(col2x(col), row2y(row));
					}
				}
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	public void mouseClicked(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根
		boardPanel.moveUp(this.id);
	}

	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		if(moveable == true) {
			if(movedVector[0] == 0 && movedVector[1] == 0) {
				boardPanel.setMove(null);
			}
			else {
				MoveDirection direction = null;
				if(Piece.getPieceType(id) == PieceType.BLUE) {
					if(movedVector[0] == 0 && movedVector[1] == -1) direction = MoveDirection.LEFT;
					else if(movedVector[0] == -1 && movedVector[1] == -1) direction = MoveDirection.FORWARD;
					else if(movedVector[0] == -1 && movedVector[1] == 0) direction = MoveDirection.RIGHT;
				}
				else {
					if(movedVector[0] == 0 && movedVector[1] == 1) direction = MoveDirection.LEFT;
					else if(movedVector[0] == 1 && movedVector[1] == 1) direction = MoveDirection.FORWARD;
					else if(movedVector[0] == 1 && movedVector[1] == 0) direction = MoveDirection.RIGHT;
				}
				boardPanel.setMove(new Move(id, direction));
			}
		}
	}

	private int col2x(int col){
		return 80*col;
	}

	private int row2y(int row){
		return 80*row;
	}

	private int y2row(int y) {
		return y / 80;
	}

	private int x2col(int x) {
		return x / 80;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
		if(moveable == true) {
			this.fromPoint[0] = y2row(this.getY());
			this.fromPoint[1] = x2col(this.getX());
		}
	}

	public boolean isInitable() {
		return initable;
	}

	public void setInitable(boolean initable) {
		this.initable = initable;
	}
}