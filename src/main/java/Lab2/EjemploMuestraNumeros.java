package Lab2;

// ============================================================================
class EjemploMuestraNumeros {
// ============================================================================

  // --------------------------------------------------------------------------
  public static void main( String args[] ) {
    int  n, numHebras;

    // Comprobacion y extraccion de los argumentos de entrada.
    if( args.length != 2 ) {
      System.err.println( "Uso: java programa <numHebras> <n>" );
      System.exit( -1 );
    }
    try {
      numHebras = Integer.parseInt( args[ 0 ] );
      n         = Integer.parseInt( args[ 1 ] );
    } catch( NumberFormatException ex ) {
      numHebras = -1;
      n         = -1;
      System.out.println( "ERROR: Argumentos numericos incorrectos." );
      System.exit( -1 );
    }
    //
    // Implementacion paralela con distribucion ciclica o por bloques.

    //Reparto ciclico
    //Thread[] hebras = new HebraCiclicaEj1[numHebras];

    //Reparto por bloques
    Thread[] hebras = new HebraBloquesEj1[numHebras];

    // Crea un vector de hebras. Crea y arranca las hebras
    for (int i=0; i<numHebras; i++) {
      //Reparto ciclico
      //hebras[i] = new HebraCiclicaEj1(i, numHebras, n);

      //Reparto bloques
      hebras[i] = new HebraBloquesEj1(i, numHebras, n);

      hebras[i].start();
    }
    // Espera a que terminen todas las hebras.
    // (B) ...
    for (int i = 0; i < numHebras; i++) {
      try {
        hebras[i].join();
      } catch (InterruptedException ex) {
        System.err.println("Error esperando a la hebra: " + i);
        System.exit(-1);
      }
    }
  }
}

// Crea las clases adicionales que sean necesarias
// (C) ... 

class HebraBloquesEj1 extends Thread {
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

class HebraCiclicaEj1 extends Thread{
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



