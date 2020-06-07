package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    EditText userNameEditText;
    EditText pwEditText;
    Button loginBtn;
    TextView newUserTextView;
    MyDBHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        pwEditText = (EditText) findViewById(R.id.pwEditText);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        newUserTextView = (TextView) findViewById(R.id.newUserTextView);
        handler = new MyDBHandler(this, "WhackAMole.db", null, 1);

        newUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(in);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = userNameEditText.getText().toString();
                String inputPw = pwEditText.getText().toString();
                boolean result = isValidUser(inputUsername, inputPw);

                if (result) {
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    Toast.makeText(MainActivity.this,"Login successful",Toast.LENGTH_LONG).show();
                    Intent in = new Intent(MainActivity.this, Main3Activity.class);
                    in.putExtra("Username", inputUsername);
                    startActivity(in);
                }
                else {
                    Toast.makeText(MainActivity.this,"Login unsuccessful",Toast.LENGTH_LONG).show();
                    Log.v(TAG, FILENAME + ": Invalid user!");
                }
            }
        });

    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        boolean result;
        UserData dbData = handler.findUser(userName);
        if (dbData != null) {
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            if (dbData.getMyUserName().equals(userName) && dbData.getMyPassword().equals(password)) {

                result = true;
            }
            else {
                result = false;
            }
        }
        else {
            result = false;
        }

        return result;
    }


}
