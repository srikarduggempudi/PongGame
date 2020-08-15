import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Paddle
{
	private int direction;
	private int speed;
	private Color color;
	private GameBoard gameBoard;
	private Rectangle bounds;

	public Paddle()
	{
		this.direction = 0;
		this.speed = 4;
		this.color = Color.ORANGE;
	}

	public int getDirection()
	{
		return direction;
	}

	public void setDirection(int direction)
	{
		synchronized (this)
		{
			this.direction = direction;
		}
	}

	public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public Rectangle getBounds()
	{
		return bounds;
	}

	public void setBounds(Rectangle bounds)
	{
		this.bounds = bounds;
	}

	public GameBoard getGameBoard()
	{
		return gameBoard;
	}

	public void setGameBoard(GameBoard gameBoard)
	{
		this.gameBoard = gameBoard;
	}

	public void move()
	{
		if (direction == -1)
		{
			moveUp();
		}
		else if (direction == 1)
		{
			moveDown();
		}
	}

	/**
	 * Moves the position up by decreasing the Y value by speed. It can only
	 * decrement till the top of the paddle hits the top boundary
	 */
	private void moveUp()
	{
		bounds.y -= speed;
		if (bounds.y < 0)
		{
			bounds.y = 0;
		}
	}

	/**
	 * Moves the position down by increasing the y value by speed. It can only
	 * increment till the bottom of the paddle hits the bottom boundary
	 */
	private void moveDown()
	{
		bounds.y += speed;
		if (bounds.y > gameBoard.getSize().getHeight() - bounds.height)
		{
			bounds.y = (int) (gameBoard.getSize().getHeight() - bounds.height);
		}
	}
}
