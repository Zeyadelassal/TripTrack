package iti.intake40.tritra.history_note_dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.model.NoteModel;

public class HistoryNoteAdapter extends RecyclerView.Adapter<HistoryNoteAdapter.HolderNode> {
    Context _context;
    List<NoteModel> notes;
    HistoryNotesContract.ViewInterface viewInterface;
    boolean clickable;

    public HistoryNoteAdapter(Context _context, List<NoteModel> notes, HistoryNotesContract.ViewInterface viewInterface, boolean clickable) {
        this._context = _context;
        this.notes = notes;
        this.viewInterface = viewInterface;
        this.clickable = clickable;
    }

    @NonNull
    @Override
    public HolderNode onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_note_history, parent, false);
        HolderNode holderNode = new HolderNode(view);
        return holderNode;
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderNode holder, final int position) {
        holder.notetxt.setText(notes.get(position).getNote());
        System.out.println("SOUT= " + notes.get(position).getNote());
        if (notes.get(position).getStatus().equals(NoteModel.STATUS.TODO))
            holder.notecheckbox.setChecked(false);
        else
            holder.notecheckbox.setChecked(true);

        holder.notetxt.setMovementMethod(new ScrollingMovementMethod());
        holder.notetxt.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        if (clickable)
            holder.notecheckbox.setClickable(true);

        holder.notecheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    notes.get(position).setStatus(NoteModel.STATUS.DONE);
                else
                    notes.get(position).setStatus(NoteModel.STATUS.TODO);
                viewInterface.updateNoteStaus(notes.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class HolderNode extends RecyclerView.ViewHolder {
        private MaterialTextView notetxt;
        private CheckBox notecheckbox;

        public HolderNode(@NonNull View itemView) {
            super(itemView);
            notetxt = itemView.findViewById(R.id.notetxt);
            notecheckbox = itemView.findViewById(R.id.notecheckbox);
        }
    }


}
