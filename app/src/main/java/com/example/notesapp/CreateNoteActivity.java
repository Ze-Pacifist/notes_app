package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextContent;
    private int notePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        Button btnSaveNote = findViewById(R.id.btnSaveNote);

        Intent intent = getIntent();
        if (intent.hasExtra("title") && intent.hasExtra("content")) {
            editTextTitle.setText(intent.getStringExtra("title"));
            editTextContent.setText(intent.getStringExtra("content"));
            notePosition = intent.getIntExtra("position", -1);
        }

        btnSaveNote.setOnClickListener((View v) -> {
            String title = editTextTitle.getText().toString();
            String content = editTextContent.getText().toString();

            // Update, send data
            Intent resultIntent = new Intent();
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("content", content);
            resultIntent.putExtra("position", notePosition);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
