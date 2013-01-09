package hu.za.pc_remote.transport;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import hu.za.pc_remote.common.RCAction;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 9/26/11
 * Time: 8:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransportManager {
    private static final String tag = "TransportManager";
    private WorkerThread workerThread = null;

    public TransportManager(ObjectOutputStream oos) {
        workerThread = new WorkerThread(oos);
        workerThread.start();
    }

    public void Send(RCAction action) {
        if (workerThread != null && workerThread.isAlive()) {
            workerThread.queue.offer(action);
        }
    }

    public boolean isAlive() {
        return workerThread != null && workerThread.isAlive();
    }

    public void Stop(){
         if(this.workerThread != null){
             this.workerThread.running = false;
         }
    }

    private class WorkerThread extends Thread {

        public WorkerThread(ObjectOutputStream oos) {
            this.oos = oos;
        }

        private static final String tag = "BTThread";
        private ObjectOutputStream oos = null;
        private boolean running = true;
        public ArrayBlockingQueue<RCAction> queue = new ArrayBlockingQueue<RCAction>(10);

        @Override
        public void run() {
            while (running) {
                try {
                    RCAction action = queue.take();
                    if (oos != null) {
                        oos.writeObject(action);
                    } else {
                        Log.w(tag, "ObjectOutputStream is null exiting run loop");
                        break;
                    }
                } catch (InterruptedException e) {
                    Log.w(tag, e.getMessage());
                } catch (IOException ioe) {
                    Log.e(tag, "Failed to write ObjectOutputStream:", ioe);
                    break;
                }
            }
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
