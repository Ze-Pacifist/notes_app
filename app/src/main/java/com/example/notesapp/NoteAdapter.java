package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {

    private final Context context;
    private final ArrayList<Note> notes;
    private final OnDeleteClickListener onDeleteClickListener;
    private final OnViewClickListener onViewClickListener;

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }

    public interface OnViewClickListener {
        void onView(int position);
    }

    public NoteAdapter(Context context, ArrayList<Note> notes, OnDeleteClickListener onDeleteClickListener, OnViewClickListener onViewClickListener) {
        super(context, 0, notes);
        this.context = context;
        this.notes = notes;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        }

        Note note = getItem(position);

        TextView textViewTitle = convertView.findViewById(R.id.noteTitle);
        Button btnView = convertView.findViewById(R.id.btnViewNote);
        Button btnDelete = convertView.findViewById(R.id.btnDeleteNote);

        if (note != null) {
            textViewTitle.setText(note.getTitle());
        }

        btnView.setOnClickListener(v -> onViewClickListener.onView(position));

        btnDelete.setOnClickListener(v -> onDeleteClickListener.onDelete(position));

        return convertView;
    }
}
