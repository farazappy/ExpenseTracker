package me.farazappy.expensetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import me.farazappy.expensetracker.helpers.SessionManager;
import me.farazappy.expensetracker.models.User;

public class LoginActivity extends AppCompatActivity {

    EditText displayName;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn())
            loginUser();

        displayName = findViewById(R.id.displayName);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayName.getText().toString().isEmpty()) {
                    displayName.setError("Please enter your name.");
                    displayName.requestFocus();
                } else {
                    String _id = java.util.UUID.randomUUID().toString();
                    User user = new User(_id, displayName.getText().toString());

                    sessionManager.setUser(user);
                    sessionManager.setLogin(true);
                    loginUser();
                }
            }
        });
    }

    private void loginUser() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
