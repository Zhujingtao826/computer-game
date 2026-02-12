package aau.ewn.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;
import aau.ewn.game.GameState;
import aau.ewn.game.Move;
import aau.ewn.game.Player;
import aau.ewn.record.GameRecord;
import aau.ewn.strategy.dice.RandomDice;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.io.File;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;


public class GameFrame extends JFrame {

	enum State{

		stop,

		init,

		move,

		human,

		computer,

		run
	};

	private static final long serialVersionUID = -2339906164422135479L;

	private static final long presettingTime = 30*60;

	private static final int INTERVAL = 1000;

	private GameState gameState;

	private PieceType firstPlayer;
	private PieceType isRedTurn;

	private byte dice;

	private Move move;

	private State state;

	private Map<PieceType, State> initState;

	private Map<PieceType, State> moveState;

	private State autoState;

	private JPanel contentPane;

	private ChessBoardPanel boardPanel;

	private JPanel gameControlPanel;

	private JTabbedPane playerSettingTabbedPane;

	private JPanel gameStateDisplayPanel;

	private Map<PieceType, PlayerSettingPanel> playerSettingPanels;
	private JLabel diceLabel;

	private JRadioButton[] diceButtons;

	private final ButtonGroup diceButtonGroup = new ButtonGroup();

	private JButton diceCreateButton;

	private JButton initButton;

	private JButton moveButton;

	private JButton pushBackButton;
	private JLabel currentPlayerLabel;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;

	private final ButtonGroup redInitButtonGroup = new ButtonGroup();
	private final ButtonGroup blueInitButtonGroup = new ButtonGroup();

	private final ButtonGroup redMoveButtonGroup = new ButtonGroup();
	private final ButtonGroup blueMoveButtonGroup = new ButtonGroup();

	private JTextField redRunTimeTextField;
	private JTextField blueRunTimeTextField;
	private JLabel label_8;
	private JLabel label_9;
	private JLabel label_10;

	private JTextField competitionTimeTextField;
	private JTextField competitionPlaceTextField;
	private JTextField competitionNameTextField;
	private JLabel redTimerLabel;
	private JLabel blueTimerLabel;
	private Timer redTimer;
	private Timer blueTimer;
	private long redRemainingTime = presettingTime;
	private long blueRemainingTime = presettingTime;

	private JLabel label_15;
	private final ButtonGroup firstMoveButtonGroup = new ButtonGroup();
	private JRadioButton redFirstRadioButton;
	private JRadioButton blueFirstRadioButton;

	private JPanel recordDisplayPanel;

	private JTextArea recordDisplayTextArea;

	private JButton autoStartButton;
	private JRadioButton redHumanInitButton;
	private JRadioButton redComputerInitButton;
	private JRadioButton redHumanMoveButton;
	private JRadioButton redComputerMoveButton;
	private JRadioButton blueHumanInitButton;
	private JRadioButton blueComputerInitButton;
	private JRadioButton blueHumanMoveButton;
	private JRadioButton blueComputerMoveButton;
	private JLabel label_18;
	private JTextField autoRedWinNumTextField;
	private JLabel label_19;
	private JLabel label_20;

