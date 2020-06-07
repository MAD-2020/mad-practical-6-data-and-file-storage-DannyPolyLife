package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    public int score;
    private Button b;
    private Button bCheck;
    private Button backButton;
    private TextView scoresDisplay;
    MyDBHandler dbHandler;
    String levelSelected;
    String username;
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */

        readyTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                final Toast ToastMessage;
                Log.v(TAG, "Countdown: " + millisUntilFinished/1000);
                ToastMessage = Toast.makeText(getApplicationContext(),"Get Ready In " + millisUntilFinished/1000 + " seconds", Toast.LENGTH_SHORT);
                ToastMessage.show();

                Timer cancelToast = new Timer();
                cancelToast.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ToastMessage.cancel();
                    }
                }, 1000);

            }

            @Override
            public void onFinish() {
                Log.v(TAG, "Countdown stopped");
                Toast.makeText(getApplicationContext(),"GO!", Toast.LENGTH_SHORT).show();
                readyTimer.cancel();
                placeMoleTimer();
            }


        };
        readyTimer.start();
    }
    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        int levelSpeed = (11 - Integer.parseInt(levelSelected)) * 1000;
        newMolePlaceTimer = new CountDownTimer(levelSpeed, levelSpeed) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (Integer.parseInt(levelSelected) > 5) {
                    setTwoMoles();
                }
                else {
                    setNewMole();
                }
            }

            @Override
            public void onFinish() {
                newMolePlaceTimer.start();
            }
        };
        newMolePlaceTimer.start();
    }
    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8, R.id.button9

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */
        scoresDisplay = findViewById(R.id.scoring);
        backButton = (Button) findViewById(R.id.backBtn);
        Intent receivingEnd = getIntent();
        levelSelected = receivingEnd.getStringExtra("Level");
        username = receivingEnd.getStringExtra("Username");

        dbHandler = new MyDBHandler(this, "WhackAMole.db", null, 1);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                Intent intentBack= new Intent(Main4Activity.this, Main3Activity.class);
                intentBack.putExtra("Username", username);
                startActivity(intentBack);
            }
        });



        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            b = (Button) findViewById(id);
            Log.v(TAG,"Populated!" + id);

            b.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    bCheck = findViewById(view.getId());
                    Log.v(TAG,"" + bCheck.getText().toString());
                    doCheck(bCheck);
                    newMolePlaceTimer.cancel();

                    if (Integer.parseInt(levelSelected) > 5) {
                        setTwoMoles();
                    }
                    else {
                        setNewMole();
                    }
                    newMolePlaceTimer.start();
                }
            });

        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        readyTimer();
        if (Integer.parseInt(levelSelected) > 5) {
            setTwoMoles();
        }
        else {
            setNewMole();
        }
    }
    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        if(checkButton.getText().toString().equals("*")){
            score += 1;
            scoresDisplay.setText("" + score);
        }
        else{
            score -= 1;
            scoresDisplay.setText("" + score);
        }
    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        for(int i=0;i<BUTTON_IDS.length;i++){
            b = (Button) findViewById(BUTTON_IDS[i]);
            if(i != randomLocation){
                b.setText("O");
            }
            else{
                b.setText("*");
            }
        }
    }

    public void setTwoMoles(){
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        int randomLocation2 = ran.nextInt(9);
        if(randomLocation == randomLocation2){
            setTwoMoles();
        }
        else{
            for(int i=0;i<BUTTON_IDS.length;i++){
                b = (Button) findViewById(BUTTON_IDS[i]);
                if(i != randomLocation || i != randomLocation2){
                    b.setText("O");
                }
                else{
                    b.setText("*");
                }
            }
        }
    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        newMolePlaceTimer.cancel();
        readyTimer.cancel();
        UserData userData = dbHandler.findUser(username);
        int currentLevel = userData.getLevels().get(Integer.parseInt(levelSelected) - 1);
        int highScore = userData.getScores().get(currentLevel);
        String currentScore = String.valueOf(score);
        Log.v(TAG, FILENAME + ": Current score: " + currentScore);
        if (score > highScore) {
            Log.v(TAG, FILENAME + ": Update user score");
            dbHandler.updateHighScore(username, Integer.parseInt(currentScore), Integer.parseInt(levelSelected));
        } else {
            Log.v(TAG, FILENAME + ": Did not beat high score");
        }

    }

}
