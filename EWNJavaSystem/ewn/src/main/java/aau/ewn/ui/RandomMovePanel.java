package aau.ewn.ui;

import aau.ewn.game.Player;
import aau.ewn.strategy.move.MoveStrategy;
import aau.ewn.strategy.move.RandomMove;

public class RandomMovePanel extends MoveStrategyPanel {

	private static final long serialVersionUID = 4593992634958175356L;
	public RandomMove moveStrategy;

	public RandomMovePanel(Player player) {
		super(player);
		moveStrategy = new RandomMove();
	}

	@Override
	public MoveStrategy getMoveStrategy() {
		// TODO 自动生成的方法存根
		return this.moveStrategy;
	}

}
