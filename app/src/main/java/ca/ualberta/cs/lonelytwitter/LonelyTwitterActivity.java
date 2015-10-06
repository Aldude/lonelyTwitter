package ca.ualberta.cs.lonelytwitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LonelyTwitterActivity extends Activity implements MyObserver {

    private static final String FILENAME = "file.sav"; // Model
    private EditText bodyText; // View
    private ListView oldTweetsList; // View
    private ArrayList<Tweet> tweets; // Model
    private ArrayAdapter<Tweet> adapter; // Controller


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bodyText = (EditText) findViewById(R.id.body); // View
        Button saveButton = (Button) findViewById(R.id.save); // View
        oldTweetsList = (ListView) findViewById(R.id.oldTweetsList); // View

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK); // Moadel
                String text = bodyText.getText().toString(); // move to controller
                tweets.add(new NormalTweet(text)); // move to controller
                saveInFile();  // move to model
                adapter.notifyDataSetChanged(); // Controller
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile(); // Moadel
        if (tweets == null) {
            throw new RuntimeException();
        }
        adapter = new ArrayAdapter<Tweet>(this, R.layout.list_item, tweets); // Controller
        oldTweetsList.setAdapter(adapter); // Controller
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME); // Model
            BufferedReader in = new BufferedReader(new InputStreamReader(fis)); // Controller
            Gson gson = new Gson();
            // Following line based on https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html retrieved 2015-09-21
            Type listType = new TypeToken<ArrayList<NormalTweet>>() {
            }.getType(); // Controller
            tweets = gson.fromJson(in, listType); // Controller

        } catch (FileNotFoundException e) {
            tweets = new ArrayList<Tweet>(); // Model
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    0); // Moadel
            OutputStreamWriter writer = new OutputStreamWriter(fos); // Controller
            Gson gson = new Gson();
            gson.toJson(tweets, writer); // Controller
            writer.flush(); // Controller
            fos.close(); // Model
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void myNotify(MyObservable observable) {
        adapter.notifyDataSetChanged(); // Controller
    }
}