	public GameFrame() {
		setTitle("安徽农业大学");
		this.initialize();

	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 789, 587);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		initData();
		initUI();
	}

	private void initData() {
		this.gameState = new GameState();
		dice = 1;
		firstPlayer = PieceType.BLUE;
		state = State.stop;
		autoState = State.stop;
		gameState.reset(firstPlayer);
		initState = new ConcurrentHashMap<PieceType, GameFrame.State>();
		moveState = new ConcurrentHashMap<PieceType, GameFrame.State>();
	}

	private void initUI() {
		playerSettingPanels = new ConcurrentHashMap<PieceType, PlayerSettingPanel>();

		// 初始化棋盘面板
		initBoardPanel();
		initGameControlPanel();
		initPlayerSettingTabbedPane();
		initGameStateDisplayPanel();
		initRecordDisplayPanel();

	}

	private void initBoardPanel() {
		boardPanel = new ChessBoardPanel();
		contentPane.add(boardPanel);
		boardPanel.setLayout(null);


		boardPanel.setVisible(true);
	}

	private void initGameControlPanel() {
		gameControlPanel = new JPanel();
		gameControlPanel.setBounds(0, 410, 400, 134);
		contentPane.add(gameControlPanel);
		gameControlPanel.setLayout(null);

		diceLabel = new JLabel("骰子");
		diceLabel.setBounds(10, 105, 36, 16);
		gameControlPanel.add(diceLabel);

		this.diceButtons = new JRadioButton[7];
		diceButtons[1] = new JRadioButton("1");
		diceButtons[1].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 1;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[1]);
		diceButtons[1].setBounds(48, 101, 36, 23);
		gameControlPanel.add(diceButtons[1]);

		diceButtons[2] = new JRadioButton("2");
		diceButtons[2].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 2;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[2]);
		diceButtons[2].setBounds(86, 101, 36, 23);
		gameControlPanel.add(diceButtons[2]);

		diceButtons[3] = new JRadioButton("3");
		diceButtons[3].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 3;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[3]);
		diceButtons[3].setBounds(124, 101, 36, 23);
		gameControlPanel.add(diceButtons[3]);

		diceButtons[4] = new JRadioButton("4");
		diceButtons[4].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 4;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[4]);
		diceButtons[4].setBounds(162, 101, 36, 23);
		gameControlPanel.add(diceButtons[4]);

		diceButtons[5] = new JRadioButton("5");
		diceButtons[5].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 5;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[5]);
		diceButtons[5].setBounds(200, 101, 36, 23);
		gameControlPanel.add(diceButtons[5]);

		diceButtons[6] = new JRadioButton("6");
		diceButtons[6].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 6;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.closeHumanMoveMode(gameState.getCurrentPlayer().getTurn());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[6]);
		diceButtons[6].setBounds(238, 101, 36, 23);
		gameControlPanel.add(diceButtons[6]);

		diceButtons[dice].setSelected(true);
		diceCreateButton = new JButton("投骰");
		diceCreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				diceCreateButtonCallback();
			}
		});
		diceCreateButton.setBounds(302, 40, 69, 23);
		gameControlPanel.add(diceCreateButton);

		initButton = new JButton("布局");
		initButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initButtonCallback();
			}
		});
		initButton.setBounds(302, 9, 69, 23);
		gameControlPanel.add(initButton);

		redTimer = createTimer("Red");
		blueTimer = createTimer("Blue");
		moveButton = new JButton("下棋");
		moveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						if (isRedTurn==PieceType.RED) {
							redTimer.start();
							blueTimer.stop();
							isRedTurn=PieceType.BLUE;
						} else {
							redTimer.stop();
							blueTimer.start();
							isRedTurn=PieceType.RED;
						}
						moveButtonCallback();
					}
				}).start();
			}
		});
		moveButton.setBounds(302, 71, 69, 23);
		gameControlPanel.add(moveButton);

		pushBackButton = new JButton("悔棋");
		pushBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pushBackButtonCallback();
			}
		});
		pushBackButton.setBounds(302, 102, 69, 23);
		gameControlPanel.add(pushBackButton);

		label = new JLabel("蓝方");
		label.setBounds(10, 75, 36, 15);
		gameControlPanel.add(label);

		label_1 = new JLabel("红方");
		label_1.setBounds(10, 54, 36, 15);
		gameControlPanel.add(label_1);

		label_2 = new JLabel("布局");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(47, 31, 105, 15);
		gameControlPanel.add(label_2);

		label_3 = new JLabel("走子");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(162, 31, 100, 15);
		gameControlPanel.add(label_3);

		redHumanInitButton = new JRadioButton("玩家");
		redHumanInitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				initState.put(PieceType.RED, State.human);
				if(state == State.init) {
					boardPanel.openHumanInitMode(PieceType.RED);
				}
			}
		});
		redInitButtonGroup.add(redHumanInitButton);
		redHumanInitButton.setBounds(45, 50, 58, 23);
		gameControlPanel.add(redHumanInitButton);

		redComputerInitButton = new JRadioButton("电脑");
		redComputerInitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initState.put(PieceType.RED, State.computer);
				boardPanel.closeHumanInitMode(PieceType.RED);
			}
		});
		redComputerInitButton.setSelected(true);
		initState.put(PieceType.RED, State.computer);
		redInitButtonGroup.add(redComputerInitButton);
		redComputerInitButton.setBounds(99, 50, 58, 23);
		gameControlPanel.add(redComputerInitButton);

		redHumanMoveButton = new JRadioButton("玩家");
		redHumanMoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveState.put(PieceType.RED, State.human);
				if(state == State.move && gameState.getCurrentPlayer().getTurn() == PieceType.RED) {
					boardPanel.openHumanMoveMode(PieceType.RED, dice);
				}
			}
		});
		redMoveButtonGroup.add(redHumanMoveButton);
		redHumanMoveButton.setBounds(154, 50, 58, 23);
		gameControlPanel.add(redHumanMoveButton);

		redComputerMoveButton = new JRadioButton("电脑");
		redComputerMoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveState.put(PieceType.RED, State.computer);
				boardPanel.updateBoardPanel(gameState.getCurrentBoard());
				boardPanel.closeHumanMoveMode(PieceType.RED);
			}
		});
		redComputerMoveButton.setSelected(true);
		moveState.put(PieceType.RED, State.computer);
		redMoveButtonGroup.add(redComputerMoveButton);
		redComputerMoveButton.setBounds(211, 50, 58, 23);
		gameControlPanel.add(redComputerMoveButton);

		blueHumanInitButton = new JRadioButton("玩家");
		blueHumanInitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initState.put(PieceType.BLUE, State.human);
				if(state == State.init) {
					boardPanel.openHumanInitMode(PieceType.BLUE);
				}
			}
		});
		blueInitButtonGroup.add(blueHumanInitButton);
		blueHumanInitButton.setBounds(45, 71, 58, 23);
		gameControlPanel.add(blueHumanInitButton);

		blueComputerInitButton = new JRadioButton("电脑");
		blueComputerInitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initState.put(PieceType.BLUE, State.computer);
				boardPanel.closeHumanInitMode(PieceType.BLUE);
			}
		});
		blueComputerInitButton.setSelected(true);
		initState.put(PieceType.BLUE, State.computer);
		blueInitButtonGroup.add(blueComputerInitButton);
		blueComputerInitButton.setBounds(99, 71, 58, 23);
		gameControlPanel.add(blueComputerInitButton);

		blueHumanMoveButton = new JRadioButton("玩家");
		blueHumanMoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveState.put(PieceType.BLUE, State.human);
				if(state == State.move && gameState.getCurrentPlayer().getTurn() == PieceType.BLUE) {
					boardPanel.openHumanMoveMode(PieceType.BLUE, dice);
				}
			}
		});
		blueMoveButtonGroup.add(blueHumanMoveButton);
		blueHumanMoveButton.setBounds(154, 71, 58, 23);
		gameControlPanel.add(blueHumanMoveButton);

		blueComputerMoveButton = new JRadioButton("电脑");
		blueComputerMoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveState.put(PieceType.BLUE, State.computer);
				boardPanel.updateBoardPanel(gameState.getCurrentBoard());
				boardPanel.closeHumanMoveMode(PieceType.BLUE);
			}
		});
		blueComputerMoveButton.setSelected(true);
		moveState.put(PieceType.BLUE, State.computer);
		blueMoveButtonGroup.add(blueComputerMoveButton);
		blueComputerMoveButton.setBounds(211, 71, 58, 23);
		gameControlPanel.add(blueComputerMoveButton);

		label_20 = new JLabel("游戏控制：");
		label_20.setBounds(10, 9, 80, 15);
		gameControlPanel.add(label_20);
		gameControlPanel.setVisible(true);
	}

	private void initPlayerSettingTabbedPane() {
		playerSettingTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		playerSettingTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				PlayerSettingPanel panel = (PlayerSettingPanel)(playerSettingTabbedPane.getSelectedComponent());
				gameState.setPlayer(panel.getPlayer());
			}
		});
		playerSettingTabbedPane.setBounds(593, 10, 178, 190);
		contentPane.add(playerSettingTabbedPane);

		initPlayerSettingPanel(PieceType.RED);
		initPlayerSettingPanel(PieceType.BLUE);
		playerSettingTabbedPane.addTab("红方", this.playerSettingPanels.get(PieceType.RED));
		playerSettingTabbedPane.addTab("蓝方", this.playerSettingPanels.get(PieceType.BLUE));

		gameState.setPlayer(playerSettingPanels.get(PieceType.BLUE).getPlayer());
		gameState.setPlayer(playerSettingPanels.get(PieceType.RED).getPlayer());
		playerSettingTabbedPane.setVisible(true);
	}

	private void initPlayerSettingPanel(PieceType player) {
		PlayerSettingPanel panel = new PlayerSettingPanel(player);
		this.playerSettingPanels.put(player, panel);
	}

	private void initGameStateDisplayPanel() {
		gameStateDisplayPanel = new JPanel();
		gameStateDisplayPanel.setBounds(410, 10, 173, 450);
		contentPane.add(gameStateDisplayPanel);
		gameStateDisplayPanel.setLayout(null);

		// 创建计时器标签
		redTimerLabel = new JLabel(formatTime(redRemainingTime));
		blueTimerLabel = new JLabel(formatTime(blueRemainingTime));


		redTimerLabel.setBounds(74, 279, 100, 30);
		gameStateDisplayPanel.add(redTimerLabel);
		redTimerLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
		blueTimerLabel.setBounds(74, 402, 100, 30);
		gameStateDisplayPanel.add(blueTimerLabel);
		blueTimerLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));


		redTimer = createTimer("Red");
		blueTimer = createTimer("Blue");

		JLabel lblNewLabel = new JLabel("红方：");
		lblNewLabel.setBounds(4, 262, 54, 16);
		gameStateDisplayPanel.add(lblNewLabel);

		JLabel label_5 = new JLabel("蓝方：");
		label_5.setBounds(4, 372, 54, 16);
		gameStateDisplayPanel.add(label_5);




		label_8 = new JLabel("比赛日期");
		label_8.setBounds(4, 5, 60, 20);
		gameStateDisplayPanel.add(label_8);

		label_9 = new JLabel("比赛地点");
		label_9.setBounds(4, 65, 60, 16);
		gameStateDisplayPanel.add(label_9);

		label_10 = new JLabel("比赛名称");
		label_10.setBounds(4, 125, 60, 16);
		gameStateDisplayPanel.add(label_10);

		competitionTimeTextField = new JTextField();
		competitionTimeTextField.setEditable(false);
		competitionTimeTextField.setBounds(37, 25, 135, 21);
		competitionTimeTextField.setText(gameState.getDate());
		gameStateDisplayPanel.add(competitionTimeTextField);
		competitionTimeTextField.setColumns(10);

		competitionPlaceTextField = new JTextField();
		competitionPlaceTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				gameState.setPlace(competitionPlaceTextField.getText());
			}
		});
		competitionPlaceTextField.setBounds(37, 85, 135, 21);
		competitionPlaceTextField.setText(gameState.getPlace());
		gameStateDisplayPanel.add(competitionPlaceTextField);
		competitionPlaceTextField.setColumns(10);

		competitionNameTextField = new JTextField();
		competitionNameTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				gameState.setCompetitionName(competitionNameTextField.getText());
			}
		});
		competitionNameTextField.setBounds(37, 145, 135, 21);
		competitionNameTextField.setText(gameState.getCompetitionName());
		gameStateDisplayPanel.add(competitionNameTextField);
		competitionNameTextField.setColumns(10);


		label_15 = new JLabel("先手方：");
		label_15.setBounds(4, 192, 60, 20);
		gameStateDisplayPanel.add(label_15);

		redFirstRadioButton = new JRadioButton("红");
		redFirstRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				firstPlayer = PieceType.RED;
			}
		});
		firstMoveButtonGroup.add(redFirstRadioButton);
		redFirstRadioButton.setBounds(78, 188, 50, 23);
		gameStateDisplayPanel.add(redFirstRadioButton);

		blueFirstRadioButton = new JRadioButton("蓝");
		blueFirstRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				firstPlayer = PieceType.BLUE;
			}
		});
		firstMoveButtonGroup.add(blueFirstRadioButton);
		blueFirstRadioButton.setBounds(130, 188, 50, 23);
		gameStateDisplayPanel.add(blueFirstRadioButton);

		currentPlayerLabel = new JLabel("行棋方：");
		currentPlayerLabel.setBounds(4, 227, 153, 20);
		gameStateDisplayPanel.add(currentPlayerLabel);

		if(firstPlayer == PieceType.BLUE) blueFirstRadioButton.setSelected(true);
		else redFirstRadioButton.setSelected(true);
	}

	private void initRecordDisplayPanel() {
		recordDisplayPanel = new JPanel();
		recordDisplayPanel.setBounds(600, 240, 163, 240);
		contentPane.add(recordDisplayPanel);
		recordDisplayPanel.setLayout(null);

		JLabel label_5 = new JLabel("棋谱：");
		label_5.setBounds(10, 5, 54, 15);
		recordDisplayPanel.add(label_5);


		recordDisplayTextArea = new JTextArea();

	    JScrollPane jsp = new JScrollPane(recordDisplayTextArea);

	    jsp.setBounds(0, 25, 163, 230);

	    jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    jsp.setAutoscrolls(true);

	    recordDisplayPanel.add(jsp);

	    JButton saveRecordButton = new JButton("保存");
	    saveRecordButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		saveRecordButtonCallback();
	    	}
	    });
	    saveRecordButton.setBounds(90, 1, 69, 23);
	    recordDisplayPanel.add(saveRecordButton);
	}


	private void diceCreateButtonCallback() {
		dice = new RandomDice().getDice();
		diceButtons[dice].setSelected(true);
		if(state == State.move) {
			if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
				boardPanel.updateBoardPanel(gameState.getCurrentBoard());
				boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
			}
		}
	}

	private void initButtonCallback() {
		if(state == State.move) {

			state = State.stop;
			initButtonCallback();
		}
		else if(state == State.init) {

			initButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					redTimer.stop();
					blueTimer.stop();
					redTimerLabel.setText(formatTime((long) (4*60)));
					redTimerLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
					blueTimerLabel.setText(formatTime((long) (4*60)));
					blueTimerLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
				}
			});
			ChessBoard board = boardPanel.getChessBoard();
			gameState.reset(firstPlayer, board);
			boardPanel.closeHumanInitMode(PieceType.BLUE);
			boardPanel.closeHumanInitMode(PieceType.RED);
			if(moveState.get(PieceType.BLUE) == State.human) {
				boardPanel.openHumanMoveMode(PieceType.BLUE, dice);
			}
			if(moveState.get(PieceType.RED) == State.human) {
				boardPanel.openHumanMoveMode(PieceType.BLUE, dice);
			}
			state = State.move;
			initButton.setText("重置");
			competitionTimeTextField.setText(gameState.getDate());

			recordDisplayTextArea.setText(gameState.getRecord().toString());
		}
		else if(state == State.stop) {

			initButton.setText("确认");
			state = State.init;

			gameState.reset(firstPlayer);
			boardPanel.updateBoardPanel(gameState.getCurrentBoard());

			if(initState.get(PieceType.BLUE) == State.human) {
				boardPanel.openHumanInitMode(PieceType.BLUE);
			}
			if(initState.get(PieceType.RED) == State.human) {
				boardPanel.openHumanInitMode(PieceType.RED);
			}

			competitionTimeTextField.setText(gameState.getDate());

			updateGameControlPanel();
		}
	}

	private void moveButtonCallback() {
		if(state == State.stop || state == State.init) return;

		state = State.move;

		if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.computer) {
			moveButton.setEnabled(false);
			moveButton.setText("稍等");
			boardPanel.closeHumanMoveMode(PieceType.BLUE);
			boardPanel.closeHumanMoveMode(PieceType.RED);

			Player player = gameState.getCurrentPlayer();
			long startTime = System.currentTimeMillis();
			move = player.getMoveStrategy().getMove(gameState, dice);
			long runTime = System.currentTimeMillis() - startTime;
			player.addRunningTime(runTime);

			gameState.step(dice, move);

			moveButton.setEnabled(true);
			moveButton.setText("下棋");
		}
		else {


			move = boardPanel.getMove();
			if(move == null) return;
			boardPanel.closeHumanMoveMode(PieceType.BLUE);
			boardPanel.closeHumanMoveMode(PieceType.RED);
			gameState.step(dice, move);
		}

		boardPanel.updateBoardPanel(gameState.getCurrentBoard());
		updateGameControlPanel();
		diceCreateButtonCallback();

		recordDisplayTextArea.setText(gameState.getRecord().toString());

		if(gameState.isEnd() == true) {
			state = State.stop;
		}
		move = null;
	}

	private void pushBackButtonCallback() {
		if(state == State.init) return;

		if(gameState.pushBack()==true) {
			state = State.move;
			boardPanel.updateBoardPanel(gameState.getCurrentBoard());
			updateGameControlPanel();
			recordDisplayTextArea.setText(gameState.getRecord().toString());
		}
	}

	private void saveRecordButtonCallback() {
		GameRecord record = gameState.getRecord();
		record.setCompetitionName(competitionNameTextField.getText());
		record.setPlace(competitionPlaceTextField.getText());

		String defaultFileName = record.getFileName();
		String defaultFilePath = "C:/Users/" + new String(System.getProperty("user.name").getBytes()) + "/Desktop/";

		JFileChooser fileChooser=new JFileChooser(new File(defaultFilePath));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(new File(defaultFilePath + defaultFileName + ".txt"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("txt","txt"));
		fileChooser.showSaveDialog(new JLabel());

		if(fileChooser.getSelectedFile()!=null){
			record.save(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	private void updateGameControlPanel() {
		if(gameState.isEnd()==false) {
			currentPlayerLabel.setText("行棋方：" + gameState.getCurrentPlayer());
		}
		else {
			currentPlayerLabel.setText("行棋方：");
		}
	}




	private Timer createTimer(final String name) {
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (name.equals("Red")) {
					redRemainingTime--;
					if (redRemainingTime < 0) {
						redTimer.stop();
						JOptionPane.showMessageDialog(GameFrame.this, "Red's time is up!");
					}
					redTimerLabel.setText(formatTime(redRemainingTime));
				} else if (name.equals("Blue")) {
					blueRemainingTime--;
					if (blueRemainingTime < 0) {
						blueTimer.stop();
						JOptionPane.showMessageDialog(GameFrame.this, "Blue's time is up!");
					}
					blueTimerLabel.setText(formatTime(blueRemainingTime));
				}
			}
		});
		return timer;
	}

	private String formatTime(Long seconds) {
		long minutes = seconds / 60;
		long remainingSeconds = seconds % 60;
		return String.format("%02d:%02d", minutes, remainingSeconds);
	}

}


