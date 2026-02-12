package aau.ewn.record;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.Piece;
import aau.ewn.board.PieceType;
import aau.ewn.game.Move;
import aau.ewn.game.Player;

public class GameRecord {

	private Map<PieceType, String> players;

	private PieceType firstPlayer;

	private PieceType winner;

	private String date;

	private String place;

	private String competitionName;

	private ChessBoard initBoard;

	private ArrayList<Step> steps;

	public class Step{

		public PieceType player;

		public byte dice;

		public Move move;
	}

	public GameRecord() {
		this.players = new ConcurrentHashMap<PieceType, String>();
		this.winner = PieceType.NULL;
		this.firstPlayer = PieceType.NULL;
		this.updateDate();
		this.place = "安徽";
		this.competitionName = "计算机博弈";
		this.initBoard = new ChessBoard();
		this.steps = new ArrayList<Step>();
	}

	public ArrayList<Step> getSteps(){
		return this.steps;
	}

	public void push(byte dice, Move move) {
		Step step = new Step();
		step.player = Piece.getPieceType(move.getPiece());
		step.dice = dice;
		step.move = move.clone();
		
		if(this.steps.size() == 0) this.firstPlayer = step.player;
		this.steps.add(step);
	}

	public Step pop() {
		if(this.steps.size() == 0) return null;
		Step step = this.steps.remove(this.steps.size() - 1);
		return step;
	}

	public PieceType getFirstPlayer() {
		return this.firstPlayer;
	}

	public ChessBoard getBoard(int stepNum) {
		ChessBoard board = this.initBoard.clone();
		for(int i = 0; i < stepNum; i++) {
			Step step = this.steps.get(i);
			board.processMove(step.move);
		}
		return board;
	}

	public ChessBoard getInitBoard() {
		return initBoard;
	}

	public void setInitBoard(ChessBoard initBoard) {
		this.initBoard = initBoard;
	}

	public void setPlayer(Player... players) {
		for(Player player: players) {
			this.players.put(player.getTurn(), player.getLabel());
		}
	}

	public Map<PieceType, String> getPlayerNames(){
		return this.players;
	}

	public void setWinner(PieceType player) {
		this.winner = player;
	}

	public PieceType getWinner() {
		return this.winner;
	}

	public String getPlayerName(PieceType player) {
		return this.players.get(player);
	}

	public void updateDate() {
		Date dt = new Date();   
	    //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制   
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");   
	    this.date = sdf.format(dt);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getCompetitionName() {
		return competitionName;
	}

	public void setCompetitionName(String competitionName) {
		this.competitionName = competitionName;
	}

	public int size() {
		return this.steps.size();
	}

	public String getFileName() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");   
	    String time = sdf.format(dt);
	    String firstName = this.players.get(firstPlayer);
	    String lastName = this.players.get(firstPlayer==PieceType.BLUE? PieceType.RED: PieceType.BLUE);
	    String win = "null";
	    if(winner == firstPlayer) {
	    	win = "先手胜";
	    }
	    else if(winner != PieceType.NULL) {
	    	win = "后手胜";
	    }
	    return new String("WTN-"+firstName+"vs"+lastName+"-"+win+time);
	}

	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("#");

		str.append("[");
		if(this.firstPlayer == PieceType.NULL) {
			str.append("null");
		}
		else {
			str.append(this.players.get(this.firstPlayer));
		}
		str.append("]");

		str.append("[");
		if(this.firstPlayer == PieceType.NULL) {
			str.append("null");
		}
		else {
			PieceType lastPlayer = this.firstPlayer==PieceType.BLUE? PieceType.RED: PieceType.BLUE;
			str.append(this.players.get(lastPlayer));
		}
		str.append("]");

		str.append("[");
		if(this.winner == PieceType.NULL) str.append("null");
		else if(this.winner == this.firstPlayer) str.append("先手胜");
		else str.append("后手胜");
		str.append("]");
		// 1-4 添加比赛时间和地点
		str.append("[");
		str.append(this.date + " " + this.place);
		str.append("]");
		// 1-5 添加比赛名称
		str.append("[");
		str.append(this.competitionName);
		str.append("]");
		
		str.append(";\n");
		
		// 2 添加初始布局
		str.append(this.board2string());
		str.append("\n");
		
		// 3 添加走法序列
		for(int t = 0; t < this.steps.size(); t++) {
			Step step = this.steps.get(t);
			byte dice = step.dice;
			byte piece = step.move.getPiece();
			ChessBoard toBoard = this.getBoard(t+1);
			int[] toPoint = toBoard.getPointByPiece(piece);
			
			str.append(String.valueOf(t+1) + ":");
			str.append(String.valueOf(dice) + ";");
			str.append("(");
			if(Piece.getPieceType(piece) == PieceType.BLUE){
				str.append("B"+String.valueOf(Piece.getNumber(piece)));
			}
			else if(Piece.getPieceType(piece) == PieceType.RED){
				str.append("R"+String.valueOf(Piece.getNumber(piece)));
			}
			str.append(",");
			str.append(this.rowcol2string(toPoint[0], toPoint[1]));
			str.append(")");
			if(t != this.steps.size() - 1) str.append("\n");
		}
		
		return str.toString();
	}

	public void save(String fileName) {
		File file=new File(fileName);
    	try{
    		FileWriter writer = new FileWriter(file);
    		writer.write(this.toString());
    		writer.flush();
    		writer.close();
    	}
    	catch(IOException ex){
    		new IOException("Error saving data set file!",ex).printStackTrace();
    	}
	}

	private String board2string() {
		StringBuilder str = new StringBuilder();
		// 1 添加红方棋子位置
		str.append("R:");
		for(byte number=1; number<=6; number++) {
			int[] rowcol = this.initBoard.getPointByPiece(Piece.create(PieceType.RED, number));
			str.append(this.rowcol2string(rowcol[0], rowcol[1]) + "-" + String.valueOf(number));
			if(number != 6) str.append(";");
		}
		str.append("\n");
		// 1 添加蓝方棋子位置
		str.append("B:");
		for(byte number=1; number<=6; number++) {
			int[] rowcol = this.initBoard.getPointByPiece(Piece.create(PieceType.BLUE, number));
			str.append(this.rowcol2string(rowcol[0], rowcol[1]) + "-" + String.valueOf(number));
			if(number != 6) str.append(";");
		}

		return str.toString();
	}

	private String rowcol2string(int row, int col) {
		String[] colStr = new String[]{"A", "B", "C", "D", "E"};
		String[] rowStr = new String[]{"5", "4", "3", "2", "1"};
		return new String(colStr[col] + rowStr[row]);
	}

	public void setFirstPlayer(PieceType firstTurn) {
		// TODO 自动生成的方法存根
		this.firstPlayer = firstTurn;
	}
}
