package Logic;

public class test implements Runnable {
    private int i;

    public test(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.i * 10; i++)
            System.out.println(i + ".draadje" + this.i);
    }

    public static void main(String[] args) {
        test draadje1 = new test(100);
        test draadje2 = new test(200);
        Thread thread1 = new Thread(draadje1);
        Thread thread2 = new Thread(draadje2);
        thread1.start();
        thread2.start();
    }
}