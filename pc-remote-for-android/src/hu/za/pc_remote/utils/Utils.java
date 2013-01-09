package hu.za.pc_remote.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 9/26/11
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public static boolean HasValue(String s) {
        return s != null && !s.equals("");
    }

//    private boolean isMyServiceRunning() {
//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if ("com.example.MyService".equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }
}
