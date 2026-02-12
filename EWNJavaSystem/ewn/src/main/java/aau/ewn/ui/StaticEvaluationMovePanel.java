package aau.ewn.ui;

import aau.ewn.game.Player;
import aau.ewn.strategy.evaluation.EvaluationFunction;
import aau.ewn.strategy.evaluation.MySuperEvaluate;
import aau.ewn.strategy.evaluation.RandomEvaluate;
import aau.ewn.strategy.move.MoveStrategy;
import aau.ewn.strategy.move.StaticEvaluationMove;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class StaticEvaluationMovePanel extends MoveStrategyPanel {

	private static final long serialVersionUID = 8494673941148394734L;
	private StaticEvaluationMove moveStrategy;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private EvaluationFunction customEvaluate = new MySuperEvaluate();

	public StaticEvaluationMovePanel(Player player) {
		super(player);
		this.moveStrategy = new StaticEvaluationMove(new RandomEvaluate());
		initUI();
		
	}
	
	private void initUI() {
		setLayout(null);
		
		JRadioButton RandomEvaluateButton = new JRadioButton("随机估值");
		buttonGroup.add(RandomEvaluateButton);
		RandomEvaluateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveStrategy.setEvaluationFunciton(new RandomEvaluate());
			}
		});
		RandomEvaluateButton.setSelected(true);
		RandomEvaluateButton.setBounds(6, 6, 121, 23);
		add(RandomEvaluateButton);
		
		JRadioButton customEvaluateButton = new JRadioButton("超级估值");
		customEvaluateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveStrategy.setEvaluationFunciton(customEvaluate);
			}
		});
		buttonGroup.add(customEvaluateButton);
		customEvaluateButton.setBounds(6, 39, 121, 23);
		add(customEvaluateButton);
		
		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setText("注：超级估值会调用MySuperEvaluate.getValue()");
		area.setBounds(6, 64, 135, 142);
		add(area);
	}

	@Override
	public MoveStrategy getMoveStrategy() {
		// TODO 自动生成的方法存根
		return this.moveStrategy;
	}
}
