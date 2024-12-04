package com.example.notesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;
    private static final String PREFS_NAME = "NotesAppPrefs";
    private static final String NOTES_KEY = "NotesList";
    private final Gson gson = new Gson();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final ActivityResultLauncher<Intent> noteLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String title = result.getData().getStringExtra("title");
                    String content = result.getData().getStringExtra("content");
                    int position = result.getData().getIntExtra("position", -1);

                    if (position == -1) {
                        // Add new note
                        notes.add(new Note(title, content));
                    } else {
                        // Edit existing note
                        notes.set(position, new Note(title, content));
                    }
                    saveNotesToPrefsInBackground();
                    adapter.notifyDataSetChanged();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notes = loadNotesFromPrefs();
        if (notes == null) {
            notes = new ArrayList<>();
        }

        ListView listViewNotes = findViewById(R.id.listViewNotes);
        Button btnCreateNote = findViewById(R.id.btnCreateNote);

        adapter = new NoteAdapter(this, notes, position -> {
            notes.remove(position);
            saveNotesToPrefsInBackground();
            adapter.notifyDataSetChanged();
        }, position -> {
            Note note = notes.get(position);
            Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            intent.putExtra("position", position);
            noteLauncher.launch(intent);
        });

        listViewNotes.setAdapter(adapter);

        btnCreateNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
            noteLauncher.launch(intent);
        });
    }

    private void saveNotesToPrefsInBackground() {
        new Thread(() -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            String json = gson.toJson(notes);
            editor.putString(NOTES_KEY, json);
            editor.apply();

            mainHandler.post(() -> {
                 Toast.makeText(this, "Notes saved successfully!", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    private ArrayList<Note> loadNotesFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(NOTES_KEY, null);
        if (json == null) {
            return null;
        }
        Type type = new TypeToken<ArrayList<Note>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
