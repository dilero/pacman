package pacman.controllers.me.montecarlo;

import pacman.controllers.Controller;
import pacman.controllers.examples.AggressiveGhosts;
import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.RandomGhosts;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class DilerMonteCarloSimulation {
	
	MOVE givenMove;
	Game game;
	int numberOfTrials;
	private static Controller<MOVE> dilerMonteCarloPacman;
	
	public DilerMonteCarloSimulation(Game game, MOVE move, int numberOfTrials) {
		this.givenMove = move;
		this.game = game;
		if(dilerMonteCarloPacman == null){
			dilerMonteCarloPacman = new DilerMonteCarloPacMan();
		}
		this.numberOfTrials = numberOfTrials;
	}
	
	public double runSimulation() {
		double averageScore = 0;
		for(int i = 0; i< numberOfTrials; i++) {
			DilerExecutor executor = new DilerExecutor(game.copy());
			int score = executor.runMonteCarloExperiment(dilerMonteCarloPacman, new StarterGhosts(), givenMove);
			
			averageScore += score;
		}
		
		averageScore = averageScore/ numberOfTrials;
		
		return averageScore;
	}

}
