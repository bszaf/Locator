package com.example.bartoszszafran.locator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class BuildingActivity extends AppCompatActivity {

    private BuildingActivity.MyView mv;
    private Paint mPaint;
    private BuildingTopology mBuilding;
    private boolean drawKnownfields = false;
    private Position mPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("building");
        Log.d("building activity", "Opening building " + name);

        mBuilding = App.getBuildingArchive().getBuidling(name);

        mv = new BuildingActivity.MyView(this, mBuilding.buildingScheme);
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
            if (drawKnownfields) {
                for (Position pos : mBuilding.positionToRouterSignalStrength.keySet()) {
                    canvas.drawCircle(pos.x, pos.y, 10, mPaint);
                }
            }
            if (mPos != null) {
                canvas.drawCircle(mPos.x, mPos.y, 25, mBitmapPaint);

            }
        }

        private void touch(final float x, final float y) {
            //showDialog();
            Log.d("ui", "Touched at" + " x:  " + x + "  y: " + y);
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

        public void toogleKnown() {
            BuildingActivity.this.drawKnownfields = !BuildingActivity.this.drawKnownfields;
            invalidate();
        }

        public void updatePosition() {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);

            // Used for handling results of the scan
            final BroadcastReceiver br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    List<ScanResult> results = wifiManager.getScanResults();
                    List<ShortScanResult> r  = new LinkedList<>();
                    for (ScanResult s : results) {
                        ShortScanResult elem = new ShortScanResult();
                        elem.BSSID = s.BSSID;
                        elem.level = s.level;
                        r.add(elem);
                    }
                    mPos = Locator.locate(mBuilding.positionToRouterSignalStrength, r);
                    invalidate();
                    context.unregisterReceiver(this);
                }
            };
            context.registerReceiver(br, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifiManager.startScan();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.building_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_building_toggle_known:
                Log.d("building_activity", "Toggle known positions");
                mv.toogleKnown();
                return true;
            case R.id.activity_building_locate:
                Log.d("building_activity", "Update my locate");
                mv.updatePosition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
