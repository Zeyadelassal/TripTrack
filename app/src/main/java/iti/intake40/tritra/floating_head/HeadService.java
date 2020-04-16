package iti.intake40.tritra.floating_head;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.history_note_dialog.NoteDialogActivity;
import iti.intake40.tritra.model.NoteModel;
import iti.intake40.tritra.notes.NoteActivity;
import iti.intake40.tritra.notes.NoteAdapter;
import iti.intake40.tritra.notes.NotesContract;
import iti.intake40.tritra.notes.NotesPresenter;

public class HeadService extends Service implements View.OnClickListener {

    private WindowManager mWindowManager;
    private View floatingView;
    private View collapsedView;
    private int layoutParams;

    NotesContract.PresenterInterface presenterInterface;
    String tripid;
    ArrayList<NoteModel> notes;
    NoteAdapter adapter;

    public HeadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
            tripid = intent.getStringExtra(NoteActivity.TRIP_ID_KEY);
            notes = new ArrayList<>();

            //getting the widget layout from xml using layout inflater
            floatingView = LayoutInflater.from(this).inflate(R.layout.head_icon, null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ;
            }else{
                layoutParams = WindowManager.LayoutParams.TYPE_PHONE;
            }
            //setting the layout parameters
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    layoutParams,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            //Specify the view position
            params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
            params.x = 0;
            params.y = 100;


            //getting windows services and adding the floating view to it
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(floatingView, params);


            //getting the collapsed and expanded view from the floating view
            collapsedView = floatingView.findViewById(R.id.layoutCollapsed);

            //adding click listener to close button and expanded view
            floatingView.findViewById(R.id.buttonClose).setOnClickListener(this);

            //adding an touchlistener to make drag movement of the floating widget
            floatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //remember the initial position.
                            initialX = params.x;
                            initialY = params.y;
                            //get the touch location
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            //Update the layout with new X & Y coordinate
                            mWindowManager.updateViewLayout(floatingView, params);
                            return true;

                        case MotionEvent.ACTION_UP:
                            int Xdiff = (int) (event.getRawX() - initialTouchX);
                            int Ydiff = (int) (event.getRawY() - initialTouchY);
                            //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                            //So that is click event.
                            if (Xdiff < 10 && Ydiff < 10) {
                                if (isViewCollapsed()) {
                                    //When user clicks on the image view of the collapsed layout,
                                    //visibility of the collapsed layout will be changed to "View.GONE"
                                    //and expanded view will become visible.
                                    collapsedView.setVisibility(View.GONE);
//                                expandedView.setVisibility(View.VISIBLE);
////                                //presenterInterface.getAllNotes(tripid);
////                                notes.add(new NoteModel("1","Zeyad","DONE"));
////                                updateNoteList(notes);
                                    Intent notesIntent = new Intent(HeadService.this, NoteDialogActivity.class);
                                    notesIntent.putExtra(NoteActivity.TRIP_ID_KEY,intent.getStringExtra(NoteActivity.TRIP_ID_KEY));
                                    notesIntent.putExtra(NoteDialogActivity.CLICKABLE,"Click");
                                    notesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(notesIntent);
                                    stopSelf();
                                }
                            }
                            return true;
                    }
                    return false;
                }
            });
        return super.onStartCommand(intent, flags, startId);
    }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (floatingView != null) mWindowManager.removeView(floatingView);
        }

        private boolean isViewCollapsed() {
            return floatingView == null || floatingView.findViewById(R.id.layoutCollapsed).getVisibility() == View.VISIBLE;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonClose:
                    //closing the widget
                    stopSelf();
                    break;
            }
        }
    }

