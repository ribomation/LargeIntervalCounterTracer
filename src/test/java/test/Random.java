package test;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Iterator;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Functional test of the tracer.
 *
 * @author: jriboe
 * @created: 19-Oct-2006, 21:52:47
 */
public class Random implements Runnable {
    public static void main(String[] args) {
        Random app = new Random(args);
        app.run();
    }

    private long        duration = Integer.MAX_VALUE, endTime;
    private java.util.Random r = new java.util.Random();
    private Timer       timer = new Timer();
    private LinkedList        ts = new LinkedList();

    public Random(String[] args) {
        System.out.println("[Random] started");
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.startsWith("-d")) {
                duration = Integer.parseInt( args[++i] );
            } else {
                throw new RuntimeException("Illegal option: "+arg);
            }
        }
        endTime = System.currentTimeMillis() + duration;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long    now = System.currentTimeMillis();
                long    end_15s = now - 15 * 1000;
                long    end_1m = now - 60 * 1000;
                long    end_5m = now - 6 * 60 * 1000;
                int     cnt_15s = 0;
                int     cnt_1m = 0;
                int     cnt_5m = 0;
                boolean hasIncr = true;

                for (Iterator it = ts.iterator(); it.hasNext() && hasIncr; ) {
                    hasIncr = false;
                    long  t = ((Long)it.next()).longValue();
                    if (end_15s <= t) {cnt_15s++; hasIncr=true;}
                    if (end_1m  <= t) {cnt_1m++;  hasIncr=true;}
                    if (end_5m  <= t) {cnt_5m++;  hasIncr=true;}
                }

                String  xNow = new SimpleDateFormat("HH:mm:ss").format(new Date(now));
                String  x15s = new SimpleDateFormat("HH:mm:ss").format(new Date(end_15s));
                String  x1m = new SimpleDateFormat("HH:mm:ss").format(new Date(end_1m));
                String  x5m = new SimpleDateFormat("HH:mm:ss").format(new Date(end_5m));
                System.out.println("[Random] " + xNow + " - 15s/"+x15s+"=" + cnt_15s + ", 1m/"+x1m+"=" + cnt_1m + ", 5m/"+x5m+"=" + cnt_5m);
            }
        }, 0, 15000);
    }

    public void     doit() {
        long    now = System.currentTimeMillis();
        ts.addFirst( new Long(now) );
        String  nowTxt = new SimpleDateFormat("HH:mm:ss").format(new Date(now));
        System.out.println("[Random] " + nowTxt + " - DOIT");
    }

    public void     run() {
        while (System.currentTimeMillis() < endTime) {
            int  numCalls = r.nextInt(4);
            for (int k=1; k<=numCalls; ++k) {
                doit();
                sleep(50);
            }

            int numSecs = 1 + r.nextInt(60);
            sleep(numSecs * 1000);
        }
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {}
    }

}
