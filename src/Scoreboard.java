import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Scoreboard
{
	private int player1Score;
	private int player2Score;
	private GameBoard gameBoard;
	private Color color;

	public Scoreboard()
	{
		player1Score = 0;
		player2Score = 0;
		color = Color.YELLOW;
	}

	public int getPlayer1Score()
	{
		return player1Score;
	}

	public void setPlayer1Score(int player1Score)
	{
		this.player1Score = player1Score;
	}

	public int getPlayer2Score()
	{
		return player2Score;
	}

	public void setPlayer2Score(int player2Score)
	{
		this.player2Score = player2Score;
	}

	public GameBoard getGameBoard()
	{
		return gameBoard;
	}

	public void setGameBoard(GameBoard gameBoard)
	{
		this.gameBoard = gameBoard;
	}

	public Color getColor()
	{
		return color;
	}

	public void setcolor(Color color)
	{
		this.color = color;
	}

	/**
	 * Increment player 1's score when the ball hits the right boundary
	 */
	public void incrementPlayer1()
	{
		player1Score++;
	}

	/**
	 * Increment player 2's score when the ball hits the left boundary
	 */
	public void incrementPlayer2()
	{
		player2Score++;
	}

	/**
	 * Resets the scoreboard back to 0-0
	 */
	public void reset()
	{
		setPlayer1Score(0);
		setPlayer2Score(0);
	}
}
