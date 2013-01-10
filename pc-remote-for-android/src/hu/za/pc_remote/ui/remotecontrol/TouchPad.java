package hu.za.pc_remote.ui.remotecontrol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.IBinder;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import hu.za.pc_remote.common.RCAction;
import hu.za.pc_remote.transport.ConnectionHandlingService;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/12/11
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TouchPad extends SurfaceView implements SurfaceHolder.Callback {
    Context context;
    GestureDetector gestureDetector;
    Drawer drawer;
    Sender sender;

    float x, y, vx, vy, dx, dy;
    static final float a = 50;
    private float sensitivity = (float) -0.5;
    Paint paint;

    public TouchPad(Context context) {
        super(context);
        this.context = context;

        gestureDetector = new GestureDetector(new TouchpadGestureListener());
        x = 100;
        y = 100;
        paint = new Paint();
        paint.setColor(Color.BLUE);

        getHolder().addCallback(this);
        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawer = new Drawer();
        drawer.start();

        sender = new Sender();
        sender.doBindService();
        sender.start();
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        sender.running = drawer.running = false;
    }

    private class TouchpadGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            vx += velocityX;
            vy += velocityY;

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float _dx, float _dy) {
            x -= _dx;
            y -= _dy;

            dx += _dx;
            dy += _dy;

            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            x = e.getX();
            y = e.getY();

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Intent i = new Intent(ConnectionHandlingService.RC_INTENT_ACTION);
            RCAction extra = new RCAction();
            extra.type = RCAction.Type.MOUSE_CLICK;
            extra.arguments = new Integer[]{new Integer(1)};
            i.putExtra(ConnectionHandlingService.INTENT_DATA_EXTRA_KEY, extra);
            context.sendBroadcast(i);

            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            for(int k=0;k<2;k++){
                Intent i = new Intent(ConnectionHandlingService.RC_INTENT_ACTION);
                RCAction extra = new RCAction();
                extra.type = RCAction.Type.MOUSE_CLICK;
                extra.arguments = new Integer[]{new Integer(1)};
                i.putExtra(ConnectionHandlingService.INTENT_DATA_EXTRA_KEY, extra);

                context.sendBroadcast(i);
            }
            return true;
        }
    }

    private class Sender extends Thread {
        public boolean running = true;

        private ConnectionHandlingService mConnService;

        private ServiceConnection mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                mConnService = ((ConnectionHandlingService.LocalBinder) service).getService();
            }

            public void onServiceDisconnected(ComponentName className) {
                mConnService = null;
            }
        };

        public void doBindService(){
            context.bindService(
                    new Intent(context, ConnectionHandlingService.class),
                    mConnection, Context.BIND_AUTO_CREATE);
        }

        void doUnbindService() {
            if (mConnService != null) {
                context.unbindService(mConnection);
            }
        }

        public void run() {

            while (running) {
                if (dx != 0 || dy != 0) {
                    //Intent i = new Intent(ConnectionHandlingService.RC_INTENT_ACTION);
                    RCAction data = new RCAction();
                    data.type = RCAction.Type.MOUSE_MOVE;
                    data.arguments = new Float[]{sensitivity * dx, sensitivity * dy};

                    if(mConnService != null){
                        mConnService.sendData(data);
                    }

                    //i.putExtra(ConnectionHandlingService.INTENT_DATA_EXTRA_KEY, extra);
                    //context.sendBroadcast(i);
                }
                dx = 0;
                dy = 0;

                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    Log.w("Sender", "Drawer thread interrupted", e);
                }
            }
        }
    }

    private class Drawer extends Thread {
        public boolean running = true;

        public void run() {
            while (running) {
                x += (vx / 100);
                y += (vy / 100);

                vx = nextValue(vx);
                vy = nextValue(vy);

                Canvas c = getHolder().lockCanvas();
                c.drawColor(Color.BLACK);
                c.drawCircle(x, y, 20, paint);
                getHolder().unlockCanvasAndPost(c);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Log.w("hello", "Drawer thread interrupted");
                }
            }
        }

        private float nextValue(float f) {
            if (f == 0)
                return 0;
            float i = f > 0 ? 1 : -1;

            f = f - (a * i);
            if ((f * i) < 0)
                f = 0;
            return f;
        }
    }

}

