package aau.ewn.ui;

import javax.swing.JPanel;
import aau.ewn.board.ChessBoard;
import aau.ewn.board.Piece;
import aau.ewn.board.PieceType;
import aau.ewn.game.Player;
import aau.ewn.strategy.initial.InitialStrategy;
import aau.ewn.strategy.initial.RandomInitial;
import aau.ewn.strategy.initial.StaticInitial;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class PlayerSettingPanel extends JPanel {

	private static final long serialVersionUID = 4671248824274000889L;

	private Player player;
	
	private JTextField nameTextField;
	private JComboBox<String> initStrategyComboBox;
	
	
	private ArrayList<InitialStrategy> initStrategies;
	private JTextField staticInitialBoardTextField;
	private JButton staticInitialBoardConfirm;
	private JTabbedPane moveStrategyTabbedPane;

	public PlayerSettingPanel(PieceType type) {
		initialize(type);
	}
	
	private void initialize(PieceType type) {
		initData(type);
		initUI();
	}
	
	private void initData(PieceType type) {
		this.player = new Player(type);
		
		initStrategies = new ArrayList<InitialStrategy>();
		InitialStrategy strategy = new RandomInitial();
		strategy.setLabel("随机布局");
		initStrategies.add(strategy);
	}
	
	private void initUI() {
		setLayout(null);

		JLabel nameLabel = new JLabel("名称");
		nameLabel.setBounds(10, 10, 54, 15);
		add(nameLabel);

		nameTextField = new JTextField();
		nameTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				player.setLabel(nameTextField.getText());
			}
		});
		nameTextField.setBounds(51, 7, 120, 21);
		nameTextField.setText(player.getLabel());
		add(nameTextField);
		nameTextField.setColumns(10);

		JLabel initStrategyLabel = new JLabel("布局策略");
		initStrategyLabel.setBounds(10, 41, 80, 15);
		add(initStrategyLabel);

		initStrategyComboBox = new JComboBox<String>();
		initStrategyComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				InitialStrategy strategy = initStrategies.get(initStrategyComboBox.getSelectedIndex());
				player.setInitStrategy(strategy);
				if(initStrategyComboBox.getSelectedIndex() == 1) {
					StaticInitial s = (StaticInitial)strategy;

					staticInitialBoardTextField.setEditable(true);
					staticInitialBoardConfirm.setEnabled(true);
					staticInitialBoardTextField.setVisible(true);
					staticInitialBoardConfirm.setVisible(true);
				}
			}
		});
		initStrategyComboBox.setBounds(85, 38, 86, 21);
		for(InitialStrategy strategy: initStrategies) {
			initStrategyComboBox.addItem(strategy.toString());
		}
		initStrategyComboBox.setSelectedIndex(0);
		player.setInitStrategy(initStrategies.get(0));
		add(initStrategyComboBox);

		staticInitialBoardTextField = new JTextField();
		staticInitialBoardTextField.setBounds(85, 68, 86, 21);
		add(staticInitialBoardTextField);
		staticInitialBoardTextField.setEditable(false);
		staticInitialBoardTextField.setColumns(10);

		staticInitialBoardConfirm = new JButton("确认");
		staticInitialBoardConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String str = staticInitialBoardTextField.getText();

					StaticInitial initStrategy = (StaticInitial)player.getInitStrategy();

					staticInitialBoardTextField.setEditable(false);
					staticInitialBoardConfirm.setEnabled(false);
				}
				catch(Exception ex){
					staticInitialBoardTextField.setText("错误");
				}
			}
		});
		staticInitialBoardConfirm.setBounds(10, 66, 65, 23);
		staticInitialBoardConfirm.setEnabled(false);
		staticInitialBoardConfirm.setVisible(false);
		add(staticInitialBoardConfirm);



		moveStrategyTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		moveStrategyTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				MoveStrategyPanel panel = (MoveStrategyPanel)moveStrategyTabbedPane.getSelectedComponent();
				player.setMoveStrategy(panel.getMoveStrategy());
			}
		});

	}
	
	public Player getPlayer() {
		return this.player;
	}


}
