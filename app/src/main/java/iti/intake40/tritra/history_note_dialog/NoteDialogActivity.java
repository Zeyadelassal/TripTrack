package iti.intake40.tritra.history_note_dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.floating_head.HeadService;
import iti.intake40.tritra.history.HistoryContract;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.NoteModel;
import iti.intake40.tritra.notes.NoteActivity;
import iti.intake40.tritra.notes.NoteAdapter;
import iti.intake40.tritra.notes.NotesContract;
import iti.intake40.tritra.notes.NotesPresenter;


public class NoteDialogActivity extends AppCompatActivity implements HistoryNotesContract.ViewInterface {
    HistoryNotesContract.PresenterInterface presenterInterface;
    Button btnClose;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    String tripid;
    String clickable;
    HistoryNoteAdapter adapter;
    LinearLayout noNotesLayouts;
    public static final String CLICKABLE = "CLICKABLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_note_dialog);
        this.setFinishOnTouchOutside(false);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        btnClose = findViewById(R.id.btn_note_close);
        recyclerView = findViewById(R.id.recycle_nodeHistory);
        noNotesLayouts = findViewById(R.id.no_notes_layout);

        tripid = getIntent().getStringExtra(NoteActivity.TRIP_ID_KEY);
        clickable = getIntent().getStringExtra(CLICKABLE);
        presenterInterface = new HistoryNotesPresenter(this);
        presenterInterface.getAllNotes(tripid);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickable != null){
                    Intent serviceIntent = new Intent(NoteDialogActivity.this, HeadService.class);
                    serviceIntent.putExtra(NoteActivity.TRIP_ID_KEY,tripid);
                    startService(serviceIntent);
                }
                NoteDialogActivity.this.finish();
            }
        });
    }

    @Override
    public void updateNoteList(List<NoteModel> notes) {
        if (notes != null && notes.size() > 0) {
            noNotesLayouts.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            if (getIntent().getStringExtra(CLICKABLE) == null)
                adapter = new HistoryNoteAdapter(getApplicationContext(), notes, this, false);
            else
                adapter = new HistoryNoteAdapter(getApplicationContext(), notes, this, true);
            recyclerView.setAdapter(adapter);
        } else {
            noNotesLayouts.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateNoteStaus(NoteModel noteModel) {
        presenterInterface.updateNoteStaus(noteModel, tripid);
    }

    @Override
    public void onBackPressed() {
        if(clickable != null){
            Intent serviceIntent = new Intent(NoteDialogActivity.this, HeadService.class);
            serviceIntent.putExtra(NoteActivity.TRIP_ID_KEY,tripid);
            startService(serviceIntent);
        }
        NoteDialogActivity.this.finish();
    }
}
