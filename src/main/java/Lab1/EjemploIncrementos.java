package Lab1;

// ============================================================================
class CuentaIncrementos {
// ============================================================================
  long contador = 0;

  // --------------------------------------------------------------------------
  void incrementaContador() {
    contador++;
  }

  // --------------------------------------------------------------------------
  long dameContador() {
    return( contador );
  }
}


// ============================================================================
class MiHebra extends Thread {
  private int miId;
  private CuentaIncrementos cuenta;

  public MiHebra(int miId, CuentaIncrementos cuenta) {
    this.miId = miId;
    this.cuenta = cuenta;
  }

  public void run() {
    System.out.println("Hebra: " + miId + " iniciandose");

    for (int i = 0; i < 1000000; i++) {
      cuenta.incrementaContador();
    }

    System.out.println("Hebra: " + miId + " acabado");
  }
}

// ============================================================================
class EjemploIncrementos {
// ============================================================================

  // --------------------------------------------------------------------------
  public static void main( String args[] ) {
    int  numHebras;

    // Comprobacion y extraccion de los argumentos de entrada.
    if( args.length != 1 ) {
      System.err.println( "Uso: java programa <numHebras>" );
      System.exit( -1 );
    }
    try {
      numHebras = Integer.parseInt( args[ 0 ] );
    } catch( NumberFormatException ex ) {
      numHebras = -1;
      System.out.println( "ERROR: Argumentos numericos incorrectos." );
      System.exit( -1 );
    }
    System.out.println( "numHebras: " + numHebras );

    // --------  INCLUIR NUEVO CODIGO A CONTINUACION --------------------------
    // ...
    CuentaIncrementos cont = new CuentaIncrementos();
    System.out.println("Valor inicial del contador: " + cont.contador);
    MiHebra[] hebras = new MiHebra[numHebras];
    for (int i = 0; i < numHebras; i++) {
      hebras[i] = new MiHebra(i, cont);
      hebras[i].start();
    }

    for (int i = 0; i < numHebras; i++) {
      try {
        hebras[i].join();
      } catch (InterruptedException ex) {
        System.err.println("Error esperando a la hebra: " + i);
        System.exit(-1);
      }
    }


    System.out.println("Valor final del contador: " + cont.dameContador());

  }
}

