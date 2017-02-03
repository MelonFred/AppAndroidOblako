package com.example.foroblako.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private ListAdapter mAdapter;
    private ListView listView;
    Bundle b;

    static class Project {
        @SerializedName("id")
        int id;
        @SerializedName("title")
        String title;
        @SerializedName("todos")
        List<Todo> todos;
    }

    static class Todo {
        @SerializedName("id")
        int todo_id;
        @SerializedName("text")
        String text;
        @SerializedName("isCompleted")
        boolean isCompleted;
    }

    ArrayList<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

       Ion.with(this)

                .load(getString(R.string.kIndexRequest))

                .asJsonArray()

                .setCallback(new FutureCallback<JsonArray>() {

                    @Override

                    public void onCompleted(Exception e, JsonArray result) {

                        if (result != null) {

                            projects = new ArrayList<Project>();

                            for (final JsonElement projectJsonElement : result) {

                                projects.add(new Gson().fromJson(projectJsonElement, Project.class));

                            }

                            b = new Bundle();
                            List<String> string = new ArrayList<>();

                            for (int x = 0; x < projects.size(); x++) {
                                Project project = projects.get(x);
                                string.add(project.title);
                            }
                            b.putStringArrayList("projectsArray", (ArrayList<String>) string);

                            listView = (ListView) findViewById(R.id.listView);

                            mAdapter = new ListAdapter(listView.getContext());

                            for (int i = 0; i < projects.size(); i++) {

                                Project project = projects.get(i);

                                mAdapter.addSectionHeaderItem(project.title);

                                for (int y = 0; y < project.todos.size(); y++) {

                                    Collections.sort(project.todos, new Comparator<Todo>() {
                                        public int compare(Todo o1, Todo o2) {
                                            return o1.todo_id - o2.todo_id;
                                        }
                                    });

                                    Todo todo = project.todos.get(y);

                                    mAdapter.addItem(todo.text);
                                }
                            }


                            listView.setAdapter(mAdapter);
                        }
                    }

                });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtras(b);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}



