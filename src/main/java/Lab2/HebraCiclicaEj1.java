package Lab2;

public class HebraCiclicaEj1 extends Thread{
    private int id, numHebras, n;

    public HebraCiclicaEj1(int id, int numHebras, int n) {
        super();
        this.id = id;
        this.numHebras = numHebras;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i=id; i<n; i+=numHebras) {
            System.out.println(i);
        }
    }
}
