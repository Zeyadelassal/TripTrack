package iti.intake40.tritra.home;

import android.content.Context;
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
import iti.intake40.tritra.model.TripModel;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<TripModel> tripsList;

    private CardMenuInterface cardMenuInterface;

    public RecyclerAdapter(List<TripModel> tripsList, CardMenuInterface menuInterface) {
        this.tripsList = tripsList;
        this.cardMenuInterface = menuInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = layoutInflater.inflate(R.layout.card_trip,parent,false);
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

        holder.btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(holder.layout.getContext(), "btnNotes", Toast.LENGTH_SHORT).show();
                cardMenuInterface.openNotes(tripsList.get(position).getId());
            }
        });

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(holder.layout.getContext(), "btnMenu", Toast.LENGTH_SHORT).show();
                cardMenuInterface.onPopupMenuClick(v,position);
            }
        });

        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(holder.layout.getContext(), "btnStart", Toast.LENGTH_SHORT).show();
                cardMenuInterface.startTrip(tripsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        View layout;
        Button btnStart;
        ImageButton btnMenu;
        ImageButton btnNotes;
        TextView txtTripName;
        TextView txtTripType;
        TextView txtTripDate;
        TextView txtTripTime;
        TextView txtTripStartPoint;
        TextView txtTripEndPoint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            btnStart = itemView.findViewById(R.id.card_btn_start);
            btnMenu = itemView.findViewById(R.id.card_imgbtn_menu);
            btnNotes = itemView.findViewById(R.id.card_imgbtn_notes);
            txtTripDate = itemView.findViewById(R.id.card_txt_date);
            txtTripTime = itemView.findViewById(R.id.card_txt_time);
            txtTripEndPoint = itemView.findViewById(R.id.card_txt_trip_end_point);
            txtTripStartPoint = itemView.findViewById(R.id.card_txt_trip_start_point);
            txtTripName = itemView.findViewById(R.id.card_txt_trip_name);
            txtTripType = itemView.findViewById(R.id.card_txt_trip_type);
        }
    }


}
