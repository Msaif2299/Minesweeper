package Interface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Hashtable;
import java.util.Random;

import Logic.game;
import components.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
public class gui extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1518185228791941515L;
	private static int size = 10;
	private static int mines = 20;
	private int windowSize = 400;
	private static ImageIcon icon, flagicon;
	private static int click[] = new int[2];
	private static boolean over = false;
	private static Button[][] grid;
	private ImageIcon iconBuilder(String path) {
		ImageIcon icon;
		ClassLoader cl= this.getClass().getClassLoader();
		URL imageURL   = cl.getResource(path);
		icon = new ImageIcon(imageURL);
		Image image = icon.getImage();
		Image newImage = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newImage);
		return icon;
	}
	
	public JPanel build() {
		JPanel buttonGrid = new JPanel(new GridLayout(size, size));
		game g = new game(gui.mines, gui.size);
		gui.grid = new Button[size][];
		for(int i = 0; i < size; i++) {
			grid[i] = new Button[size];
			for(int j = 0; j < size; j++) {
				boolean mine = game.state[i][j] == 'M' ? true : false;
				grid[i][j] = new Button(i, j, false, mine, g);
				buttonGrid.add(grid[i][j].btn);
				final int tempi = grid[i][j].row, tempj = grid[i][j].column;
				grid[i][j].btn.addItemListener(new ItemListener() {
			        public void itemStateChanged(ItemEvent ev) {
			            if (ev.getStateChange() == ItemEvent.SELECTED) {
			            	grid[tempi][tempj].btn.setEnabled(false);
			            	grid[tempi][tempj].btn.setIcon(null);
			            	click[0] = tempi;
			            	click[1] = tempj;
			            	game.state = g.updateBoard(game.state, click);
			            	setBoard(game.state, grid);
			            	if(grid[tempi][tempj].mine) {
				            	grid[tempi][tempj].btn.setIcon(gui.icon);
				            	grid[tempi][tempj].btn.setText("");
			            	}
			            	if(game.state[tempi][tempj] == 'X') {
			            		synchronized ((Object)gui.over){
				            		gui.over = true;
				            		JOptionPane.showMessageDialog((JFrame)SwingUtilities.getRoot(buttonGrid), "Game over!");
				            		showBoard(game.state, grid);
				            		JOptionPane.showMessageDialog((JFrame)SwingUtilities.getRoot(buttonGrid),  "Starting new game");
				            		game.resetFlags();
				            		game.state = g.createGame(gui.mines, gui.size);
				            		reset(grid);
				            		setMines(game.state, grid);
				            		setBoard(game.state, grid);
			            		}
			            	}
			            }
			        }
			    });
				grid[i][j].btn.addMouseListener(new MouseListener() {
					boolean flagged = false;
					@Override
					public void mousePressed(MouseEvent e) {
						if(grid[tempi][tempj].btn.isEnabled() && !gui.over) {
							grid[tempi][tempj].btn.getModel().setArmed(true);
							grid[tempi][tempj].btn.getModel().setPressed(true);
							flagged = !flagged;
						}
						game.flaggedMines[tempi][tempj] = false;
						grid[tempi][tempj].btn.setIcon(null);
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						if(grid[tempi][tempj].btn.isEnabled() && !gui.over) {
							grid[tempi][tempj].btn.getModel().setArmed(false);
							grid[tempi][tempj].btn.getModel().setPressed(false);
							if(flagged) {
								grid[tempi][tempj].btn.setIcon(gui.flagicon);
								game.flaggedMines[tempi][tempj] = true;
							}
							else {
								grid[tempi][tempj].btn.setIcon(null);
								game.flaggedMines[tempi][tempj] = false;
							}
						}
						if(gui.over == true)
							gui.over = false;
						if(game.win()) {
							synchronized((Object)gui.over) {
								gui.over = true;
								JOptionPane.showMessageDialog((JFrame)SwingUtilities.getRoot(buttonGrid), "You Win!");
			            		showBoard(game.state, grid);
			            		JOptionPane.showMessageDialog((JFrame)SwingUtilities.getRoot(buttonGrid),  "Starting new game");
			            		game.resetFlags();
			            		game.state = g.createGame(gui.mines, gui.size);
			            		reset(grid);
			            		setMines(game.state, grid);
			            		setBoard(game.state, grid);
							}
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}



					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
				if(grid[i][j].mine)
					grid[i][j].btn.setDisabledIcon(icon);
			}
		}
		return buttonGrid;
	}
	
	private static void newGame(JPanel buttonGrid, int low, int high, String mode, boolean notRandom) {
		Random random = new Random();
		synchronized((Object)gui.over) {
			gui.over = true;
    		showBoard(game.state, gui.grid);
    		JOptionPane.showMessageDialog((JFrame)SwingUtilities.getRoot(buttonGrid),  "Starting new game: "+ mode +" Mode");
    		game.resetFlags();
    		if(notRandom)
    			gui.mines = low;
    		else
    			gui.mines = low + random.nextInt(high);
    		game.state = game.createGame(gui.mines, gui.size);
    		synchronized((Object)gui.grid) {
        		reset(gui.grid);
        		setMines(game.state, gui.grid);
        		setBoard(game.state, gui.grid);
    		}
		}
	}
	
	public JPanel customPanel(JSlider js) {
		js.setValue(10);
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(js, BorderLayout.CENTER);
		JTextField watch = new JTextField("10");
		JPanel labelpanel = new JPanel();
		labelpanel.add(watch);
		watch.setHorizontalAlignment(JLabel.CENTER);
		watch.setEditable(true);
		jp.add(labelpanel, BorderLayout.SOUTH);
		watch.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		watch.setPreferredSize(new Dimension(30, 20));
		js.addChangeListener(e -> {
			watch.setText(Integer.toString(js.getValue()));
		});
		watch.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
                String typed = watch.getText();
                js.setValue(0);
                if(!typed.matches("\\d+") || typed.length() > 3) {
                    return;
                }
                int value = Integer.parseInt(typed);
                if(value < 1 || value > 100)
                	return;
                js.setValue(value);
            }
		});
		return jp;
	}
	
	public JMenuBar menu(JPanel buttonGrid) {
		JMenuBar menubar = new JMenuBar();
		JMenu newgame = new JMenu("New Game");
		JMenuItem easy = new JMenuItem("Easy");
		JMenuItem normal = new JMenuItem("Normal");
		JMenuItem hard = new JMenuItem("Hard");
		JMenuItem custom = new JMenuItem("Custom");
		
		newgame.add(easy);
		newgame.add(normal);
		newgame.add(hard);
		newgame.add(custom);
		menubar.add(newgame);
		
		easy.addActionListener(e -> newGame(buttonGrid, 7, 5, "Easy", false));
		normal.addActionListener(e -> newGame(buttonGrid, 13, 17, "Normal", false));
		hard.addActionListener(e -> newGame(buttonGrid, 31, 20, "Hard", false));
		
		JSlider js = new JSlider(1, 100, 10);
		js.setMajorTickSpacing(25);
		js.setMinorTickSpacing(10);
		js.setPaintTicks(true);
		Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
		position.put(1, new JLabel("1"));
		position.put(25, new JLabel("25"));
		position.put(50,  new JLabel("50"));
		position.put(75, new JLabel("75"));
		position.put(100,  new JLabel("100"));
		js.setLabelTable(position);
		js.setPaintLabels(true);
		
		custom.addActionListener(e -> {
			int option = JOptionPane.showConfirmDialog(null, customPanel(js), "Choose the number of mines!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(option == JOptionPane.YES_OPTION) {
				gui.mines = js.getValue();
				newGame(buttonGrid, gui.mines, 0, "Custom (" + Integer.toString(gui.mines) + " mines)", true);
			}
		});
		return menubar;
	}
	
	public gui() {
		gui.icon = iconBuilder("./images/mine.jpg");
		gui.flagicon = iconBuilder("./images/flag.png");
		this.setResizable(false);
		this.setTitle("Minesweeper");
		this.setSize(windowSize, windowSize);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel buttonGrid = build();
		this.setJMenuBar(menu(buttonGrid));
		this.add(buttonGrid);
		this.setVisible(true);
	}
	
	public static void reset(Button[][] grid) {
		for(Button[] row: grid) {
			for(Button button: row)
				button.reset();
		}
	}
	
	private static void setMines(char[][] board, Button[][] grid) {
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				if(board[i][j] == 'M') {
					grid[i][j].btn.setDisabledIcon(icon);
					grid[i][j].mine = true;
				}
	}
	
	public static void setBoard(char[][] board, Button[][] grid) {
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++) {
				if(board[i][j] == 'E')
					grid[i][j].setText("");
				else if(board[i][j] == 'B') {
					grid[i][j].setVal(' ');
					grid[i][j].btn.setEnabled(false);
				}
				else if(board[i][j] != 'M' && board[i][j] != 'X') {
					grid[i][j].setVal(board[i][j]);
					grid[i][j].btn.setEnabled(false);
				}
			}
	}
	
	public static void showBoard(char[][] board, Button[][] grid) {
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++) {
				if(board[i][j] == 'E' || board[i][j] == 'B') {
					grid[i][j].setVal(' ');
					grid[i][j].btn.setEnabled(false);
				}
				else if(board[i][j] == 'M' || board[i][j] == 'X') {
					grid[i][j].btn.setEnabled(false);
					grid[i][j].btn.setIcon(icon);
	            	grid[i][j].btn.setText("");
				}
				else {
					grid[i][j].setVal(board[i][j]);
					grid[i][j].btn.setEnabled(false);
				}
			}
	}
	
	
}
