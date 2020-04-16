package iti.intake40.tritra.notes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import iti.intake40.tritra.R;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.NoteModel;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.HolderNode> {
    Context _context;
    List<NoteModel>notes;
    NotesContract.PresenterInterface changeInterface;

    public NoteAdapter(Context _context, List<NoteModel> notes, NotesContract.PresenterInterface notePresnter) {
        this._context = _context;
        this.notes = notes;
        this.changeInterface = notePresnter;
    }

    @NonNull
    @Override
    public HolderNode onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.custom_note_row,parent,false);
        HolderNode holderNode=new HolderNode(view);
        return holderNode;
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderNode holder, final int position) {


        holder.notetxt.setText(notes.get(position).getNote());



        holder.notetxt.addTextChangedListener(
                new TextWatcher() {
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                        holder.layout.setBackgroundColor(Color.parseColor("#FFACD2E4"));
                       /* if (TextUtils.isEmpty(holder.notetxt.getText().toString())) {
                            holder.notetxt.setError("fill empty field");
                        }*/

                    }
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }


                    private Timer timer=new Timer();
                    private final long DELAY = 1000; // milliseconds

                    @Override
                    public void afterTextChanged(final Editable s) {
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {

                                        if (TextUtils.isEmpty(holder.notetxt.getText().toString())) {
                                            notes.get(position).getId();
                                            changeInterface.notifyDelete(notes.get(position), position);

                                        }else {
                                        notes.get(position).setNote(holder.notetxt.getText().toString());
                                        changeInterface.notifyUpdate(notes.get(position), position);}


                                        // TODO: do what you need here (refresh list)
                                        // you will probably need to use runOnUiThread(Runnable action) for some specific actions (e.g. manipulating views)
                                    }
                                },
                                DELAY

                        );

                    }
                }
        );


        holder.noteimbtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             notes.get(position).getId();
             changeInterface.notifyDelete(notes.get(position),position);


         }
     });

     if(notes.get(position).getStatus().equals(NoteModel.STATUS.TODO)) {
         holder.notecheckbox.setChecked(false);
         holder.notetxt.setEnabled(true);

     }

     else {

         holder.notetxt.setEnabled(false);
         holder.notecheckbox.setChecked(true);
         //holder.layout.setBackgroundColor(Color.parseColor("#808080"));
     }

     holder.notecheckbox.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if(holder.notecheckbox.isChecked()){

                 holder.notetxt.setEnabled(false);
                 notes.get(position).setStatus(NoteModel.STATUS.DONE);
                 changeInterface.notifyUpdate(notes.get(position),position);


             }else{
                 notes.get(position).setStatus(NoteModel.STATUS.TODO);
                 changeInterface.notifyUpdate(notes.get(position),position);


             }
         }
     });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class HolderNode extends RecyclerView.ViewHolder {
        private EditText notetxt;
        private ImageButton noteimbtn;
        private CheckBox notecheckbox;
        LinearLayout layout;

        public HolderNode(@NonNull View itemView) {
            super(itemView);
            notetxt=itemView.findViewById(R.id.notetxt);
            noteimbtn=itemView.findViewById(R.id.deletebtn);
            notecheckbox=itemView.findViewById(R.id.notecheckbox);
            layout= itemView.findViewById(R.id.horizontalnote);
        }
    }

}
