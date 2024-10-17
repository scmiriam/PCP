package Lab1;

class MiHebra1 extends Thread {
  int miId;
  public MiHebra1( int miId ) {
    this.miId = miId;
  }
  public void run() {
    for( int i = 0; i < 1000; i++ ) {
      System.out.println( "Hebra: " + miId );
    }
  }
}
class EjemploCreacionThread {
  public static void main( String args[] ) {
    new MiHebra1(0).start();
    new MiHebra1(1).start();
    /*
    new MiHebra(0).run();
    new MiHebra(1).run();
    */
  }
}
