import java.util.*;

public class Johnny5 extends Agent
{
    Random r;
    private int largestIndex;
    private int largestTogether;
    private final int ADDITIONAL_LENGTH_FOR_WIN=3;
    private final int LOWEST_SLOT=5;
    private final int LAST_COLUMN=6;
    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public Johnny5(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
        largestIndex=-1;
        largestTogether=0;
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {  
        largestTogether=0;
        final int FIRST_MOVE_COLUMN=3;
        final int SECOND_MOVE_COLUMN=2;
        final int THIRD_MOVE_COLUMN=4;
        int blockLimit=3;
        
        intialMoves(FIRST_MOVE_COLUMN, SECOND_MOVE_COLUMN, THIRD_MOVE_COLUMN);
        
        if(largestIndex==-1)
        {
            inRowHorizontalForward();
            inRowHorizontalBack();
            inRowVertical();
            inRowDiagonalForward();
            inRowDiagonalBack();
        }
        
        
        if(largestIndex==-1)
        {
         largestIndex = randomMove();
        }
        
       if(theyCanWin(blockLimit)!=-1)
       {
           largestIndex = theyCanWin(blockLimit);
       }
         
        while(getLowestEmptyIndex(myGame.getColumn(largestIndex))<0)
        {
          largestIndex = randomMove();
        } 
        moveOnColumn(largestIndex);
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }

    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.
     */
    public int iCanWin()
    {
        return 0;
    }
        
    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *@param blockLimit sets the threshhold to block.
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin(int blockLimit)
    {
        
        if(theyCanWinForward(blockLimit)!=-1)
        {
            return theyCanWinForward(blockLimit);
        }
        if(theyCanWinBack(blockLimit)!=-1)
        {
            return theyCanWinBack(blockLimit);
        }
        if(theyCanWinDown(blockLimit)!=-1)
        {
            return theyCanWinDown(blockLimit);
        }
        if(theyCanWinDiagonalForward(blockLimit)!=-1)
        {
            return theyCanWinDiagonalForward(blockLimit);
        }
        if(theyCanWinDiagonalBack(blockLimit)!=-1)
        {
            theyCanWinDiagonalBack(blockLimit);
        }
            
        return -1;    
    }
   
    /**
     * Returns the column that would allow the opponent to win down.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *@param blockLimit sets the threshhold to block.
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWinDown(int limit)
    {
        int together = 0;
        int maxSlot = LOWEST_SLOT;
        boolean isMyColor= true;
        int slot=0;
        for(int column=0; column<=LAST_COLUMN; column++)
        {
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            if(slot+ADDITIONAL_LENGTH_FOR_WIN>LOWEST_SLOT)
            {
                maxSlot = LOWEST_SLOT;
            }
            else
            {
                maxSlot = slot +ADDITIONAL_LENGTH_FOR_WIN;
            }
            together = 0;
            for(int i=slot+1; i<=maxSlot && isMyColor== true; i++)
            {
                if(myGame.getColumn(column).getSlot(i).getIsRed()!=iAmRed)
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }
                    
                if(together==limit)
                {
                   return column;
                }
            }
        }
        return -1;
    }

        /**
     * Returns the column that would allow the opponent to win back.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *@param limit sets the threshhold to block.
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWinBack(int limit)
    {
        int together = 0;
        int minColumn = 0;
        boolean isMyColor= true;
        int slot=0;
        for(int column=0; column<=LAST_COLUMN; column++)
        {
            if(column-ADDITIONAL_LENGTH_FOR_WIN<0)
            {
                minColumn = 0;
            }
            else
            {
                minColumn = column -ADDITIONAL_LENGTH_FOR_WIN;
            }
            
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            together = 0;
            if(slot ==-1)
            {
                isMyColor=false;
            }
            
            for(int i=column-1; i>=minColumn && isMyColor== true; i--)
            {
                if(myGame.getColumn(i).getSlot(slot).getIsRed()!=iAmRed && myGame.getColumn(i).getSlot(slot).getIsFilled())
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }
                if(together==limit)
                {
                    return column;
                }
            }   
            
            
        }
        return -1;
    }
    
       /**
     * Returns the column that would allow the opponent to win forward.
     * 
     *@param limit sets the threshhold to block.
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWinForward(int limit)
    {
        int together = 0;
        int maxColumn = 0;
        boolean isMyColor= true;
        int slot=0;
        for(int column=0; column<=LAST_COLUMN; column++)
        {

            if(column+ADDITIONAL_LENGTH_FOR_WIN > LAST_COLUMN)
            {
                maxColumn = LAST_COLUMN;
            }
            else
            {
                maxColumn= column+ADDITIONAL_LENGTH_FOR_WIN;
            }
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            together = 0;
            if(slot ==-1)
            {
                isMyColor=false;
            }
            for(int i=column+1; i<=maxColumn && isMyColor== true; i++)
            {
                if(myGame.getColumn(i).getSlot(slot).getIsRed()!=iAmRed && myGame.getColumn(i).getSlot(slot).getIsFilled())
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }
                if(together==limit)
                {
                    return column;
                }
            }
       
        }
        return -1;
    }
    
     /**
     * Returns the column that would allow the opponent to win diagonally forward.
     * 
     *@param limit sets the threshhold to block.
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWinDiagonalForward(int limit)
    {
        int together = 0;
        int maxSlot = LOWEST_SLOT;
        int maxColumn =0;
        boolean isMyColor= true;
        int slot=0;
        for(int column=0; column<LAST_COLUMN; column++)
        {
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            together = 0;
            if(slot+ADDITIONAL_LENGTH_FOR_WIN>LOWEST_SLOT)
            {
                maxSlot = LOWEST_SLOT;
            }
            else
            {
                maxSlot = slot +ADDITIONAL_LENGTH_FOR_WIN;
            }
            if(column+ADDITIONAL_LENGTH_FOR_WIN>LAST_COLUMN)
            {
                maxColumn = LAST_COLUMN;
            }
            else
            {
                maxColumn = column +ADDITIONAL_LENGTH_FOR_WIN;
            }
            for(int i=1; slot + i<=maxSlot && maxColumn >=column + i && isMyColor== true; i++)
            {
                if(myGame.getColumn(column+i).getSlot(slot+i).getIsRed()!=iAmRed && myGame.getColumn(column+i).getSlot(slot+i).getIsFilled())
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }

                if(together==limit)
                {
                    return column;
                }
            }
                
        }
        return -1; 
    }
    
        /**
     * Returns the column that would allow the opponent to win diagonally back.
     * 
     *@param limit sets the threshhold to block.
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWinDiagonalBack(int limit)
    {
        int together = 0;
        int maxSlot = LOWEST_SLOT;
        int minColumn =0;
        boolean isMyColor= true;
        int slot=0;
        for(int column=1; column<=LAST_COLUMN; column++)
        {
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            together = 0;
            if(slot+ADDITIONAL_LENGTH_FOR_WIN>LOWEST_SLOT)
            {
                maxSlot = LOWEST_SLOT;
            }
            else
            {
                maxSlot = slot +ADDITIONAL_LENGTH_FOR_WIN;
            }
            if(column-ADDITIONAL_LENGTH_FOR_WIN<0)
            {
                minColumn = 0;
            }
            else
            {
                minColumn = column -ADDITIONAL_LENGTH_FOR_WIN;
            }
            for(int i=1; slot + i<=maxSlot &&  column - i>=minColumn && isMyColor== true; i++)
            {
                if(myGame.getColumn(column-i).getSlot(slot+i).getIsRed()!=iAmRed && myGame.getColumn(column-i).getSlot(slot+i).getIsFilled())
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }

                if(together==limit)
                {
                    return column;
                }
            }
                
        }
        return -1;
    }
    
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "Johnny 5";
    }
    
    /**
     * Make moves initially to the middle of the board, if 2-4 are available in the first slot.
     *
     * @return the column to make the move on.
     */
    public void intialMoves(int first, int second, int third)
    {

        if(getLowestEmptyIndex(myGame.getColumn(first))==LOWEST_SLOT)
        {
            largestIndex= first;
        }
        else if(getLowestEmptyIndex(myGame.getColumn(second))==LOWEST_SLOT)
        {
            largestIndex = second;
        }
        else if(getLowestEmptyIndex(myGame.getColumn(third))==LOWEST_SLOT)
        {
            largestIndex = third;
        }
        else
        {
            largestIndex = -1;
        }
    }
    
    /**
     * Determines how many pieces are already together for a move horizontally forward.
     * 
     * @return the number already available together horizontal.
     */
    public void inRowHorizontalForward()
    {
        int together = 0;
        int maxColumn = 0;
        boolean isMyColor= true;
        int slot=0;
        for(int column=0; column<=LAST_COLUMN; column++)
        {

            if(column+ADDITIONAL_LENGTH_FOR_WIN > LAST_COLUMN)
            {
                maxColumn = LAST_COLUMN;
            }
            else
            {
                maxColumn= column+ADDITIONAL_LENGTH_FOR_WIN;
            }
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            together = 0;
            if(slot ==-1)
            {
                isMyColor=false;
            }
            for(int i=column+1; i<=maxColumn && isMyColor== true; i++)
            {
                if(myGame.getColumn(i).getSlot(slot).getIsRed()==iAmRed && myGame.getColumn(i).getSlot(slot).getIsFilled())
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }
                if(together>largestTogether)
                {
                    largestIndex=column;
                    largestTogether=together;
                }
            }

        }
        
    }
    
