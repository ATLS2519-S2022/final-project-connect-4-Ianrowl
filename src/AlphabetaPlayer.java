public class AlphabetaPlayer implements Player {
	int id; 
	int opponent_id;
    int cols; 
    
    @Override
    public String name() {
        return "Beta";
    }

    @Override
    public void init(int id, int msecPerMove, int rows, int cols) {
    	this.id = id; //id is your player's id, opponent's id is 3-id
    	this.cols = cols;
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
        
        int move = 0; 
        int maxDepth = 1;
        int highScore = -1000;
        
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        
        // while there's time left and maxDepth <= number of moves remaining
        while(!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
        	
    		for(int i = 0; i < 7; i++) {//Cycle through each column
    			if (board.isValidMove(i)) {//If the move in this column is valid, then
	    			board.move(i, id);//Make the move to this column
	    			int score = alphabeta(board, maxDepth -1, alpha, beta, false, arb);//Calculate the score by running the minimax
	    			board.unmove(i, id);//Reverse the move
	    			if(score > highScore) {//If this score is greater than the highScore variable
	    				highScore = score;//Then the highScore equals this current score
	    				move = i;//And the move equals the current i
	    				alpha = Math.max(alpha, highScore);
	    				if(alpha > beta) {
	    					break;
	    				}
	    			}
    			}
    		}
        
            arb.setMove(move);//After the for loop has been looped through, set this move as your move
            maxDepth++;//Then increase the maxDepth
        }        

    }
    
    public int alphabeta(Connect4Board board, int depth, int alpha, int beta, boolean isMaximizing, Arbitrator arb) {
    	
//    	if depth = 0 or there's no moves left or time is up
//    			return the heuristic value of node 
    	
    	if (depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) {
    		return score(board);
    	}
    	

    	int bestScore;
    	
    	if(isMaximizing) {
    		bestScore = -1000;//Set the best score to a very low number
    		for(int i = 0; i < 7; i++) {//Cycle through each column
    			if (board.isValidMove(i)) {//If the move in this column is valid, then
	    			board.move(i, id);//Make the move to this column
	    			bestScore = Math.max(bestScore, alphabeta(board, depth - 1, alpha, beta, false, arb));//Calculate the highest score
	    			
	    			alpha = Math.max(alpha, bestScore);
	    			
	    			board.unmove(i, id);//Reverse the move
	    			if(alpha >= beta) {
	    				break;
	    			}
    			}
    		}
    		return bestScore;//Return the bestScore
    	}

    	else {
    		bestScore = 1000;//Set the bestScore to a high number
    		for(int i = 0; i < 7; i++) {//Cycle through each column
    			if (board.isValidMove(i)) {//If the move in this column is valid, then
	    			board.move(i, opponent_id);//Move the opponents to each column
	    			bestScore = Math.min(bestScore, alphabeta(board, depth - 1, alpha, beta, true, arb));//Calculate the minimum of the move
	    			
	    			beta = Math.min(beta, bestScore);
	    			
	    			board.unmove(i, opponent_id);//Reverse the move
	    			if(alpha >= beta) {
	    				break;
	    			}
    			}
    		}
    			
    		return bestScore;//Return the bestScore
    	}
    }
    
    // your score - opponent's score
    public int score(Connect4Board board) {//Look at how many options to get connect 4's. Biased towards the center
    	return calcScore(board, id) - calcScore(board, opponent_id);
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