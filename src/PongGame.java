import java.awt.*;
import javax.swing.*;

public class PongGame extends JFrame
{
	public PongGame()
	{
		GameBoard board = new GameBoard();
		this.add(board);

		this.setIconImage(Toolkit.getDefaultToolkit().getImage(PongGame.class.getResource("Pong.png")));
		this.setTitle("Pong");
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 500);
		this.setVisible(true);
	}

	public static void main(String args[])
	{
		try
		{
			// make the UI look like Windows
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
		PongGame game = new PongGame();
	}
}
