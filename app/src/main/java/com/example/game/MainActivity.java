package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView playerOneScore;    //Showing how many games the playerOne has won
    private TextView playerTwoScore;    //Showing how many games the playerTwi has won
    private TextView playerStatus;      //For showing which player has more wins
    private Button [] buttons = new Button[9]; //For 9 buttons which make up the grid
    private Button resetGame;

    private int playerOneScoreCount;       //Incrementing no of games won by playerOne
    private int playerTwoScoreCount;    //Incrementing no of games won by playerTwo
    private int buttonsClicked;         //Keeping track of how many buttons have been clicked

    //space occupied by no one meaning empty spaces = 2
    //space occupied by playerOne = 0
    //space occupied by playerTwo = 1
    int [] gamestate = {2,2,2,2,2,2,2,2,2};
    int [][] winningPositions ={
            {0,1,2}, {3,4,5}, {6,7,8}, //Three in a row
            {0,3,6}, {1,4,7}, {2,5,8}, //Three in a column
            {0,4,8}, {2,4,6} //Three in a diagonal
    };
    Boolean activeplayer; //Checking which player's turn it is true for playerOne & false for playerTwo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Getting Resources

        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGame = (Button) findViewById(R.id.resetGame);
        for(int i=0 ; i<9 ; i++){
            String buttonId = "btn_" + i; //As "btn_" is common in the name and i will change the name as it increments and all the buttons will be accessed
            int resourceId = getResources().getIdentifier(buttonId, "id", getPackageName()); //This will turn the String to the R.id. Format
            buttons [i] = (Button) findViewById(resourceId);
            buttons [i].setOnClickListener(this); //this will allow the same onclick listner for all the buttons in the array
        }

        buttonsClicked = 0; //No of buttons clicked will be zero in the start of the round
        playerOneScoreCount = 0; //playerOne's score will be zero at the start of the round
        playerTwoScoreCount = 0; //playerTwo's score count will be zero at the start of the round
        activeplayer = true; //PlayerOne active at the start of the round
    }

    @Override
    public void onClick(View v) {
        if(!((Button)v).getText().toString().equals("")){  //checking if the button the player is trying to press is already pressed or not
            return;
        }
        String buttonId = v.getResources().getResourceEntryName(v.getId()); //getting the name/id of the button that's pressed
        int gamestatePointer = Integer.parseInt(buttonId.substring(buttonId.length()-1, buttonId.length())); //to get the last letter of the button name like 1 in btn_1 etc
                                                                                                            //this will give us the index of the button in the gamestate array that's pressed
        if(activeplayer){
            ((Button) v).setText("X");  //Changing the text of the button to mark a X
            ((Button) v).setTextColor(Color.parseColor("#db0000")); //changing the color of the X
            gamestate[gamestatePointer] = 0; //Marking that button is occupied by PlayerOne
        }
        else {
            ((Button) v).setText("O");  //Changing the text of the button to mark a O
            ((Button) v).setTextColor(Color.parseColor("#ffc65c")); //changing the color of the O
            gamestate[gamestatePointer] = 1; //Marking that button is occupied by PlayerTwo
        }
        buttonsClicked++; //Incrementing the no of clicked buttons

        if(checkWinner()){      //checking for a winner
            if(activeplayer){       //checking which player is playing
                playerOneScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player One Won This Round!!", Toast.LENGTH_SHORT).show();
                playAgain();
            }
            else{
                playerTwoScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player Two Won This Round!!", Toast.LENGTH_SHORT).show();
                playAgain();
            }
        }
        else if(buttonsClicked == 9){       //checking if all the buttons are pressed
            Toast.makeText(this, "Draw Round!!", Toast.LENGTH_SHORT).show();
            playAgain();
        }
        else {
            activeplayer = !activeplayer;   //shifting between two players playerOne is true and playertwo is false
        }

        if(playerOneScoreCount > playerTwoScoreCount){  //cheking which player has more wins
            playerStatus.setText("Player One has more wins!!");
        }
        else if(playerOneScoreCount < playerTwoScoreCount) {
            playerStatus.setText("Player Two has more wins!!");
        }
        else {
            playerStatus.setText("Its Anybody's Game!!");
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
                playAgain();
            }
        });
    }

    public Boolean checkWinner(){
        Boolean winnerResult = false;      //As no winners exist at the start of the game

        for(int [] winningPosition : winningPositions){ //passing the arrays pressent in the winning positions to check if they match
            if(gamestate[winningPosition[0]] == gamestate[winningPosition[1]]  //checking first two indexes of the subarrays
                    && gamestate[winningPosition[1]] == gamestate[winningPosition[2]]   //checking last two indexes of the subarrays
                        && gamestate[winningPosition[0]] != 2){     //checking if the values on that indexes is not an unoccupied space
                winnerResult = true; //updating result to true if the indexes of both arrays match
            }
        }
        return winnerResult;
    }

    public void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    public void playAgain(){
        buttonsClicked = 0;     //Setting buttons clicked to 0
        activeplayer = true;    //changing active player to PlayerOne

        for(int i=0; i<9; i++){
            gamestate[i] = 2;   //reseting the indexes to 2 to mark them as unoccupied
            buttons[i].setText(""); //Removing the Xs and Os from the buttons
        }
    }

}