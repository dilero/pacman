package pacman.controllers.me.montecarlo;

import static pacman.game.Constants.DELAY;

import java.util.EnumMap;

import pacman.Executor;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class DilerExecutor extends Executor {

	Game game;
	final static int NUMBER_OF_MONTECARLO_MOVES = 50;
	int oldGameScore;
	final static int DEATH_SCORE = -600;
	final static int ALIVE_SCORE = 40;
	final static int GHOST_EATEN_SCORE = 200;
	final static int EATEN_POWERPILLS_SCORE = 100;
	final static int EATEN_PILLS_SCORE = 1;
	final static int LAST_QUARTER_EATEN_PILLS_SCORE = 2;
	final static int LAST_QQ_EATEN_PILLS_SCORE = 4;
	final static int LAST_EATEN_PILLS_SCORE = 100;
	final static int LEVEL_SCORE = 3000;

	public DilerExecutor(Game game) {
		this.game = game.copy();
		this.oldGameScore = calculateGameScoreForMonteCarlo();
//		System.out.println("------------OLD--------------");
//		System.out.println("YENENE PILL :"
//				+ (game.getNumberOfPills() - game.getNumberOfActivePills()));
//		System.out.println("ALIVE SCORE : "
//				+ game.getPacmanNumberOfLivesRemaining());
//		System.out.println("GHOST EATEN : " + game.getNumGhostsEaten());
//		System.out.println("GHOST EATEN : " + game.getNumGhostsEaten());
//		System.out.println("POWERPILL : "
//				+ (game.getNumberOfPowerPills() - game
//						.getNumberOfActivePowerPills()));
//		System.out.println("death score : " + game.gameOver());
//		System.out.println("----------------------------------");
	}

	public int runMonteCarloExperiment(Controller<MOVE> pacManController,
			Controller<EnumMap<GHOST, MOVE>> ghostController, MOVE firstMove) {

		game.advanceGame(
				firstMove,
				ghostController.getMove(game.copy(), System.currentTimeMillis()
						+ DELAY));

		for (int i = 0; i < NUMBER_OF_MONTECARLO_MOVES; i++) {
			game.advanceGame(
					pacManController.getMove(game, System.currentTimeMillis()
							+ DELAY),
					ghostController.getMove(game, System.currentTimeMillis()
							+ DELAY));
			// System.out.println(game.getPacmanLastMoveMade());
		}

		int gameScore = calculateGameScoreForMonteCarlo();
		if (gameScore - oldGameScore < -300) {
//			System.out
//					.println("YENENE PILL :"
//							+ (game.getNumberOfPills() - game
//									.getNumberOfActivePills()));
//			System.out.println("ALIVE SCORE : "
//					+ game.getPacmanNumberOfLivesRemaining());
//			System.out.println("GHOST EATEN : " + game.getNumGhostsEaten());
//			System.out.println("GHOST EATEN : " + game.getNumGhostsEaten());
//			System.out.println("POWERPILL : "
//					+ (game.getNumberOfPowerPills() - game
//							.getNumberOfActivePowerPills()));
//			System.out.println("death score : " + game.gameOver());
		}
//		System.out.println("MOVE " + firstMove + " Score diff "
//				+ (gameScore - oldGameScore) + " GAME SCORE : " + gameScore
//				+ " OLD GAME SCORE : " + oldGameScore);

		return gameScore - oldGameScore;

	}

	// get gameScore for evaluation of montecarlo move
	private int calculateGameScoreForMonteCarlo() {

		int mcScore = 0;

		if (game.gameOver()) {
			mcScore += DEATH_SCORE;
		} else {
			if (game.getPacmanNumberOfLivesRemaining() > 0) {
				mcScore += game.getPacmanNumberOfLivesRemaining() * ALIVE_SCORE;
			}
		}

		mcScore += game.getNumGhostsEaten() * GHOST_EATEN_SCORE;

		mcScore += evaluatePills(mcScore);
		mcScore += game.getCurrentLevel() * LEVEL_SCORE;
		mcScore += game.getScore();
		
		return mcScore;
	}

	private int evaluatePills(int mcScore) {
		mcScore += EATEN_POWERPILLS_SCORE
				* (game.getNumberOfActivePowerPills() - game
						.getNumberOfActivePowerPills());
		int numberOfEatenPills = game.getNumberOfPills()
				- game.getNumberOfActivePills();
		if (game.getNumberOfActivePills() < (game.getNumberOfPills() / 4)) {
			if (game.getNumberOfActivePills() < 3) {
				mcScore += LAST_EATEN_PILLS_SCORE * numberOfEatenPills;
			} else if (game.getNumberOfActivePills() < (game.getNumberOfPills() / 8))
				mcScore += LAST_QQ_EATEN_PILLS_SCORE * numberOfEatenPills;
			else {
				mcScore += LAST_QUARTER_EATEN_PILLS_SCORE * numberOfEatenPills;
			}
		} else {
			mcScore += EATEN_PILLS_SCORE * numberOfEatenPills;

		}
		return mcScore;
	}

}
