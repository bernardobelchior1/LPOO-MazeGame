package maze.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import maze.logic.Maze;
import maze.logic.Game.Direction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GraphicalGameWindow implements WindowListener {
	private JFrame graphicalGameFrame;
	private JLabel instructionsLabel;
	private MazeDisplayPanel mazeDisplayPanel;
	private JScrollPane mazeDisplayScrollPane;
	private GameStateDisplayPanel gameStatePanel = new GameStateDisplayPanel();

	private Maze maze;
	private JFrame parent;

	/**
	 * Create the application.
	 */
	public GraphicalGameWindow(JFrame parent, Maze maze) {
		this.maze = maze;
		this.parent = parent;
		initialize();
		adjustWindowToScreen();
		
		gameStatePanel.updateState(maze.getGameState());
		mazeDisplayPanel.requestFocusInWindow();
		graphicalGameFrame.addWindowListener(this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		graphicalGameFrame = new JFrame();
		graphicalGameFrame.setTitle("Play Window");
		graphicalGameFrame.setBounds(100, 100, 622, 482);
		graphicalGameFrame.setLayout(new BorderLayout());
		graphicalGameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		graphicalGameFrame.getContentPane().setLayout(new BorderLayout());	
		graphicalGameFrame.setEnabled(true);
		graphicalGameFrame.setResizable(false);
		graphicalGameFrame.setVisible(true);
			
		mazeDisplayPanel = new MazeDisplayPanel(this);
		mazeDisplayPanel.setPreferredSize(new Dimension (maze.getMazeDimension()*MazeGraphics.TEXTURE_SIZE, maze.getMazeDimension()*MazeGraphics.TEXTURE_SIZE));

		mazeDisplayScrollPane = new JScrollPane(mazeDisplayPanel);
		mazeDisplayScrollPane.setBounds(0, 0, 300, 300);
		graphicalGameFrame.getContentPane().add(mazeDisplayScrollPane, BorderLayout.CENTER);

		instructionsLabel = new JLabel("");
		instructionsLabel.setSize(10, 16);
		instructionsLabel.setText("Ready to play!");
		graphicalGameFrame.getContentPane().add(instructionsLabel, BorderLayout.PAGE_END);		
	}

	public void nextTurn(Direction direction){
		maze.nextTurn(direction);
		mazeDisplayPanel.repaint();

		gameStatePanel.updateState(maze.getGameState());

		switch (maze.getGameState()) {
		case RUNNING:
			instructionsLabel.setText("You can move! Use WASD or the Arrow keys to do it.");
			break;
		case DRAGON_WIN:
			instructionsLabel.setText("Game over! Sorry, you died to a dragon..");
			break;
		case HERO_WIN:
			instructionsLabel.setText("Congratulations! You won!!");
			break;
		}
	}

	public Maze getMaze() {
		return maze;
	}

	//Closes the window.
	public void close() {
		graphicalGameFrame.setVisible(false);
		graphicalGameFrame.dispatchEvent(new WindowEvent(graphicalGameFrame, WindowEvent.WINDOW_CLOSING));
	}
	
	private void adjustWindowToScreen() {
		int width = Math.min(
				graphicalGameFrame.getInsets().left + graphicalGameFrame.getInsets().right + mazeDisplayScrollPane.getPreferredSize().width + mazeDisplayScrollPane.getVerticalScrollBar().getSize().width,
				GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width);
		int height = Math.min(
				graphicalGameFrame.getInsets().top + graphicalGameFrame.getInsets().bottom + mazeDisplayScrollPane.getPreferredSize().height + mazeDisplayScrollPane.getHorizontalScrollBar().getSize().height + instructionsLabel.getSize().height,
				GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
		graphicalGameFrame.setSize(width, height);
		MazeGraphics.centerFrame(graphicalGameFrame);
		graphicalGameFrame.setLocation(graphicalGameFrame.getLocation().x, 0);
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		parent.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}
	
	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
