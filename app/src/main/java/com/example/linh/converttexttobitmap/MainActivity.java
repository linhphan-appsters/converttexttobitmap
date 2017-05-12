package com.example.linh.converttexttobitmap;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    final static String TAG = "MainActivity";

    @BindView(R.id.edt)
    EditText mEdt;
    @BindView(R.id.txt)
    TextView mTxt;
    @BindView(R.id.img)
    ImageView mImg;
    @BindView(R.id.btn)
    Button mBtn;

    private android.widget.FrameLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        mTxt.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                takeTextCapture();
//            }
//        });

        mTxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(mTxt);
                v.startDrag(dragData, myShadow, null, 0);
                return true;
            }
        });

        mTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(mTxt);
                    mTxt.startDrag(data, shadowBuilder, mTxt, 0);
//                    mTxt.setVisibility(View.INVISIBLE);
                    return true;
                }

                return false;
            }
        });

        mTxt.setOnDragListener(new View.OnDragListener() {
            int x_cord;
            int y_cord;
            int deltaX;
            int deltaY;
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (FrameLayout.LayoutParams)v.getLayoutParams();
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_STARTED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        deltaX = x_cord - v.getLeft();
                        deltaY = y_cord - v.getTop();
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_EXITED");
                        Log.d(TAG, "leftMargin "+ x_cord);
                        Log.d(TAG, "topMargin "+ y_cord);
                        v.requestLayout();
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        Log.d(TAG, "x_cord " + x_cord);
                        Log.d(TAG, "y_cord " + y_cord);
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENDED");
                        layoutParams.leftMargin = x_cord - deltaX;
                        layoutParams.topMargin = y_cord;
                        Log.d(TAG, "leftMargin "+ (x_cord - deltaX));
                        Log.d(TAG, "topMargin "+ y_cord);
                        v.setVisibility(View.VISIBLE);
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(TAG, "ACTION_DROP event");

                        // Do nothing
                        break;
                    default: break;
                }
                return true;
            }
        });
    }

    private void takeTextCapture(){
        Log.d("MainActivity", "takeTextCapture");
        mTxt.setDrawingCacheEnabled(true);
        mTxt.buildDrawingCache();
        mImg.setImageBitmap(mTxt.getDrawingCache());
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
//        mEdt.setFocusable(false);
//        mEdt.setDrawingCacheEnabled(true);
//        mEdt.buildDrawingCache();
//        mImg.setImageBitmap(mEdt.getDrawingCache());
//        mEdt.setDrawingCacheEnabled(true);
        mTxt.setText(mEdt.getText().toString());
        mTxt.postDelayed(new Runnable() {
            @Override
            public void run() {
                takeTextCapture();
            }
        }, 100);
    }

    @OnClick(R.id.edt)
    public void onEditClicked(){
        mEdt.setFocusableInTouchMode(true);
        mEdt.setFocusable(true);
        mEdt.requestFocus(); //<<< get Focus on EditText
    }

}
