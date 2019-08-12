package components;
import Logic.game;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
public class Button extends JToggleButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8595397720548214856L;
	public int row, column;
	public boolean mine;
	public game g;
	public JToggleButton btn;
	
	
	
	public Button(int row, int column, boolean set, boolean mine, game g) {
		this.row = row;
		btn = new JToggleButton("");
		this.column = column;
		this.mine = mine;
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setForeground(Color.RED);
		btn.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public void setVal(char val) {
		btn.setSelected(false);
		btn.setEnabled(false);
		if(val != ' ') {
			btn.setText(Character.toString(val));
		}
		else {
			btn.setText("");
		}
	}
	
	public void reset() {
		btn.setText("");
		btn.setSelected(false);
		btn.setIcon(null);
		btn.setDisabledIcon(null);
		btn.setEnabled(true);
		this.mine = false;
	}
	
}
