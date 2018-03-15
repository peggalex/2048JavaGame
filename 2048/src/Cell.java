import java.awt.Color;

public class Cell {
	private Model m;
	private int value;
	private Color color;

	public Cell() {
		this.value = 2;
		this.color = Color.white;
	}
	
	public Cell value(int value) {
		this.value = value;
		return this;
	}
	
	public Cell model(Model m) {
		this.m = m;
		setColor();
		return this;
	}
	private void setColor() {
		int index = (int)(Math.log(value)/Math.log(2));
		while (index-10>=0) {
			index-=10;
		}
		this.color = this.m.getColor(index);
	}
	
	public int getValue() { return this.value; }
	
	public Color getColor() { return this.color; }
	
	public int expand() {
		this.value=this.value*2;
		setColor();
		return this.value;
	}
	
	@Override
	public boolean equals(Object o) {
		return o.getClass() == Cell.class && ((Cell)o).getValue()==this.getValue();
	}
}
