package Lab1;

class MiHebra2 extends Thread {
  int miId;
  int num1;
  int num2;

  public MiHebra2( int miId, int num1, int num2 ) {
    this.miId=miId;
    this.num1 = num1;
    this.num2 = num2;
  }

  public void run() {
    long suma = 0;

    System.out.println( "Hebra Auxiliar " + miId + " , inicia calculo" );
    for( int i = num1; i <= num2 ; i++ ) {
      suma += (long) i;
    }
    System.out.println( "Hebra Auxiliar " + miId + " , suma: " + suma);
  }

}

class EjemploDaemon {
  public static void main( String args[] ) {
    System.out.println( "Hebra Principal inicia" );
    // ... (C)
    Thread t0 = Thread.startVirtualThread(new MiHebra2(0, 1, 1000000));
    Thread t1 = Thread.startVirtualThread(new MiHebra2(1, 1, 1000000));
    /*MiHebra t0 = new MiHebra(0, 1 , 1000000);
    MiHebra t1 = new MiHebra(1, 1,  1000000);
    t0.start();
    t1.start();*/
    // ... (D)

    try {
      t0.join();
      t1.join();
    }catch( InterruptedException ex ) {
      ex.printStackTrace();
      System.out.println("Hebra Principal finaliza");
    }
  }
}


