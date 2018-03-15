import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener, ActionListener{
	Model m;
	
	public Controller(Model m) {
		this.m = m;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			this.m.shift("up");
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.m.shift("left");
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.m.shift("down");
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.m.shift("right");
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		m.newGame();
	}

}
