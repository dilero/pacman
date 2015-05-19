package pacman.controllers.me.montecarlo;


import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DilerPacMan extends Controller<MOVE> {

	final static int NUMBER_OF_TRIALS = 100;	

	@Override
	public MOVE getMove(Game game, long timeDue) {
	
		int currentNode = game.getPacmanCurrentNodeIndex();
		
		// if first move or pacman is in junction do simulation or pacman is eaten
		if(game.isJunction(currentNode) || lastMove == null ||game.wasPacManEaten()) {
			//System.out.println("CurNode: " + currentNode);
			
			MOVE move = simulateMC(game, currentNode);
			
			lastMove = move;
			
			if(timeDue < System.currentTimeMillis()) {
				System.out.println("Gecti");
			}
			
			return move;
		} // else move towards the junction which is not last junction
		else {
			
			MOVE[] possibleMoves = game.getPossibleMoves(currentNode, lastMove);
			assert possibleMoves.length == 1: "More than One neigbor";
			
			// possibleMoves returns null when pacman has died
			if(possibleMoves != null) {
				lastMove = possibleMoves[0];
			} else {
				//System.out.println("NULL, node Diler " +currentNode);
			}
			
			return lastMove;
			
		}
		
	}

	private MOVE simulateMC(Game game, int currentNode) {

		int neighboringNodes[] = null;

		// if pacman is eaten getNeighboringNodes returns null
		if(lastMove != null) {
			 neighboringNodes = game.getNeighbouringNodes(currentNode);
		} else {
			neighboringNodes = game.getNeighbouringNodes(currentNode);
		}
		
		// neighboringNodes returns null when pacman has died
		if(neighboringNodes == null) {
			return MOVE.NEUTRAL;
		}
		
		double scoresNeighboringNodes[] = new double[neighboringNodes.length];
		
//		System.out.println();
//		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
		
		// simulate montecarlo for every neighbor node
		for (int i = 0; i < neighboringNodes.length; i++) {
			MOVE moveToReach = game.getMoveToMakeToReachDirectNeighbour(currentNode, neighboringNodes[i]);
			DilerMonteCarloSimulation simulation = new DilerMonteCarloSimulation(
					game, moveToReach, NUMBER_OF_TRIALS);
			
			
			double score = simulation.runSimulation();
//			System.out.println("Average Score for " + neighboringNodes[i] +" with move " + moveToReach + " : " + score);
			
			scoresNeighboringNodes[i] = score;
		}

		double scoreMax = Double.NEGATIVE_INFINITY;
		int bestNode = -1;
		for (int j = 0; j < scoresNeighboringNodes.length; j++) {

			if (scoresNeighboringNodes[j] > scoreMax) {
				scoreMax = scoresNeighboringNodes[j];
				bestNode = neighboringNodes[j];
			}
		}
		
		
		MOVE move = game.getMoveToMakeToReachDirectNeighbour(currentNode, bestNode);
		
//		System.out.println("Move to " + bestNode + " : " + move);
//		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
//		System.out.println();
		
		return move;
	}

}
