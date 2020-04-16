package iti.intake40.tritra.history;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.home.CardMenuInterface;
import iti.intake40.tritra.model.TripModel;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {
    NoteInterface noteInterface;
    private List<TripModel> tripsList;

    public HistoryRecyclerAdapter(List<TripModel> tripsList, NoteInterface noteInterface) {
        this.tripsList = tripsList;
        this.noteInterface = noteInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = layoutInflater.inflate(R.layout.card_trip_history,parent,false);
        ViewHolder viewHolder =new ViewHolder(root);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtTripName.setText(tripsList.get(position).getName());
        holder.txtTripType.setText(tripsList.get(position).getType());
        holder.txtTripTime.setText(tripsList.get(position).getTime());
        String[] date = tripsList.get(position).getDate().split("-");
        int tripYear =Integer.parseInt(date[0]);
        int tripMonth = Integer.parseInt(date[1])+1;
        int tripDay = Integer.parseInt(date[2]);
        holder.txtTripDate.setText(tripYear+"-"+tripMonth+"-"+tripDay);
        holder.txtTripStartPoint.setText(tripsList.get(position).getStartPoint());
        holder.txtTripEndPoint.setText(tripsList.get(position).getEndPoint());
        holder.txtTripStatus.setText(tripsList.get(position).getStatus());

        holder.btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(holder.layout.getContext(), "btnNotesHIS", Toast.LENGTH_SHORT).show();
                noteInterface.openNote(tripsList.get(position).getId());

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(holder.layout.getContext(), "btnDelete", Toast.LENGTH_SHORT).show();
                noteInterface.deleteTrip(tripsList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        View layout;
        ImageButton btnDelete;
        ImageButton btnNotes;
        TextView txtTripName;
        TextView txtTripType;
        TextView txtTripDate;
        TextView txtTripTime;
        TextView txtTripStartPoint;
        TextView txtTripEndPoint;
        TextView txtTripStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            btnDelete = itemView.findViewById(R.id.card_imgbtn_delete);
            btnNotes = itemView.findViewById(R.id.card_imgbtn_notes);
            txtTripDate = itemView.findViewById(R.id.card_txt_date);
            txtTripTime = itemView.findViewById(R.id.card_txt_time);
            txtTripEndPoint = itemView.findViewById(R.id.card_txt_trip_end_point);
            txtTripStartPoint = itemView.findViewById(R.id.card_txt_trip_start_point);
            txtTripName = itemView.findViewById(R.id.card_txt_trip_name);
            txtTripType = itemView.findViewById(R.id.card_txt_trip_type);
            txtTripStatus = itemView.findViewById(R.id.card_txt_trip_status);
        }
    }


}
