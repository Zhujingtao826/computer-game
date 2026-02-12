package aau.ewn.game;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;
import aau.ewn.record.GameRecord;
import aau.ewn.strategy.dice.DiceStrategy;
import aau.ewn.strategy.dice.RandomDice;

import java.util.HashMap;
import java.util.Map;


public class GameState {

    private Player currentPlayer;

    private ChessBoard currentBoard;

    private Map<PieceType, Player> players;

    private DiceStrategy diceStrategy;

    private GameRecord record;

    public GameState(ChessBoard board, Player player1, Player player2) {
        this.currentBoard = board;
        players = new HashMap<PieceType, Player>(2);
        this.players.put(player1.getTurn(), player1);
        this.players.put(player2.getTurn(), player2);
        this.diceStrategy = new RandomDice();
        this.record = new GameRecord();
        this.record.setPlayer(player1, player2);
    }

    public GameState() {
        players = new HashMap<PieceType, Player>(2);
        this.players.put(PieceType.BLUE, new Player(PieceType.BLUE));
        this.players.put(PieceType.RED, new Player(PieceType.RED));
        this.diceStrategy = new RandomDice();
        this.record = new GameRecord();
        this.record.setPlayer(players.get(PieceType.BLUE), players.get(PieceType.RED));
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(PieceType player) {
        this.currentPlayer = players.get(player);
    }

    public ChessBoard getCurrentBoard() {
        return this.currentBoard;
    }

    public Map<PieceType, Player> getPlayers() {
        return this.players;
    }

    public Player getPlayer(PieceType pieceType) {
    	return this.players.get(pieceType);
    }

    public void setPlayer(Player player) {
        this.players.remove(player.getTurn());
        this.players.put(player.getTurn(), player);
        this.record.setPlayer(player);
    }

    public DiceStrategy getDiceStrategy(){
        return this.diceStrategy;
    }

    public void setDiceStrategy(DiceStrategy diceStrategy){
        this.diceStrategy = diceStrategy;
    }

    public final boolean isWin(PieceType turn) {
        return this.currentBoard.isWin(turn);
    }

    public final boolean isEnd(){
        return currentBoard.isEnd();
    }

    public final PieceType getWinner(){
        return currentBoard.getWinner();
    }

    public void reset(PieceType firstTurn){

        this.currentBoard = new ChessBoard();

        this.currentPlayer = this.players.get(firstTurn);
        this.players.get(PieceType.BLUE).setRunningTime(0);
        this.players.get(PieceType.RED).setRunningTime(0);


        for(Player player:this.players.values()){
            PieceType turn=player.getTurn();
            ChessBoard board=player.getInitStrategy().getBoard(turn);

            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                    byte piece=board.getPieceByPoint(i, j);
                    if(piece!=0) this.currentBoard.setPieceLocation(piece, i, j);
                }
            }
        }
        

        this.record = new GameRecord();
        this.record.setPlayer(players.get(PieceType.BLUE), players.get(PieceType.RED));
        this.record.setFirstPlayer(firstTurn);
        this.record.setInitBoard(this.currentBoard.clone());
    }

    public void reset(PieceType firstTurn, ChessBoard initBoard){

        this.currentBoard = initBoard;

        this.currentPlayer = this.players.get(firstTurn);
        this.players.get(PieceType.BLUE).setRunningTime(0);
        this.players.get(PieceType.RED).setRunningTime(0);

        this.record = new GameRecord();
        this.record.setPlayer(players.get(PieceType.BLUE), players.get(PieceType.RED));
        this.record.setFirstPlayer(firstTurn);
        this.record.setInitBoard(currentBoard.clone());
    }

    public byte getDice(){
        return this.diceStrategy.getDice();
    }

    public void step(byte dice, Move move){
        if(isEnd()) return;

        byte eated_piece = currentBoard.processMove(move);
        if(eated_piece == -1) return;

        this.record.push(dice, move);
        this.record.setWinner(this.getWinner());

        if(this.currentPlayer.getTurn() == PieceType.BLUE){
            this.currentPlayer = this.players.get(PieceType.RED);
        }
        else{
            this.currentPlayer = this.players.get(PieceType.BLUE);
        }
    }

    public boolean pushBack() {

    	if(this.record.size() == 0) return false;

    	if(this.currentPlayer.getTurn() == PieceType.BLUE){
            this.currentPlayer = this.players.get(PieceType.RED);
        }
        else{
            this.currentPlayer = this.players.get(PieceType.BLUE);
        }

    	this.record.pop();
    	this.currentBoard = this.record.getBoard(this.record.size());
    	this.record.setWinner(this.getWinner());

    	if(this.record.size() == 0) this.reset(this.currentPlayer.getTurn(), this.record.getInitBoard());
    	return true;
    }

    public GameRecord getRecord() {
    	return this.record;
    }

    public void updateDate() {
		this.record.updateDate();
	}

	public String getDate() {
		return this.record.getDate();
	}

	public void setDate(String date) {
		this.record.setDate(date);
	}

	public String getPlace() {
		return this.record.getPlace();
	}

	public void setPlace(String place) {
		this.record.setPlace(place);
	}

	public String getCompetitionName() {
		return this.record.getCompetitionName();
	}

	public void setCompetitionName(String competitionName) {
		this.record.setCompetitionName(competitionName);
	}

	public String toString() {
		return this.record.toString();
	}

}
