package pacman.controllers.examples;

import java.util.Random;
import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.controllers.Controller;

/*
 * The Class RandomPacMan.
 */
public final class RandomPacMan extends Controller<MOVE>
{
	private Random rnd=new Random();
	private MOVE[] allMoves=MOVE.values();
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game,long timeDue)
	{
		int currentNode = game.getPacmanCurrentNodeIndex();
	    System.out.println("CurrentNode " +currentNode);
	    System.out.println("Made Move");
		return allMoves[rnd.nextInt(allMoves.length)];
	}
}