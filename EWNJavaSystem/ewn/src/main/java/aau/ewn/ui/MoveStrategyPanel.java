package aau.ewn.ui;

import javax.swing.JPanel;

import aau.ewn.game.Player;
import aau.ewn.strategy.move.MoveStrategy;
public abstract class MoveStrategyPanel extends JPanel {

	private static final long serialVersionUID = -4073663085914745169L;
	
	protected Player player;

	public MoveStrategyPanel(Player player) {
		this.player = player;
	}
	
	public abstract MoveStrategy getMoveStrategy();

}
