/**
 * A Connect-4 player that makes random valid moves.
 * 
 * Player that calculates the score for the next move
 * 
 * @author Daniel Szafir and Ian Rowland
 *
 */
public class GreedyPlayer implements Player
{
	
	int id;
	int opponent_id;
	int cols;
	
	
    @Override
    public String name() {
        return "Greedy Player";
    }

    @Override
    public void init(int id, int msecPerMove, int rows, int cols) {
    	this.id = id;//Your id is id, opponent's id is 3-id
    	opponent_id = 3-id;
    }

    @Override
    public void calcMove(
        Connect4Board board, int oppMoveCol, Arbitrator arb) 
        throws TimeUpException {
        // Make sure there is room to make a move.
        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }
        
        // Find maximum of relative scores for all possible moves        
    	int bestCol = 0;//Set up a variable to set the best column to
    	int highScore = -1000;//Set up a variable to set the best score to

            for(int col = 0; col < 7; col++) {//Cycle through each column of the Connect 4 board
            	
            	if (board.isValidMove(col)) {//If the move in this column is valid, then
            		board.move(col, id);//Make the move to this column
            		int score = (calcScore(board, id)) - (calcScore(board, opponent_id));//Detect the greedy player's score - opponents score if this move was made
            		board.unmove(col, id);//Take back the move
            		if(score > highScore) {//If this score is greater than the highest score
            			highScore = score;//Then set the highest score to this current score
            			bestCol = col;//And the best column to the current column
            		}
            	}
            }
            arb.setMove(bestCol);//Once every possible move has been cycled through, the move is set to the best column
    }


	// Return the number of connect-4s that player #id has.
	public int calcScore(Connect4Board board, int id)
	{
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}
}