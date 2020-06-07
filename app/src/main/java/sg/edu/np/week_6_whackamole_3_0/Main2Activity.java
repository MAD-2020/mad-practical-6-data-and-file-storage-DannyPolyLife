package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */


    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    EditText createUserNameEditText;
    EditText createPwEditText;
    Button createCancelBtn;
    Button createBtn;
    MyDBHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */
        createUserNameEditText = (EditText) findViewById(R.id.createUserNameEditText);
        createPwEditText = (EditText) findViewById(R.id.createPwEditText);
        createCancelBtn = (Button) findViewById(R.id.createCancelBtn);
        createBtn = (Button) findViewById(R.id.createBtn);
        handler = new MyDBHandler(this, "WhackAMole.db", null, 1);

        createCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = createUserNameEditText.getText().toString();
                String inputPw = createPwEditText.getText().toString();

                newAccount(handler, inputUsername, inputPw);
            }
        });
    }
    public void newAccount(MyDBHandler aHandler, String aInputUsername, String aInputPw) {
        if (aInputUsername.length() != 0 && aInputPw.length() != 0) {
            ArrayList<Integer> scoreList = new ArrayList<>();
            ArrayList<Integer> levelList = new ArrayList<>();
            UserData existingAcc = aHandler.findUser(aInputUsername);
            if (existingAcc == null) {
                Log.v(TAG, FILENAME + ": New user created successfully!");
                for (int i = 1; i <= 10; i++) {
                    levelList.add(i);
                    scoreList.add(0);
                }
                aHandler.addUser(new UserData(aInputUsername, aInputPw, levelList, scoreList));
                Intent toLevelSelection = new Intent(Main2Activity.this,Main3Activity.class);
                toLevelSelection.putExtra("Username", aInputUsername);
                startActivity(toLevelSelection);
            }
            else {
                Log.v(TAG, FILENAME + ": User already exist during new user creation!");
                Toast.makeText(Main2Activity.this,"User already exist please use a new username and password.",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(Main2Activity.this,"An error has occurred",Toast.LENGTH_LONG).show();
        }
    }
    protected void onStop() {
        super.onStop();
        finish();
    }
}
