package test;

/**
 * Functional test of the tracer.
 *
 * @author: jriboe
 * @created: 19-Oct-2006, 21:52:47
 */
public class Periodic implements Runnable {
    public static void main(String[] args) {
        Periodic app = new Periodic(args);
        app.run();
    }

    private long        puls = 1000, duration = Integer.MAX_VALUE, endTime;

    public Periodic(String[] args) {
        System.out.println("[Periodic] start");
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.startsWith("-p")) {
                puls = Integer.parseInt( args[++i] );
            } else if (arg.startsWith("-d")) {
                duration = Integer.parseInt( args[++i] );
            } else {
                throw new RuntimeException("Illegal option: "+arg);
            }
        }
        endTime = System.currentTimeMillis() + duration;
    }

    public void     doit() {
        System.out.println("[Periodic] puls");
    }

    public void     run() {
        while (System.currentTimeMillis() < endTime) {
            doit();
            sleep(puls);
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {}
    }

}
