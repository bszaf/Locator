package com.example.bartoszszafran.locator;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AddMeasurementsActivity extends AppCompatActivity {
    AddMeasurementsActivity.MyView mv;
    AlertDialog dialog;
    BuildingArchive buildingArchive = App.getBuildingArchive();
    BuildingTopology newBuilding = new BuildingTopology();
    HashMap<Position, List<ShortScanResult>> scans = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        newBuilding.buildingScheme = bmp;

        mv = new AddMeasurementsActivity.MyView(this, bmp);
        mv.setDrawingCacheEnabled(true);
        setContentView(mv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
    }

    private Paint mPaint;

    public class MyView extends View {

        private static final float MINP = 0.25f;
        private static final float MAXP = 0.75f;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Paint   mBitmapPaint;
        Context context;

        public MyView(Context c, Bitmap bmp) {
            super(c);
            context=c;
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            mBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
            mCanvas = new Canvas(mBitmap);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        }

        private void touch(final float x, final float y) {
            //showDialog();
            Log.d("ui", "Touched at" + " x:  " + x + "  y: " + y);
            mCanvas.drawCircle(x, y, 10, mPaint);
            final WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);

            // Used for handling results of the scan
            final BroadcastReceiver br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    List<ScanResult> results = wifiManager.getScanResults();
                    Position p = new Position(x, y);
                    List<ShortScanResult> r  = new LinkedList<>();
                    for (ScanResult s : results) {
                        ShortScanResult elem = new ShortScanResult();
                        elem.BSSID = s.BSSID;
                        elem.level = s.level;
                        r.add(elem);
                    }
                    scans.put(p, r);
                    context.unregisterReceiver(this);
                }
            };
            context.registerReceiver(br, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifiManager.startScan();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch(x, y);
                    invalidate();
                    break;
            }
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_measurements_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case R.id.add_measurements_action_revert:
                Log.d("add measurements", "Undo");
                return true;
            case R.id.add_measurements_action_next:
                Log.d("add measurements", "Next");
                AlertDialog.Builder editalert = new AlertDialog.Builder(AddMeasurementsActivity.this);
                editalert.setTitle("Please Enter the name of the building");
                final EditText input = new EditText(AddMeasurementsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                editalert.setView(input);
                editalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = input.getText().toString();
                        newBuilding.positionToRouterSignalStrength = scans;
                        buildingArchive.addBuilding(name, newBuilding);
                        finish();
                    }
                });
                editalert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
