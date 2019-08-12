package Interface;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import Logic.game;
import components.*;
import javax.swing.*;
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
		Button grid[][] = new Button[size][];
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
	public gui() {
		gui.icon = iconBuilder("./images/mine.jpg");
		gui.flagicon = iconBuilder("./images/flag.png");
		this.setResizable(false);
		this.setTitle("Minesweeper");
		this.setSize(windowSize, windowSize);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel buttonGrid = build();
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
