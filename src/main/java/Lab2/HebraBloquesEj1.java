package Lab2;

public class HebraBloquesEj1 extends Thread {
    private int id, numHebras, n;

    public HebraBloquesEj1(int id, int numHebras, int n) {
        super();
        this.id = id;
        this.numHebras = numHebras;
        this.n = n;
    }
    @Override
    public void run() {
        int tamanyo = (n + numHebras - 1) / numHebras;
        int inicio = id * tamanyo;
        int fin = Math.min(inicio + tamanyo, n);

        for (int i=inicio; i<fin; i++) {
            System.out.println(i);
        }
    }
}
