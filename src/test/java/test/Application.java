package test;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2011-10-12 16:58
 */
public class Application {
    public static void main(String[] args) {
        new Thread(new Periodic(args)).start();
        new Thread(new Random(args)).start();
    }
}
