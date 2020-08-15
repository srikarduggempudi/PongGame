import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.sound.sampled.*;
import javax.swing.*;

public class GameBoard extends JPanel implements ActionListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	private Ball ball;
	private Paddle player1Pad;
	private Paddle player2Pad;
	private Scoreboard scoreboard;
	private Font titleFont = new Font("Arial Black", Font.PLAIN, 40);
	private Font scoreFont = new Font("Arial Black", Font.PLAIN, 20);
	private boolean gameOver;
	private boolean isEasy;
	private JPanel optionPanel;

	public GameBoard()
	{
		// define colors used in the game
		Color boardBGColor = new Color(61, 61, 61);
		Color ballColor = new Color(1, 184, 170);
		Color paddleColor = new Color(253, 98, 94);
		Color scoreColor = new Color(242, 200, 17);

		setBackground(boardBGColor);

		// create the title screen that holds Easy or Hard options
		optionPanel = new JPanel();
		optionPanel.setBackground(boardBGColor);
		optionPanel.setLayout(null);

		// pong game label
		JLabel pongGameLbl = new JLabel("Pong Game");
		pongGameLbl.setForeground(scoreColor);
		pongGameLbl.setFont(titleFont);
		pongGameLbl.setBounds(275, 50, 300, 200);
		optionPanel.add(pongGameLbl);

		// easy button
		JButton easyBtn = new JButton("Easy");
		easyBtn.setBounds(300, 200, 100, 50);
		easyBtn.setForeground(ballColor);
		easyBtn.setFont(scoreFont);
		easyBtn.setActionCommand("EASY");
		easyBtn.addActionListener(this);
		optionPanel.add(easyBtn);

		// hard button
		JButton hardBtn = new JButton("Hard");
		hardBtn.setBounds(415, 200, 100, 50);
		hardBtn.setForeground(paddleColor);
		hardBtn.setFont(scoreFont);
		hardBtn.setActionCommand("HARD");
		hardBtn.addActionListener(this);
		optionPanel.add(hardBtn);

		// copyright label
		JLabel copyLbl = new JLabel("© Copyright 2017 Srikar Duggempudi");
		copyLbl.setForeground(scoreColor);
		copyLbl.setFont(scoreFont);
		copyLbl.setBounds(200, 375, 600, 100);
		optionPanel.add(copyLbl);

		this.setLayout(new BorderLayout());
		this.add(optionPanel, BorderLayout.CENTER);

		// default is easy
		isEasy = false;

		// create a new ball
		ball = new Ball();
		ball.setGameBoard(this);
		ball.setColor(ballColor);
		ball.reset();

		// create player 1 and player 2 paddles
		player1Pad = new Paddle();
		player1Pad.setGameBoard(this);
		player1Pad.setBounds(new Rectangle(15, 10, 20, 100));
		player1Pad.setColor(paddleColor);
		player2Pad = new Paddle();
		player2Pad.setGameBoard(this);
		player2Pad.setBounds(new Rectangle(760, 10, 20, 100));
		player2Pad.setColor(paddleColor);

		// create the score board
		scoreboard = new Scoreboard();
		scoreboard.setcolor(scoreColor);

		gameOver = false;

		// create a timer that fires every 5 ms that controls the gameloop
		Timer timer = new Timer(5, this);
		timer.start();
		timer.setActionCommand("GAMELOOP-TIMER");

		// add keyboard listener so that we can track keyboard events
		addKeyListener(this);

		setFocusable(true);

		// double buffering for smooth animation
		setDoubleBuffered(true);
	}

	public Ball getBall()
	{
		return ball;
	}

	public void setBall(Ball ball)
	{
		this.ball = ball;
	}

	public Paddle getPlayer1Pad()
	{
		return player1Pad;
	}

	public void setPlayer1Pad(Paddle player1Pad)
	{
		this.player1Pad = player1Pad;
	}

	public Paddle getPlayer2Pad()
	{
		return player2Pad;
	}

	public void setPlayer2Pad(Paddle player2Pad)
	{
		this.player2Pad = player2Pad;
	}

	public Scoreboard getScoreboard()
	{
		return scoreboard;
	}

	public void setScoreboard(Scoreboard scoreboard)
	{
		this.scoreboard = scoreboard;
	}

	/**
	 * 
	 */
	public void gameLoop()
	{
		// get into the method only if the game is not yet over
		if (gameOver == false)
		{
			// move the paddles if the appropriate key is pressed
			player1Pad.move();
			player2Pad.move();

			// detect ball collision with paddle or gameboard edges and move the
			// ball
			CollisionType collision = detectCollisionAndMoveBall();
			if (collision == CollisionType.LEFT)
			{
				// ball hit the left edge so player 2 gets a point
				scoreboard.incrementPlayer2();
				ball.reset();
			}
			else if (collision == CollisionType.RIGHT)
			{
				// ball hit the right edge so player 1 gets a point
				scoreboard.incrementPlayer1();
				ball.reset();
				ball.setXAccel(-ball.getXAccel());
			}

			// invalidate the gameboard panel and repaint it as the position of
			// the ball, panel etc. have changed
			this.invalidate();
			this.repaint();

			// see if either player1 or player2 might have won the game
			if (scoreboard.getPlayer1Score() == 10)
			{
				gameOver = true;
				playGameOverSound();
				JOptionPane.showMessageDialog(this, "Player 1 wins", "Game Over", JOptionPane.INFORMATION_MESSAGE);
				optionPanel.setVisible(true);
			}
			else if (scoreboard.getPlayer2Score() == 10)
			{
				gameOver = true;
				playGameOverSound();
				JOptionPane.showMessageDialog(this, "Player 2 wins", "Game Over", JOptionPane.INFORMATION_MESSAGE);
				optionPanel.setVisible(true);
			}
		}
	}

	/**
	 * Resets the board back to the beginning
	 */
	public void reset()
	{
		ball.setBallInMotion(false);
		gameOver = false;
		scoreboard.reset();
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);

		// use 2D graphics and antialiasing for better rendering of graphics
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Paint the ball
		if (ball.getBallInMotion())
		{
			g.setColor(ball.getColor());
			g.fillOval(ball.getBounds().x, ball.getBounds().y, ball.getBounds().width, ball.getBounds().height);
		}

		// Paint the left paddle
		g.setColor(player1Pad.getColor());
		g.fillRect(player1Pad.getBounds().x, player1Pad.getBounds().y, player1Pad.getBounds().width,
				player1Pad.getBounds().height);

		// Paint the right paddle
		g.setColor(player2Pad.getColor());
		g.fillRect(player2Pad.getBounds().x, player2Pad.getBounds().y, player2Pad.getBounds().width,
				player2Pad.getBounds().height);

		// Paint the score board
		g.setColor(scoreboard.getColor());
		String score = scoreboard.getPlayer1Score() + " - " + scoreboard.getPlayer2Score();

		// paint the scoreboard
		int scoreWidth = g.getFontMetrics().stringWidth(score);
		g.setFont(scoreFont);
		g.drawString(score, (getWidth() - scoreWidth) / 2, 30);

		// if the ball is waiting to be launched, show help text
		if (ball.getBallInMotion() == false)
		{
			String msg = "Press space to launch the ball";
			int msgWidth = g.getFontMetrics().stringWidth(msg);
			g.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() - 30);
		}
	}

	/**
	 * Detects when the ball makes contact with the paddle or the boundary
	 * 
	 * @return
	 */
	public CollisionType detectCollisionAndMoveBall()
	{
		// if the ball is not moving, nothing needs to be done
		if (ball.getBallInMotion() == false)
		{
			return CollisionType.NONE;
		}

		Rectangle bounds = ball.getBounds();
		int xAccel = ball.getXAccel();
		int yAccel = ball.getYAccel();
		int adjustFactor = 4;
		CollisionType collision = CollisionType.NONE;

		int newX = bounds.x + xAccel;
		int newY = bounds.y + yAccel;

		// If the ball is going beyond the top boundary, reverse the y direction
		if (newY < 0)
		{
			yAccel = -yAccel;
			// Sometimes the ball goes well into the ground, so we need to move
			// the ball more
			newY = adjustFactor;
			collision = CollisionType.TOP;
			playWallSound();
		}

		// If the ball is going beyond the bottom boundary, reverse the y
		// direction
		else if (newY + bounds.height > getHeight())
		{
			yAccel = -yAccel;
			// Sometimes the ball goes well into the ground, so we need to move
			// the ball more
			newY = (int) (getHeight() - bounds.height - adjustFactor);
			collision = CollisionType.BOTTOM;
			playWallSound();
		}

		// If the ball hits the left paddle, reverse the x direction
		else if (getPlayer1Pad().getBounds().intersects(bounds) && player1Pad.getBounds().x < bounds.x)
		{
			xAccel = -xAccel;
			// Sometimes the ball goes well into the paddle, so we need to move
			// the ball more
			newX = (int) (getPlayer1Pad().getBounds().x + getPlayer1Pad().getBounds().width + adjustFactor);
			collision = CollisionType.PLAYER1PADDLE;
			playHitSound();
		}

		// If the ball hits the right paddle, reverse the x direction
		else if (getPlayer2Pad().getBounds().intersects(bounds) && player2Pad.getBounds().x > bounds.x)
		{
			xAccel = -xAccel;
			// Sometimes the ball goes well into the paddle, so we need to move
			// the ball more
			newX = (int) (getPlayer2Pad().getBounds().x - bounds.width - adjustFactor);
			collision = CollisionType.PLAYER2PADDLE;
			playHitSound();
		}
		// The ball goes behind the player 1 Paddle
		else if (newX < 0)
		{
			collision = CollisionType.LEFT;
			playPlayer2ScoreSound();
		}
		// The ball goes behind the player 2 paddle
		else if (newX + bounds.getWidth() > getWidth())
		{
			collision = CollisionType.RIGHT;
			playPlayer1ScoreSound();
		}

		// apply new position and speed to the ball
		bounds.x = newX;
		bounds.y = newY;
		ball.setBounds(bounds);
		ball.setXAccel(xAccel);
		ball.setYAccel(yAccel);

		// if the game is in Hard mode, increase the speed if the ball hits
		// either of the paddles
		if (isEasy == false && (collision == CollisionType.PLAYER1PADDLE || collision == CollisionType.PLAYER2PADDLE))
		{
			ball.increaseSpeed();
		}

		return collision;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("GAMELOOP-TIMER"))
		{
			// timer ticked, run the gameloop once
			gameLoop();
		}
		else if (e.getActionCommand().equals("EASY"))
		{
			// Easy button clicked, set the game in easy mode
			isEasy = true;
			optionPanel.setVisible(false);
			this.reset();
		}
		else if (e.getActionCommand().equals("HARD"))
		{
			// Hard button clicked, set the game in hard mode
			isEasy = false;
			optionPanel.setVisible(false);
			this.reset();
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_W && player1Pad.getDirection() == 0)
		{
			player1Pad.setDirection(-1);
		}
		if (e.getKeyCode() == KeyEvent.VK_S && player1Pad.getDirection() == 0)
		{
			player1Pad.setDirection(1);
		}

		if (e.getKeyCode() == KeyEvent.VK_UP && player2Pad.getDirection() == 0)
		{
			player2Pad.setDirection(-1);
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN && player2Pad.getDirection() == 0)
		{
			player2Pad.setDirection(1);
		}

		this.invalidate();
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_W && player1Pad.getDirection() == -1)
		{
			player1Pad.setDirection(0);
		}
		else if (e.getKeyCode() == KeyEvent.VK_S && player1Pad.getDirection() == 1)
		{
			player1Pad.setDirection(0);
		}

		if (e.getKeyCode() == KeyEvent.VK_UP && player2Pad.getDirection() == -1)
		{
			player2Pad.setDirection(0);
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN && player2Pad.getDirection() == 1)
		{
			player2Pad.setDirection(0);
		}

		this.invalidate();
		this.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if (e.getKeyChar() == ' ')
		{
			ball.setBallInMotion(true);
		}
	}

	private void playHitSound()
	{
		playSound("/Hit.wav");
	}

	private void playWallSound()
	{
		playSound("/Wall.wav");
	}

	private void playPlayer1ScoreSound()
	{
		playSound("/Player1Score.wav");
	}

	private void playPlayer2ScoreSound()
	{
		playSound("/Player2Score.wav");
	}

	private void playGameOverSound()
	{
		playSound("/GameOver.wav");
	}

	private void playSound(String soundFile)
	{
		Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
		Mixer mixer = AudioSystem.getMixer(mixInfos[0]);
		DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
		try
		{
			Clip clip = (Clip) mixer.getLine(dataInfo);
			URL soundURL = GameBoard.class.getResource(soundFile);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
			clip.open(audioStream);
			clip.start();
		}
		catch (Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}
}
