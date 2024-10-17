package Lab2;

public class HebraCíclicaEj1 extends Thread{
    private int id, numHebras, n;

    public HebraCíclicaEj1(int id, int numHebras, int n) {
        super();
        this.id = id;
        this.numHebras = numHebras;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i=id; i<n; i+=numHebras) {
            System.out.println("Hebra "+id+": "+i);
        }
    }
}
