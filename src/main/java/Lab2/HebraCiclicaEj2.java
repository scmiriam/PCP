package Lab2;

public class HebraCiclicaEj2 extends Thread{
    private int id, numHebras, n;
    private double vectorX[], vectorY[];

    public HebraCiclicaEj2(int id, int numHebras, int n, double vectorX[], double vectorY[]) {
        super();
        this.id = id;
        this.numHebras = numHebras;
        this.n = n;
        this.vectorX = vectorX;
        this.vectorY = vectorY;
    }

    @Override
    public void run() {
        for (int i=id; i<n; i+=numHebras) {
           vectorY[ i ] = EjemploFuncionCostosa.evaluaFuncion( vectorX[ i ] );
           //vectorY[ i ] = EjemploFuncionSencilla.evaluaFuncion( vectorX[ i ] );
        }
    }
}
