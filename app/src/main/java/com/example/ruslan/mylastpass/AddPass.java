package com.example.ruslan.mylastpass;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddPass extends AppCompatActivity {

    EditText password, login, siteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pass);

        password = (EditText) findViewById(R.id.Password);
        login = (EditText) findViewById(R.id.Login);
        siteName = (EditText) findViewById(R.id.siteName);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                String pass = password.getText().toString();
                String log = login.getText().toString();
                String site = siteName.getText().toString();

                if (pass.length() != 0  && log.length() != 0 && site.length() != 0) {
                    PassList.Passes.add(pass);
                    PassList.Names.add(site);
                    PassList.Logins.add(log);
                    Toast.makeText(getApplicationContext(),
                            "Saved Successfully", Toast.LENGTH_SHORT).show();

                    Intent upIntent = NavUtils.getParentActivityIntent(this);
                    if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                        TaskStackBuilder.create(this)
                                .addNextIntentWithParentStack(upIntent)
                                .startActivities();
                    } else {
                        NavUtils.navigateUpTo(this, upIntent);
                    }
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Can't leave blank fields", Toast.LENGTH_SHORT).show();
                    return false;
                }
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
