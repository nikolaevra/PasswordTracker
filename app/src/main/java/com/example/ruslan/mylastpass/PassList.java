package com.example.ruslan.mylastpass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.jar.JarEntry;

public class PassList extends AppCompatActivity {

    static ArrayList<String> Names = new ArrayList<>();
    static ArrayList<String> Passes = new ArrayList<>();
    static ArrayList<String> Logins = new ArrayList<>();
    ListView listView;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.ruslan.notes", Context.MODE_PRIVATE);
        String string = sharedPreferences.getString("data", null);

        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Names);
        listView.setAdapter(arrayAdapter);

        if (string != null) {
            try {
                disparseArrayList(string);
                arrayAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
          Toast.makeText(getApplicationContext(), "No data to disparse",
                  Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Pass", Passes.get(i));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),
                        String.format("Password for: %s has been copied " +
                                "to the clipboard", Names.get(i)),
                        Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                new AlertDialog.Builder(PassList.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this password?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Names.remove(i);
                                Passes.remove(i);
                                Logins.remove(i);

                                save();

                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plus_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this.getApplicationContext(), AddPass.class);
                startActivity(intent);
                return true;

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

    @Override
    protected void onStop() {
        save();

        finish();
        super.onStop();
    }

    private String intoJSONstring(ArrayList<String> list) {
        JSONArray array = new JSONArray(Arrays.asList(list.toArray()));
        return array.toString();
    }

    private String parseForWrite () {
        String p_to_write = intoJSONstring(Passes);
        String n_to_write = intoJSONstring(Names);
        String u_to_write = intoJSONstring(Logins);

        return p_to_write + " | " + n_to_write + " | " + u_to_write;
    }

    private void disparseArrayList (String string) throws JSONException {

        ArrayList<String> JArray = new ArrayList<>(Arrays.asList(string.split(" | ")));

        JSONArray jsonArray = new JSONArray(JArray.get(0));
        for (int i = 0; i < jsonArray.length(); i++) {
            Passes.add(jsonArray.get(i).toString());
        }

        jsonArray = new JSONArray(JArray.get(2));
        for (int i = 0; i < jsonArray.length(); i++) {
            Names.add(jsonArray.get(i).toString());
        }

        jsonArray = new JSONArray(JArray.get(4));
        for (int i = 0; i < jsonArray.length(); i++) {
            Logins.add(jsonArray.get(i).toString());
        }
    }

    private void save () {
        String write = parseForWrite();
        SharedPreferences sharedPreferences = PassList.this.getSharedPreferences("com.example.ruslan.notes", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("data", write).apply();
        write = null;
    }
}
