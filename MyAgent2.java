/**
 * Connect 4 agent by Benjamin Larkin
 * September 21, 2015
 * v4 September 30, 2015
 * Final project for Udacity Intro to Java Course.
 */


import java.util.Random;

public class MyAgent2 extends Agent
{
    Random r;
    private static final int MAX_TRIES = 25;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent2(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
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
        //Set up the informtation for the agent to play.
        boolean played = false;
        int nextMove;
       
       
      
 
        nextMove = iCanWin(iAmRed); //check for winning moves.
        //if the iCanWin() function comes back with places to win then play there.
        if(nextMove > -1)
        {
            moveOnColumn(nextMove); //move in the column that you can win on.
            played = true;     //set played to true so it exits the turn.
        } else {
        nextMove = theyCanWin(iAmRed);
        //if the theyCanWin function comes back with places to block and agent hasn't played then play there.
        if(nextMove > -1 && !played)
        {
            moveOnColumn(nextMove);  //move in the column that will block their win.
            played = true;    //set played to true so it exits the turn.
        }
    }
         //if the agent hasn't played due to the win functions invoke a different move function to see if we can set up to win in a later round.
        if(!played)
        {
                int w = 0;   //variable to iterate through columns
                while(w < myGame.getColumnCount() && !played)  //while loop to check for plays.
                {
                   if(getLowestEmptyIndex(myGame.getColumn(w)) != -1) 
                   {
                       if(possWin(w, iAmRed) && !possWin(w, !iAmRed)) //if we can set up to win play there and end the loop.
                       {
                           moveOnColumn(w); //play on column w
                           played = true;   //set played to true to end turn.
                        }                      
                   }
                   w++;
                }
         }
        
        //if agent hasn't played yet check to see if we can block a two in a row.
        if(!played) 
        {
            int w = twoInRow(!iAmRed); //check for two in a row for opposing player
            if(w != -1) { //if there are two in a row that can be blocked play there an exit turn.
                moveOnColumn(w);
                played = true;
            }    
            
        }
              
        //if the agent isn't able to move to set up a win in a later round make a random move that doesn't set the opponent up to win.    
        if(!played)    
        {    
                int i = randomMove();    //choose a random move column.
                int w = 0;               //variable to terminate after 25 tries.
                while(possWin(i, !iAmRed) && w < MAX_TRIES) //check to see if we would set up yellow to win.
                {
                    int j = i;
                    i = randomMove();   //if the testRandom() fails generate a new random move.
                    if( j == i)         // if the new randomMove() is the same as the old generate a new one.
                      i = randomMove();  
                   
                    w++;  
                }
                moveOnColumn(i);  //move in the column that passed the random test.
            
            }
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
     * @param the color of the agent
     * @return the column that would allow the agent to win -1 if no such column.
     */
    public int iCanWin(boolean red)
    {
        return canWin(red);        //call the method to test if player agent can win.           
    }

    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     * @param the color of the agent
     * @return the column that would allow the opponent to win -1 if no such column.
     */
    public int theyCanWin(boolean red)
    {
         return canWin(red);       //call the method to see if the other agent can win. 
    }

    
    /**
     * Method to test if agent can win the game
     * @param  boolean agent color.
     * @return the column that red could win the game in or -1 if none.
     */
    public int canWin(boolean red)
    {  
        for(int i = 0; i < myGame.getColumnCount(); i++) //iterate through the columns
        {
           for ( int j = 0; j < myGame.getRowCount(); j++) //iterate throught the rows
           {
               if(myGame.getColumn(i).getSlot(j).getIsFilled() && (myGame.getColumn(i).getSlot(j).getIsRed() == red))  //check to see if there is a "red" token in the slot.              
               {
                   if( j + 2 < myGame.getRowCount() && !myGame.getColumn(i).getIsFull()) //check the column for 3 in a row vertically
                   {
                       int w = threeInColumn(i, j, red);
                       if(w != -1) //if there are three in a row vertically return that column.
                           return w;                          
                    }                    
                   if( i  < myGame.getColumnCount() - 2) //check rows for 3 in a row where i+2 <= #columns - 1.
                   {
                      int w = threeInRow(i, j, red);  
                      if(w != -1)               //if the test comes back with a possible win return the column to block.
                            return w;
                    }                
                   if( i  < myGame.getColumnCount()- 2 && j < myGame.getRowCount()-2) //check along the diagonal with slope -1
                   {
                      int w = negDiagonal(i, j, red); 
                      if( w != -1)
                           return w;                      
                   }                  
                   if(i + 3 < myGame.getColumnCount() && j > 2) //evaluate the diagonal with slope 1.
                   {
                      int w = posDiagonal(i, j, red); 
                      if(w != -1)
                           return w;
                   }
                } 
            }
        }            
        return -1;       //return -1 if none are found
    }  
    /**
     * Method to test if the move will give a chance to win
     * @param move column int
     * @param agent color.
     * @return true if a win will be possible after playing false if not.
     */
    public boolean possWin(int i , boolean red)
    {
        int j = getLowestEmptyIndex(myGame.getColumn(i));  //get the lowest unplayed row in the column
        if(possHorizontal(i, j, red))                   //check the column for a possible winning play next round in the next higher row
            return true;
        
        if(nextDiagonal(i, j, red))                     //check the diagonal above the playing position for a possible diagonal win next round
            return true;
            
        return false;                                      //return false if no possible wins found.
        
    }
 
    
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "My Agent";
    }
    
   
    /**
     * Method to check for three in a row for the columns
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return int of column number for 3 in a row or -1 if none
     */
    public int threeInColumn(int i,int j, boolean red)
    {
        if( j + 2 < myGame.getRowCount() && !myGame.getColumn(i).getIsFull()) //check the column for 3 in a row
           {
              if(myGame.getColumn(i).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i).getSlot(j+1).getIsRed() == red) //check the two tokens below
                 && myGame.getColumn(i).getSlot(j+2).getIsFilled() &&  (myGame.getColumn(i).getSlot(j+2).getIsRed() == red)
                 && j - 1 >= 0 && !myGame.getColumn(i).getSlot(j - 1).getIsFilled())
                 {
                       return i;
                 }
            }
        return -1;           //return false if there is not 3 in a row in the column
    }
    
    /**
     * Method to check if there is a win possible in a row horizontally
     * Includes three consecutive and three broken up by a space.
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return column if win is possible or -1 if none possible.
     */
    
    public int threeInRow(int i, int j, boolean red)
    {
        
            if(myGame.getColumn(i + 1).getSlot(j).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j).getIsRed() == red)  //check to see if there's 3 in a row
                                    && myGame.getColumn(i + 2).getSlot(j).getIsFilled() && (myGame.getColumn(i + 2).getSlot(j).getIsRed() == red) ) 
                       {
                           if(i > 0 && !myGame.getColumn(i - 1).getSlot(j).getIsFilled())  //check for columns before the 3 in a row.                                  
                           {
                              if(j == myGame.getRowCount() - 1 || myGame.getColumn(i - 1).getSlot(j + 1).getIsFilled()) //check to see if the play level is at the row leve
                                         return i - 1;
                           }
                       }                        
            if( i < myGame.getColumnCount() - 3 && myGame.getColumn(i + 1).getSlot(j).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j).getIsRed() == red )
                        && myGame.getColumn(i + 2).getSlot(j).getIsFilled() && (myGame.getColumn(i + 2).getSlot(j).getIsRed() == red)
                        && !myGame.getColumn(i + 3).getSlot(j).getIsFilled()) //check to see if there is room to play after the 3 in a row.
                        {
                            if(j == myGame.getRowCount() - 1 || myGame.getColumn(i + 3).getSlot(j + 1).getIsFilled() ) //check to see if the play level is right.
                                 return i + 3;                           
                        }                       
            if( i < myGame.getColumnCount() - 3 && !myGame.getColumn(i + 1).getSlot(j).getIsFilled() && myGame.getColumn(i + 3).getSlot(j).getIsFilled() 
                        && (myGame.getColumn(i + 3).getSlot(j).getIsRed() == red ) && myGame.getColumn(i + 2).getSlot(j).getIsFilled()
                        && (myGame.getColumn(i + 2).getSlot(j).getIsRed() == red) )//check to see if there is place to play in the second position of 4 in a row.
                       {
                            if(j == myGame.getRowCount() - 1 || myGame.getColumn(i + 1 ).getSlot(j + 1).getIsFilled() ) //check if the play level is right.
                                 return i + 1;
                        }                       
            if( i < myGame.getColumnCount() - 3 && !myGame.getColumn(i + 2).getSlot(j).getIsFilled() && myGame.getColumn(i + 3).getSlot(j).getIsFilled() 
                        && ( myGame.getColumn(i + 3).getSlot(j).getIsRed() == red) && myGame.getColumn(i + 1).getSlot(j).getIsFilled() 
                        && ( myGame.getColumn(i + 1).getSlot(j).getIsRed() == red) ) //check to see if there is a place in the 3rd position of 4 in a row.
                       {
                          if(j == myGame.getRowCount() - 1 || myGame.getColumn(i + 2).getSlot(j + 1).getIsFilled()) //check to see if the level is right.
                                return i + 2;                                   
                       }            
        
        return -1;
    }
    
    /**
     * Method to check for possible winning move along the diagonal with slope of -1
     * Will check for the ends and for plays in the middle of the possible 4.
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return int column to play to block or -1 if none possible
     */
    public int negDiagonal(int i, int j, boolean red)
    {
        
            if( i  < myGame.getColumnCount()-3 && j < myGame.getRowCount()-3)
            {
            if(myGame.getColumn(i+1).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i+1).getSlot(j + 1).getIsRed() == red) && //check for 3 diag
                     myGame.getColumn(i+2).getSlot(j + 2).getIsFilled() && (myGame.getColumn(i+2).getSlot(j + 2).getIsRed() == red) && //continuous.
                     !myGame.getColumn(i + 3).getSlot(j+3).getIsFilled() )
                     {
                        if(j + 3 == myGame.getRowCount() - 1 )         //check to see if the space under is filled or if the last would be the bottomw row.
                                 return i + 3;
                        else if( j + 4 < myGame.getRowCount() && myGame.getColumn(i+3).getSlot(j + 4).getIsFilled() )
                                 return i + 3;
                        else if(i > 0 && j > 0 && !myGame.getColumn(i - 1).getSlot(j -1).getIsFilled() && //if we don't block below try above
                              myGame.getColumn(i - 1).getSlot(j).getIsFilled())
                                 return i - 1;                                        
                      }         
            if(!myGame.getColumn(i+1).getSlot(j + 1).getIsFilled() &&
                     myGame.getColumn(i+2).getSlot(j + 2).getIsFilled() && ( myGame.getColumn(i+2).getSlot(j + 2).getIsRed() == red) && //check for split diagonal
                     myGame.getColumn(i + 3).getSlot(j+3).getIsFilled() && ( myGame.getColumn(i+3).getSlot(j + 3).getIsRed() == red ))   //in position 2
                      {
                         if(myGame.getColumn(i + 1).getSlot(j + 2).getIsFilled()) //check for appropriate play level to block.
                                 return i + 1;
                      }
            if(myGame.getColumn(i+1).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i+1).getSlot(j + 1).getIsRed() == red) && //check for split diagonal
                      !myGame.getColumn(i+2).getSlot(j + 2).getIsFilled() &&                                                           //in position 3.
                       myGame.getColumn(i + 3).getSlot(j+3).getIsFilled() && ( myGame.getColumn(i+3).getSlot(j + 3).getIsRed() == red))
                       {       
                          if(myGame.getColumn(i + 2).getSlot(j + 3).getIsFilled()) //check for appropriate play level.
                                 return i + 2;
                       }  
            }                  
            if( i < myGame.getColumnCount() - 2 && i > 0 && j > 0 && j < myGame.getRowCount() - 2 && !myGame.getColumn(i - 1).getSlot(j - 1).getIsFilled()
                && myGame.getColumn(i + 1).getSlot(j+1).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j+1).getIsRed() == red ) &&
                myGame.getColumn(i + 2).getSlot(j+2).getIsFilled() && (myGame.getColumn(i + 2).getSlot(j+2).getIsRed() == red)) //check for diagonal 
                {
                      if(myGame.getColumn(i - 1).getSlot(j).getIsFilled())  //check the play level.
                             return i - 1;
                }                               
        
        return -1;        
    }
    
    /**
     * Method to check the diagonal with slope 1.
     * The method will check the ends and possible gaps in the middle of the diagonal.
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return int column to play or -1 if none possible
     */
    
    public int posDiagonal(int i, int j, boolean red)
    {
        
            if(myGame.getColumn(i+1).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i+1).getSlot(j - 1).getIsRed() == red )&&
               myGame.getColumn(i+2).getSlot(j - 2).getIsFilled() && (myGame.getColumn(i+2).getSlot(j - 2).getIsRed() == red )&&  
               !myGame.getColumn(i + 3).getSlot(j-3).getIsFilled()) //check to see if the end needs blocked.
               {
                    if(myGame.getColumn(i + 3).getSlot(j - 2).getIsFilled())  //check play level.
                         return i + 3;  
               }
            if( j < myGame.getRowCount() - 1 && i > 0 && !myGame.getColumn(i - 1).getSlot(j + 1).getIsFilled() && //check the other end.
                myGame.getColumn(i+1).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i+1).getSlot(j - 1).getIsRed() == red ) &&
                myGame.getColumn(i+2).getSlot(j - 2).getIsFilled() && (myGame.getColumn(i+2).getSlot(j - 2).getIsRed() == red ))
                {
                    if(j + 1 == myGame.getRowCount() - 1 || myGame.getColumn(i).getSlot(j + 2).getIsFilled()) //check play level.
                         return i - 1;                                
                 }
            if(myGame.getColumn(i + 2).getSlot(j - 2).getIsFilled() && ( myGame.getColumn(i + 2).getSlot(j - 2).getIsRed() == red )
               && myGame.getColumn(i + 3).getSlot(j - 3).getIsFilled() && ( myGame.getColumn(i + 3).getSlot( j - 3).getIsRed() == red )
               && !myGame.getColumn(i + 1).getSlot(j - 1).getIsFilled()) //check for block in position 2.
               {
                   if(myGame.getColumn(i + 1).getSlot(j).getIsFilled()) //check play level.
                         return i + 1;
               }
            if(myGame.getColumn(i + 1).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j - 1).getIsRed()== red ) &&
                myGame.getColumn(i+3).getSlot(j - 3).getIsFilled() && (myGame.getColumn(i + 3).getSlot(j - 3).getIsRed() == red ) &&
                !myGame.getColumn(i + 2).getSlot(j - 2).getIsFilled()) //chekc for block in position 3.
                {
                    if(myGame.getColumn(i + 2).getSlot(j - 1).getIsFilled()) //check play level.
                          return i + 2;
                }           
        
       return -1;
    }
    
    /**
     * Method to test if the column we play in will allow a win in the next play by getting 4 in a row horizontal.
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return true if it will
     */
    public boolean possHorizontal(int i, int j, boolean red)
    {
        
         if(i < myGame.getColumnCount() - 3 && j != 0) //check for possible 3 in row starting at play
           {
                if(myGame.getColumn(i + 1).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j - 1).getIsRed() == red ) &&
                   myGame.getColumn(i + 2).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i + 2).getSlot(j - 1).getIsRed() == red ) &&
                   myGame.getColumn(i + 3).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i + 3).getSlot(j - 1).getIsRed() == red ))
                        return true;
            }        
         if(i > 2 && j != 0) //check for 3 in a row ending at play
           {
           if(myGame.getColumn(i - 1).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i - 1).getSlot(j - 1).getIsRed() == red ) && 
               myGame.getColumn(i - 2).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i - 2).getSlot(j - 1).getIsRed() == red ) &&
               myGame.getColumn(i - 3).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i - 3).getSlot(j - 1).getIsRed() == red ))
                
                        return true;
           }        
         if(0 < i && i < myGame.getColumnCount() - 2 && j > 0) //check for 3 in a row in middle
           {
            if(myGame.getColumn(i - 1).getSlot(j - 1).getIsFilled() && ( myGame.getColumn(i - 1).getSlot(j - 1).getIsRed() == red ) &&
               myGame.getColumn(i + 1).getSlot(j - 1).getIsFilled() && ( myGame.getColumn(i + 1).getSlot(j - 1).getIsRed() == red ) &&
               myGame.getColumn(i + 2).getSlot(j - 1).getIsFilled() && ( myGame.getColumn(i + 2).getSlot(j - 1).getIsRed() == red ))
                        return true;
            }        
         if(1 < i && i < myGame.getColumnCount() - 1 && j > 0) //check for 3 in a row in middle
            {
            if(myGame.getColumn(i - 1).getSlot(j - 1).getIsFilled() && ( myGame.getColumn(i - 1).getSlot(j - 1).getIsRed() == red ) &&
               myGame.getColumn(i + 1).getSlot(j - 1).getIsFilled() && ( myGame.getColumn(i + 1).getSlot(j - 1).getIsRed() == red ) &&
               myGame.getColumn(i - 2).getSlot(j - 1).getIsFilled() && ( myGame.getColumn(i - 2).getSlot(j - 1).getIsRed() == red ))
                        return true;
            }            
         return false;           
        
    }
    
    /**
     * Method to check if there's a possible win next play on the diagonal
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return boolean true if there's a possible win
     */
    public boolean nextDiagonal(int i, int j, boolean red)
    {
        
        if(i < myGame.getColumnCount() - 3 && j > 3) //check for 3 in a row diagonal up starting at play
        {
            if(myGame.getColumn(i + 1).getSlot(j -2).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j - 2).getIsRed() == red ) &&
               myGame.getColumn(i + 2).getSlot(j - 3).getIsFilled() && (myGame.getColumn(i + 2).getSlot(j - 3).getIsRed() == red ) &&
               myGame.getColumn(i + 3).getSlot(j - 4).getIsFilled() && (myGame.getColumn(i + 3).getSlot(j - 4).getIsRed() == red ))
                        return true;
            
        }
        
        if(i > 2 && 0 < j && j < myGame.getRowCount() - 2) //check for three in a row up ending at play
        {
            if(myGame.getColumn(i - 1).getSlot(j).getIsFilled() && (myGame.getColumn(i - 1).getSlot(j).getIsRed() == red ) &&
               myGame.getColumn(i - 2).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i - 2).getSlot(j + 1).getIsRed() == red ) &&
               myGame.getColumn(i - 3).getSlot(j + 2).getIsFilled() && (myGame.getColumn(i - 3).getSlot(j + 2).getIsRed() == red ))
                        return true;
        }
        
        if(i < myGame.getColumnCount() - 2 && i > 0 && 2 < j && j < myGame.getRowCount() - 1) //check for 3 in a row up in middle
        {
            if(myGame.getColumn(i - 1).getSlot(j).getIsFilled() && ( myGame.getColumn(i - 1).getSlot(j).getIsRed() == red ) &&
               myGame.getColumn(i + 1).getSlot(j - 2).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j - 2).getIsRed() == red ) &&
               myGame.getColumn(i + 2).getSlot(j - 3).getIsFilled() && (myGame.getColumn(i + 2).getSlot(j - 3).getIsRed() == red ))
                        return true;
        }
        
        if(i < myGame.getColumnCount() - 1 && i > 1 && 1 < j && j < myGame.getRowCount() - 1) //check for 3 in a row up in middle
        {
            if(myGame.getColumn(i - 2).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i - 2).getSlot(j + 1).getIsRed() == red ) &&
               myGame.getColumn(i - 1).getSlot(j).getIsFilled() && (myGame.getColumn(i - 1).getSlot(j).getIsRed() == red ) &&
               myGame.getColumn(i + 1).getSlot(j - 2).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j - 2).getIsRed() == red ))
                        return true;
        }
        
        if(i < myGame.getColumnCount() - 3 && 0 < j && j < myGame.getRowCount() - 2) //check for 3 in a row down starting at play
        {
            if(myGame.getColumn(i + 1).getSlot(j).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j).getIsRed()== red ) &&
               myGame.getColumn(i + 2).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i + 2).getSlot(j + 1).getIsRed() == red ) &&
               myGame.getColumn(i + 3).getSlot(j + 2).getIsFilled() && (myGame.getColumn(i + 3).getSlot(j + 2).getIsRed() == red ))
                        return true;
        }
        
        if(i > 0 && i < myGame.getColumnCount() - 2 && 1 < j && j < myGame.getRowCount() - 2) //check for three in a row down in middle
        {
            if(myGame.getColumn(i - 1).getSlot(j - 2).getIsFilled() && (myGame.getColumn(i - 1).getSlot(j - 2).getIsRed()== red ) &&
               myGame.getColumn(i + 1).getSlot(j).getIsFilled() && (myGame.getColumn(i +1).getSlot(j).getIsRed() == red ) &&
               myGame.getColumn(i + 2).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i +2).getSlot(j + 1).getIsRed() == red ))
                        return true;
        }
        
        if(i > 1 && i < myGame.getColumnCount() - 1 && 2 < j && j < myGame.getRowCount() - 2) //check for three in a row down in middle
        {
            if(myGame.getColumn(i - 2).getSlot(j - 3).getIsFilled() && (myGame.getColumn(i - 2).getSlot(j - 3).getIsRed() == red ) &&
               myGame.getColumn(i - 1).getSlot(j - 2).getIsFilled() && (myGame.getColumn(i - 1).getSlot(j - 2).getIsRed() == red ) &&
               myGame.getColumn(i + 1).getSlot(j).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j).getIsRed() == red ))
                        return true;
        }
        
        if( i > 3 && 4 < j) //check for 3 in a row down ending at play
        {
            if(myGame.getColumn(i - 1).getSlot(j - 2).getIsFilled() && (myGame.getColumn(i - 1).getSlot(j - 2).getIsRed() == red ) &&
               myGame.getColumn(i - 2).getSlot(j - 3).getIsFilled() && (myGame.getColumn(i - 2).getSlot(j - 3).getIsRed() == red ) &&
               myGame.getColumn(i - 3).getSlot(j - 4).getIsFilled() && (myGame.getColumn(i - 3).getSlot(j - 4).getIsRed() == red ))
                        return true;
        }           
       return false;
    }
    
    /**
     * Method to check for blocking 2 in a row.
     * @param boolean iAmRed
     * @return int column to block in or -1 if there is none
     */
    public int twoInRow(boolean red)
    {
        for(int i = 0; i < myGame.getColumnCount(); i++) //iterate through the columns
        {
           for(int j = 0; j < myGame.getRowCount(); j++)  //iterate through the rows
           {
               if(myGame.getColumn(i).getSlot(j).getIsFilled() && (myGame.getColumn(i).getSlot(j).getIsRed() == red))  //check to see if there is a "red" token in the slot.              
               {
                  if( j  < myGame.getRowCount() - 2 && !myGame.getColumn(i).getIsFull()) //check the column for 2 in a row vertically
                  {
                      int w = twoColumn(i, j, red);
                      if(w != -1) //if there are three in a row vertically return that column.
                           return w;                          
                    }                    
                   if( i  < myGame.getColumnCount() - 1) //check rows for 3 in a row where i+1 <= #columns - 1.
                   {
                      int w = twoRow(i, j, red);  
                      if(w != -1)               //if the test comes back with a possible win return the column to block.
                            return w;
                    }                
                   if( i  < myGame.getColumnCount() - 1 && j < myGame.getRowCount()- 1) //check along the diagonal with slope -1
                   {
                      int w = neg2Diagonal(i, j, red); 
                      if( w != -1)
                           return w;                      
                   }                  
                   if(i + 3 < myGame.getColumnCount() && j > 2) //evaluate the diagonal with slope 1.
                   {
                      int w = pos2Diagonal(i, j, red); 
                      if(w != -1)
                           return w;
                   }
                } 
            }
        }            
        return -1;       //return -1 if none are found
        
        
    }   
        
        
     
    
     /**
     * Method to check the diagonal with slope 1 for 2 in a row.
     * The method will check the ends and possible gaps in the middle of the diagonal.
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return int column to play or -1 if none 
     */
    
    public int pos2Diagonal(int i, int j, boolean red)
    {
        
            if(myGame.getColumn(i+1).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i+1).getSlot(j - 1).getIsRed() == red )&&
               !myGame.getColumn(i + 2).getSlot(j-2).getIsFilled()) //check to see if the end needs blocked.
               {
                    if(myGame.getColumn(i + 2).getSlot(j - 3).getIsFilled())  //check play level.
                         return i + 2;  
               }
            if( j < myGame.getRowCount() - 1 && i > 0 && !myGame.getColumn(i - 1).getSlot(j + 1).getIsFilled() && //check the other end.
                myGame.getColumn(i+1).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i+1).getSlot(j - 1).getIsRed() == red ))
                {
                    if(j + 1 == myGame.getRowCount() - 1) {  //check play level.
                         return i - 1; 
                        } else if(j < myGame.getRowCount() - 2 && myGame.getColumn(i).getSlot(j + 2).getIsFilled())
                         return i - 1;
                     
                 }
            if(myGame.getColumn(i + 2).getSlot(j - 2).getIsFilled() && ( myGame.getColumn(i + 2).getSlot(j - 2).getIsRed() == red )
               && !myGame.getColumn(i + 1).getSlot(j - 1).getIsFilled()) //check for block in position 2.
               {
                   if(myGame.getColumn(i + 1).getSlot(j).getIsFilled()) //check play level.
                         return i + 1;
               }
            if(myGame.getColumn(i + 1).getSlot(j - 1).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j - 1).getIsRed()== red ) &&
                !myGame.getColumn(i + 2).getSlot(j - 2).getIsFilled()) //check for block in position 2.
                {
                    if(myGame.getColumn(i + 2).getSlot(j - 1).getIsFilled()) //check play level.
                          return i + 2;
                }           
        
       return -1;
    }
    
    /**
     * Method to check for possible 2 in a row along the diagonal with slope of -1
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return int column to play to block or -1 if none 
     */
    public int neg2Diagonal(int i, int j, boolean red)
    {
        
            if( i  < myGame.getColumnCount() - 2 && j < myGame.getRowCount() - 2)
            {
            if(myGame.getColumn(i+1).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i+1).getSlot(j + 1).getIsRed() == red) && //check for 2 on the diagonal
                     !myGame.getColumn(i + 2).getSlot(j+2).getIsFilled() )
                     {
                        if(j + 2 == myGame.getRowCount() - 1 )         //check to see if the space under is filled or if the last would be the bottomw row.
                                 return i + 2;
                        else if( j + 3 < myGame.getRowCount() && myGame.getColumn(i+2).getSlot(j + 3).getIsFilled() )
                                 return i + 2;
                        else if(i > 0 && j > 0 && !myGame.getColumn(i - 1).getSlot(j -1).getIsFilled() && //if we don't block below try above
                              myGame.getColumn(i - 1).getSlot(j).getIsFilled())
                                 return i - 1;                                        
                      }         
            if(!myGame.getColumn(i+1).getSlot(j + 1).getIsFilled() &&
                     myGame.getColumn(i+2).getSlot(j + 2).getIsFilled() && ( myGame.getColumn(i+2).getSlot(j + 2).getIsRed() == red)) //check for split diagonal pos 2
                     
                      {
                         if(myGame.getColumn(i + 1).getSlot(j + 2).getIsFilled()) //check for appropriate play level to block.
                                 return i + 1;
                      }
            if(myGame.getColumn(i+1).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i+1).getSlot(j + 1).getIsRed() == red) && //check for split diagonal
                      !myGame.getColumn(i+2).getSlot(j + 2).getIsFilled())                                                         //in position 3.
                      
                       {       
                          if(myGame.getColumn(i + 2).getSlot(j + 3).getIsFilled()) //check for appropriate play level.
                                 return i + 2;
                       }  
            }                  
            if( i < myGame.getColumnCount() - 1 && i > 0 && j > 0 && j < myGame.getRowCount() - 1 && !myGame.getColumn(i - 1).getSlot(j - 1).getIsFilled()
                && myGame.getColumn(i + 1).getSlot(j+1).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j+1).getIsRed() == red )) //check for diagonal 
                {
                      if(myGame.getColumn(i - 1).getSlot(j).getIsFilled())  //check the play level.
                             return i - 1;
                }                               
        
        return -1;        
    }
    
    /**
     * Method to check if there are 2 in a row horizontally
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return column if win is possible or -1 if none possible
     */
    
    public int twoRow(int i, int j, boolean red)
    {
        
            if(myGame.getColumn(i + 1).getSlot(j).getIsFilled() && (myGame.getColumn(i + 1).getSlot(j).getIsRed() == red))  //check to see if there's 2 in a row                                  
                       {
                           if(i > 0 && !myGame.getColumn(i - 1).getSlot(j).getIsFilled())  //check for columns before the 2 in a row.                                  
                           {
                              if(j == myGame.getRowCount() - 1 || myGame.getColumn(i - 1).getSlot(j + 1).getIsFilled()) //check to see if the play level is at the row leve
                                         return i - 1;
                           }
                       }           
            if( i < myGame.getColumnCount() - 2 && !myGame.getColumn(i + 2).getSlot(j).getIsFilled()  && myGame.getColumn(i + 1).getSlot(j).getIsFilled() 
                        && ( myGame.getColumn(i + 1).getSlot(j).getIsRed() == red) ) //play at the end of two in a row.
                       {
                          if(j == myGame.getRowCount() - 1 || myGame.getColumn(i + 2).getSlot(j + 1).getIsFilled()) //check to see if the level is right.
                                return i + 2;                                   
                       }            
        
        return -1;
    }
    
    /**
     * Method to check for 2 in a row vertically
     * @param int column #
     * @param int j row #
     * @param boolean iAmRed
     * @return column number to block or -1 for none available. 
     */
    public int twoColumn(int i,int j, boolean red)
    {
      if( j + 1 < myGame.getRowCount() && j - 1 >= 0) //check the column for 2 in a row
           {
              if(myGame.getColumn(i).getSlot(j + 1).getIsFilled() && (myGame.getColumn(i).getSlot(j+1).getIsRed() == red) //check the two tokens below              
                 && j - 1 >= 0 && !myGame.getColumn(i).getSlot(j - 1).getIsFilled())
                 {
                       return i;
                 }
           }
        return -1;           //return false if there is not 2 in a row in the column
    }
}
