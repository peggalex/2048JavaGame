import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


@SuppressWarnings("serial")
public class View extends JFrame implements Observer{
	private Model m;
	private JLabel[][] jLabels;
	private JLabel score;
	private JButton newGameButton;
	private JLabel highScore;
	private JPanel tablePanel;
	private JLabel arrow;
	private HashMap<String, ImageIcon> stringToArrow;
	private ImageIcon[] arrowIcons = 
		{new ImageIcon(View.class.getResource("arrowUp.png")),
		new ImageIcon(View.class.getResource("arrowRight.png")),
		new ImageIcon(View.class.getResource("arrowDown.png")),
		new ImageIcon(View.class.getResource("arrowLeft.png")) };
	
	public View(Model m) {
		super("2048"); // set the title and do other JFrame init
		stringToArrow = new HashMap<>();
		String[] directions = {"up","left","down","right"};
		for (int i=0; i<4; i++) {
			ImageIcon iI = new ImageIcon(arrowIcons[i].getImage().getScaledInstance
					(100,100,java.awt.Image.SCALE_SMOOTH));
			stringToArrow.put(directions[i], iI);
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c=this.getContentPane();
		c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
		this.m = m;
		JPanel menu = makeMenu();
		JPanel table = makeTable();
		c.add(menu);
		c.add(table);
		this.pack();
		this.setSize(2000, 1400);
		JOptionPane.showMessageDialog(this, "Get 2048 to win");
		this.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		Model model = (Model)o;
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				Cell c = model.getTable()[j][i];
				if (c!=null) {
					this.jLabels[j][i].setText(c.getValue()+"");
					this.jLabels[j][i].setBackground(c.getColor());
				} else {
					this.jLabels[j][i].setText("");
					this.jLabels[j][i].setBackground(new JLabel().getBackground());
				}
			}
		}
		handleScores(m);
		if (m.isGameOver()) {
			this.arrow.setIcon(null);
			JOptionPane.showMessageDialog(this, "Game over");
		} else if (m.showWinPrompt()) {
			JOptionPane.showMessageDialog(this, "Congrats, you win");
			m.setWinPrompted();
		} else if (arg != null){
			this.arrow.setIcon(stringToArrow.get((String)arg));
		} else {
			this.arrow.setIcon(null);
		}
		this.repaint();
	}
	
	public void handleScores(Model m) {
		this.score.setText(m.getScore()+"");
		Color c = (m.getIsHighScore()) ? Color.green : Color.black;
		this.score.setForeground(c);
		this.highScore.setText(Model.getHighScore()+"");
		this.highScore.setForeground(c);
	}
	
	public JPanel makeTable() {
		this.tablePanel = new JPanel(new GridLayout(4,4));
		this.jLabels = new JLabel[4][];
		for (int x=0; x<4; x++) {
			this.jLabels[x] = new JLabel[4];
		}
		Cell[][] t = this.m.getTable();
		Font f = new Font(new JLabel().getFont().getName(), Font.PLAIN, 200);
		Border b = BorderFactory.createLineBorder(new JPanel().getBackground(),20);

		for (int y=0; y<4; y++) {
			for (int x=0; x<4; x++) {
				String s = (t[y][x]==null) ? "" : t[y][x].getValue()+"";
				this.jLabels[y][x] = new JLabel(s, SwingConstants.CENTER);
				this.jLabels[y][x].setBorder(b);
				if (t[y][x]!=null) { 
					this.jLabels[y][x].setBackground(t[y][x].getColor());
				}
				this.jLabels[y][x].setFont(f);
				this.jLabels[y][x].setOpaque(true);
				this.tablePanel.add(this.jLabels[y][x]);
			}
		}
		return this.tablePanel;
	}
	
	public JPanel makeMenu() {
		JPanel menu = new JPanel();
		Font f = new Font(new JLabel().getFont().getName(), Font.PLAIN, 60);
		this.newGameButton = new JButton("New Game");
		this.newGameButton.setFocusable(false);
		this.newGameButton.setPreferredSize(new Dimension(400,100));
		this.newGameButton.setFont(f);
		menu.add(this.newGameButton);
		this.arrow = new JLabel();
		this.arrow.setPreferredSize(new Dimension(100,100));
		this.arrow.setOpaque(true);
		this.arrow.setBackground(Color.white);
		menu.add(this.arrow);
		JPanel jp2 = new JPanel(new GridLayout(2,2));
		JLabel scoreTxt = new JLabel("Score: ", SwingConstants.CENTER);
		JLabel highScoreTxt = new JLabel("High score: ", SwingConstants.CENTER);
		Font f2 = new Font(new JLabel().getFont().getName(), Font.PLAIN, 30);
		this.score = new JLabel("0", SwingConstants.RIGHT);
		this.score.setFont(f2);
		this.highScore = new JLabel("0", SwingConstants.RIGHT);
		this.highScore.setFont(f2);
		scoreTxt.setFont(f2);
		highScoreTxt.setFont(f2);
		jp2.add(scoreTxt);
		jp2.add(this.score);
		jp2.add(highScoreTxt);
		jp2.add(this.highScore);
		jp2.setBackground(Color.white);
		menu.add(Box.createRigidArea(new Dimension(1000,1)));
		menu.add(jp2);
		menu.setBackground(Color.white);
		return menu;
	}

	public void installController(Controller c) {
		this.addKeyListener(c);
		this.newGameButton.addActionListener(c);
	}

}
