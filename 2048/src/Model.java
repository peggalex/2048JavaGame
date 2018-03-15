
import java.awt.Color;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;


public class Model extends Observable{
	private Cell[][] table;
	private static int highScore = 0;
	private int score;
	private Color[] colorScheme;
	private boolean gameIsWon;
	private boolean winPrompted;
	
	public Model() {
		newGame();
	}
	
	public Cell[][] getTable() { return this.table; }
		
	public void newGame() {
		this.gameIsWon = false;
		this.winPrompted = false;
		this.score = 0;
		Random rand = new Random();
		colorScheme = new Color[10];
		for (int i = 0; i<10; i++) {
			int r = rand.nextInt(128)+128;
			int g = rand.nextInt(128)+128;
			int b = rand.nextInt(128)+128;
			colorScheme[i] = new Color(r,g,b);
		}
		this.table = new Cell[4][];
		for (int x=0; x<4; x++){
			this.table[x] = new Cell[4];
		}
		generateNewPoint(); generateNewPoint();
		setChanged();
		notifyObservers("new game");
	}

	
	private boolean removeWhiteSpace (Cell[] vector, boolean test) {
		Cell c;
		boolean changed = false;
		for (int i=vector.length-1; i>-1; i--) {
			if (vector[i]==null) {
				for (int j = i; j>-1; j--) {
					c = vector[j];
					if (c!=null) {
						if (!(test)) {
							vector[j]=null; vector[i]=c;
						}
						changed=true;
						break;
					}
				}
			}
		}
		return changed;
	}
	
	private boolean mergeCells (Cell [] vector, boolean test) {
		boolean changed = removeWhiteSpace(vector, test);
		Cell c1,c2;
		for (int i=vector.length-1; i>0; i--) {
			c1 = vector[i];
			c2 = vector[i-1];
			if (c1!=null && 
					c2!=null && 
					c1.getValue()==
					c2.getValue()) {
				if (!(test)) {
					vector[i-1]=null;
					c1.expand();
					handleScore(c1.getValue());
					removeWhiteSpace(vector, false);
				}
				return true;
			}
		}
		return changed;
	}

	private void reverseCellArray(Cell[][] cellArray) {
		for (int j=0; j<cellArray.length; j++) {
			Cell[] reversedArray = new Cell[cellArray[j].length];
			for (int i=0; i<cellArray[j].length; i++) {
				reversedArray[cellArray[j].length-1-i] = cellArray[j][i];
			}
			for (int i=0; i<cellArray[j].length; i++) {
				cellArray[j][i] = reversedArray[i];
			}
		}
	}
	
	public void shift(String direction) {
		boolean moved = false;
		if (direction.equals("up")) {
			moved = moved | shiftUp(false);
				
		} else if (direction.equals("right")) {
			moved = moved | shiftRight(false);
			
		} else if (direction.equals("down")) {
			moved = moved | shiftDown(false);

		} else {
			assert(direction.equals("left"));
			moved = moved | shiftLeft(false);
		}
		if (moved) { generateNewPoint(); }
		this.setChanged();
		this.notifyObservers(direction);
	}
	
	private boolean shiftUp(boolean test) { 
		boolean moved = false;
		Cell[][] columns = new Cell[4][];
		for (int x=0; x<4; x++) {
			columns[x] = new Cell[4];
			for (int y=0; y<4; y++) {
				columns[x][y] = this.table[y][x];
			}
		}
		reverseCellArray(columns);
		for (Cell[] vector : columns) {
			moved = moved | mergeCells(vector, test);
		}
		reverseCellArray(columns);
		for (int x=0; x<4; x++) {
			for (int y=0; y<4; y++) {
				this.table[y][x] = columns[x][y];
			}
		}
		return moved;
	}
	
	private boolean shiftRight(boolean test) {
		boolean moved = false; 
		for (Cell[] vector : this.table) {
			moved = moved | mergeCells(vector, test);
		}
		return moved;
	}
	
	private boolean shiftDown(boolean test) {
		boolean moved = false;
		Cell[][] columns = new Cell[4][];
		for (int x=0; x<4; x++) {
			columns[x] = new Cell[4];
			for (int y=0; y<4; y++) {
				columns[x][y] = this.table[y][x];
			}
		}
		for (Cell[] vector : columns) {
			moved = moved | mergeCells(vector, test);
		}
		for (int x=0; x<4; x++) {
			for (int y=0; y<4; y++) {
				this.table[y][x] = columns[x][y];
			}
		}
		return moved;
	}
	
	private boolean shiftLeft(boolean test) {
		boolean moved = false;
		reverseCellArray(this.table);
		for (Cell[] vector : this.table) {
			moved = moved | mergeCells(vector, test);
		}
		reverseCellArray(this.table);
		return moved;
	}
	private void generateNewPoint() {
		Random rand = new Random();
		int isFour = rand.nextInt(4)+1; //P(x=4) = 1/4
		int value = (isFour==4) ? 4 : 2;
		int x = rand.nextInt(4);
		int y = rand.nextInt(4);
		while (this.table[y][x] != null) {
			x = rand.nextInt(4);
			y = rand.nextInt(4);
		}
		this.table[y][x] = new Cell().value(value).model(this);
	}
	
	
	public Color getColor(int i) {
		return colorScheme[i];
	}
	
	public boolean isGameOver() {
		return !(shiftUp(true) | shiftDown(true) |
				shiftLeft(true) | shiftRight(true));
	}
	
	private void handleScore(int points) {
		this.score+=points;
		if (points==2048) { this.gameIsWon = true; }
		highScore = Math.max(highScore, this.score);
	}
	
	public void setWinPrompted() { this.winPrompted = true; }
	
	public int getScore() { return this.score; }
	
	public boolean getIsHighScore() { 
		return highScore==this.score && highScore!=0;
	}
	
	public boolean showWinPrompt() { return this.gameIsWon & !this.winPrompted; }
	
	public static int getHighScore() { return highScore; }

}