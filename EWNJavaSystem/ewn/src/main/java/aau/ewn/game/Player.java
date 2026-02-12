package aau.ewn.game;

import aau.ewn.strategy.initial.InitialStrategy;
import aau.ewn.strategy.initial.StaticInitial;
import aau.ewn.strategy.move.AdvancedMoveStrategy;
import aau.ewn.strategy.move.MoveStrategy;
import aau.ewn.board.PieceType;
import aau.ewn.strategy.move.MySuperMove1;
import aau.ewn.strategy.move.RandomMove;

public class Player{

    private PieceType turn;

    private InitialStrategy initStrategy;

    private MoveStrategy moveStrategy;

    private long presettingTime=4*60;

    private static final int INTERVAL = 1000;

    private long runningTime;

    private String label;

    public Player(PieceType turn){
        this.turn=turn;
        this.initStrategy = new StaticInitial();
        this.moveStrategy = new RandomMove();
        setLabel(turn.toString());
    }

    public Player(PieceType turn,InitialStrategy initStrategy,MoveStrategy moveStrategy){
        this.turn=turn;
        this.initStrategy=initStrategy;
        this.moveStrategy=moveStrategy;
        setLabel(turn.toString());
    }

    public Player(PieceType turn,InitialStrategy initStrategy,MoveStrategy moveStrategy,long presettingTime){
        this.turn=turn;
        this.initStrategy=initStrategy;
        this.moveStrategy=moveStrategy;
        this.presettingTime=presettingTime;
        setLabel(turn.toString());
    }

    public PieceType getTurn() {
        return turn;
    }

    public void setTurn(PieceType turn) {
        this.turn = turn;
    }

    public InitialStrategy getInitStrategy() {
        return initStrategy;
    }

    public void setInitStrategy(InitialStrategy initStrategy) {
        this.initStrategy = initStrategy;
    }

    public MoveStrategy getMoveStrategy() {
        return moveStrategy;
    }

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    public long getPresettingTime() {
        return presettingTime;
    }

    public void setPresettingTime(long presettingTime) {
        this.presettingTime = presettingTime;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public void addRunningTime(long runningTime){
        this.runningTime+=runningTime;
    }

    public String getLabel(){
        return this.label;
    }

    public void setLabel(String label){
        this.label=label;
    }

    @Override
    public String toString(){
        return this.label;
    }
}

