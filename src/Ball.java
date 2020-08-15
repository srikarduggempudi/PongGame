import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Ball
{
	private int xAccel;
	private int yAccel;
	private Color color;
	private boolean ballInMotion;
	private GameBoard gameBoard;
	private Rectangle bounds;

	public Ball()
	{
		xAccel = 2;
		yAccel = 2;
		ballInMotion = false;
		color = color.BLUE;
		bounds = new Rectangle(100, 100, 25, 25);
	}

	public int getXAccel()
	{
		return xAccel;
	}

	public void setXAccel(int xAccel)
	{
		this.xAccel = xAccel;
	}

	public int getYAccel()
	{
		return yAccel;
	}

	public void setYAccel(int yAccel)
	{
		this.yAccel = yAccel;
	}

	public Rectangle getBounds()
	{
		return bounds;
	}

	public void setBounds(Rectangle bounds)
	{
		this.bounds = bounds;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public boolean getBallInMotion()
	{
		return ballInMotion;
	}

	public void setBallInMotion(boolean ballInMotion)
	{
		this.ballInMotion = ballInMotion;
	}

	public GameBoard getGameBoard()
	{
		return gameBoard;
	}

	public void setGameBoard(GameBoard gameBoard)
	{
		this.gameBoard = gameBoard;
	}

	public void increaseSpeed()
	{
		// Increase x speed upto 4 or -4. If it too low/high, game is not
		// playable
		if (xAccel > -4 && xAccel < 4)
		{
			if (xAccel < 0)
			{
				xAccel += -1;
			}
			else
			{
				xAccel++;
			}
		}

		// Increase y speed upto 3 or -3. If it too low/high, game is not
		// playable
		if (yAccel > -3 && yAccel < 3)
		{
			if (yAccel < 0)
			{
				yAccel += -1;
			}
			else
			{
				yAccel++;
			}
		}
	}

	/**
	 * Moves the position of the ball back to the center.
	 */
	public void reset()
	{
		xAccel = 2;
		yAccel = 2;
		if (Math.random() * 2 <= 1)
		{
			yAccel = -2;
		}
		ballInMotion = false;
		getBounds().x = 400;
		getBounds().y = 200 + (int) (100 * Math.random());
	}
}
