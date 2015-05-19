package pacman.controllers.me.montecarlo;

import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DilerMonteCarloPacMan extends Controller<MOVE> {

	private Random rnd = new Random();

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int currentNode = game.getPacmanCurrentNodeIndex();

		if (game.isJunction(currentNode) || lastMove == null) {

			lastMove = actRandomly(game, currentNode);
			return lastMove;
		} // else move towards the junction which is not former junction
		else {

			MOVE[] possibleMoves = game.getPossibleMoves(currentNode, lastMove);
			assert possibleMoves.length == 1 : "More than One neigbor";

			// possibleMoves returns null when pacman has died
			if (possibleMoves != null) {
				lastMove = possibleMoves[0];
			} else {
				//System.out.println("NULL, node " + currentNode);
			}

			return lastMove;

		}

	}

	private MOVE actRandomly(Game game, int currentNode) {

		MOVE allPossibleMoves[] = null;
		if (lastMove == null) {
			allPossibleMoves = game.getPossibleMoves(currentNode);
		} else {
			allPossibleMoves = game.getPossibleMoves(currentNode, lastMove);
		}

		MOVE move = null;
		// possibleMoves returns null when pacman has died
		if (allPossibleMoves != null) {
			move = allPossibleMoves[rnd.nextInt(allPossibleMoves.length)];
		} else {
			move = lastMove;
			//System.out.println("NULL, node " + currentNode);
		}

		return move;

	}

}