    /**
     * Determines how many pieces are already together for a move horizontally back.
     
     * @return the number already available together horizontal.
     */
    public void inRowHorizontalBack()
    {
        int together = 0;
        int minColumn = 0;
        boolean isMyColor= true;
        int slot=0;
        for(int column=0; column<=LAST_COLUMN; column++)
        {
            if(column-ADDITIONAL_LENGTH_FOR_WIN<0)
            {
                minColumn = 0;
            }
            else
            {
                minColumn = column -ADDITIONAL_LENGTH_FOR_WIN;
            }
            
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            together = 0;
            if(slot ==-1)
            {
                isMyColor=false;
            }
            
            for(int i=column-1; i>=minColumn && isMyColor== true; i--)
            {
                if(myGame.getColumn(i).getSlot(slot).getIsRed()==iAmRed && myGame.getColumn(i).getSlot(slot).getIsFilled())
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }
                if(together>largestTogether)
                {
                    largestIndex=column;
                    largestTogether=together;
                }
                    
                
            }   
            
            
        }
    }
    /**
     * Determines how many pieces are already together for a move vertically.
     *
     *
     */
    public void inRowVertical()
    {
        int together = 0;
        int maxSlot = LOWEST_SLOT;
        boolean isMyColor= true;
        int slot=0;
        for(int column=0; column<=LAST_COLUMN; column++)
        {
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            together = 0;
            if(slot+ADDITIONAL_LENGTH_FOR_WIN>LOWEST_SLOT)
            {
                maxSlot = LOWEST_SLOT;
            }
            else
            {
                maxSlot = slot +ADDITIONAL_LENGTH_FOR_WIN;
            }
            for(int i=slot+1; i<=maxSlot && isMyColor== true; i++)
            {
                if(myGame.getColumn(column).getSlot(i).getIsRed()==iAmRed)
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }

                if(together>largestTogether)
                {
                    largestIndex=column;
                    largestTogether=together;
                }
                    
                
            }
                       
        }
    }
    
    /**
     * Determines how many pieces are already together for a move vertically.
     *
     *
     */
    public void inRowDiagonalForward()
    {
        int together = 0;
        int maxSlot = LOWEST_SLOT;
        int maxColumn =0;
        boolean isMyColor= true;
        int slot=0;
        for(int column=0; column<LAST_COLUMN; column++)
        {
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            together = 0;
            if(slot+ADDITIONAL_LENGTH_FOR_WIN>LOWEST_SLOT)
            {
                maxSlot = LOWEST_SLOT;
            }
            else
            {
                maxSlot = slot +ADDITIONAL_LENGTH_FOR_WIN;
            }
            if(column+ADDITIONAL_LENGTH_FOR_WIN>LAST_COLUMN)
            {
                maxColumn = LAST_COLUMN;
            }
            else
            {
                maxColumn = column +ADDITIONAL_LENGTH_FOR_WIN;
            }
            for(int i=1; slot + i<=maxSlot && maxColumn >=column + i && isMyColor== true; i++)
            {
                if(myGame.getColumn(column+i).getSlot(slot+i).getIsRed()==iAmRed && myGame.getColumn(column+i).getSlot(slot+i).getIsFilled())
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }

                if(together>largestTogether)
                {
                    largestIndex=column;
                    largestTogether=together;
                }
                    
                
            }
                       
        }
    }
    
    /**
     * Determines how many pieces are already together for a move vertically.
     *
     *
     *
     *
     *
     *
     */
    public void inRowDiagonalBack()
    {
        int together = 0;
        int maxSlot = LOWEST_SLOT;
        int minColumn =0;
        boolean isMyColor= true;
        int slot=0;
        for(int column=1; column<=LAST_COLUMN; column++)
        {
            slot = getLowestEmptyIndex(myGame.getColumn(column));
            isMyColor= true;
            together = 0;
            if(slot+ADDITIONAL_LENGTH_FOR_WIN>LOWEST_SLOT)
            {
                maxSlot = LOWEST_SLOT;
            }
            else
            {
                maxSlot = slot +ADDITIONAL_LENGTH_FOR_WIN;
            }
            if(column-ADDITIONAL_LENGTH_FOR_WIN<0)
            {
                minColumn = 0;
            }
            else
            {
                minColumn = column -ADDITIONAL_LENGTH_FOR_WIN;
            }
            for(int i=1; slot + i<=maxSlot &&  column - i>=minColumn && isMyColor== true; i++)
            {
                if(myGame.getColumn(column-i).getSlot(slot+i).getIsRed()==iAmRed && myGame.getColumn(column-i).getSlot(slot+i).getIsFilled())
                {
                    together++;
                }
                else
                {
                    isMyColor=false;
                }

                if(together>largestTogether)
                {
                    largestIndex=column;
                    largestTogether=together;
                }
                    
                
            }
                       
        }
    }
}
    
