
public class Twenty48App {
	
		public static void main(String[] args) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new Twenty48App();
				}
			});
		}
		
		public Twenty48App() {
			Model m = new Model();
			View v = new View(m);
			m.addObserver(v);
			Controller c = new Controller(m);
			v.installController(c);
		}
}
