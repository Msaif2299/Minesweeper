package Logic;
import java.util.ArrayList;
import java.util.Random;

import javafx.util.Pair;

public class game {
	public static char[][] state;
	public static boolean[][] flaggedMines;
	public game(int mines, int dim) {
		game.state = this.createGame(mines, dim);
	}
	ArrayList<Pair<Integer, Integer>> neighbors(char[][] board, int x, int y, boolean[][] visited){
        int posx[] = {x, x-1, x+1};
        int posy[] = {y, y-1, y+1};
        ArrayList<Pair<Integer, Integer>> v = new ArrayList<Pair<Integer, Integer>>();
        int m = board.length;
        int n = board[0].length;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if(posx[i]==x && posy[j]==y)
                    continue;
                else{
                    if(posx[i] >= 0 && posy[j] >= 0 && posx[i] < m && posy[j] < n && !visited[posx[i]][posy[j]])
                        v.add(new Pair<Integer, Integer>(posx[i], posy[j]));
                }
        return v;
    }
    void dfs(char[][] board, int x, int y, boolean visited[][]){
        if(board[x][y] == 'E' && !visited[x][y]){
            int count = 0;
            visited[x][y] = true;
            ArrayList<Pair<Integer, Integer>> n = neighbors(board, x, y, visited);
            for(Pair<Integer, Integer> p: n){
                if(board[p.getKey()][p.getValue()] == 'M')
                    count += 1;
            }
            if(count == 0){
                board[x][y] = 'B';
                for(Pair<Integer, Integer> p: n)
                    dfs(board, p.getKey(), p.getValue(), visited);
            }
            else
                board[x][y] = (char)(count + '0');
        }
    }
    public char[][] updateBoard(char[][] board, int[] click) {
        if(board[click[0]][click[1]] == 'M'){
            board[click[0]][click[1]] = 'X';
            return board;
        }
        boolean visited[][] = new boolean[board.length][];
        for(int i = 0; i < board.length; i++){
            visited[i] = new boolean[board[0].length];
            for(int j = 0; j < board[0].length; j++)
                visited[i][j] = false;
        }
        if(board[click[0]][click[1]] == 'E')
            dfs(board, click[0], click[1], visited);
        return board;
    }
    public char[][] createGame(int mines, int dim){
    	char[][] board = new char[dim][];
    	game.flaggedMines = new boolean[dim][];
    	for(int i = 0; i < dim; i++) {
    		board[i] = new char[dim];
    		game.flaggedMines[i] = new boolean[dim];
    		for(int j = 0; j < dim; j++) {
    			board[i][j] = 'E';
    			game.flaggedMines[i][j] = false;
    		}
    	}
    	int occupiedSpots = 0;
    	Random random = new Random();
    	while(occupiedSpots < mines) {
    		int x = random.nextInt(dim);
    		int y = random.nextInt(dim);
    		if(board[x][y] == 'E') {
    			board[x][y] = 'M';
    			occupiedSpots++;
    		}
    	}
    	return board;
    }
    
    public static void resetFlags() {
    	for(int i = 0; i < game.flaggedMines.length; i++)
    		for(int j = 0; j < game.flaggedMines.length; j++)
    			game.flaggedMines[i][j] = false;
    }
    
    public static boolean win() {
    	int n = game.flaggedMines.length;
    	for(int i = 0; i < n; i++)
    		for(int j = 0; j < n; j++) {
    			if(game.flaggedMines[i][j]) {
    				if(game.state[i][j] != 'M' && game.state[i][j] != 'X')
    					return false;
    			}
    			else {
    				if(game.state[i][j] == 'M' || game.state[i][j] == 'X')
    					return false;
    			}
    			if(game.state[i][j] == 'E')
    				return false;
    		}
    	return true;
    }
}
