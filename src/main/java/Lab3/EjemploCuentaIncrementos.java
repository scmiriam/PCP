package Lab3;

// ============================================================================
class CuentaIncrementos {
// ============================================================================
  int numIncrementos = 0;

  // --------------------------------------------------------------------------
  void incrementaNumIncrementos() {
    numIncrementos++;
  }

  // --------------------------------------------------------------------------
  int dameNumIncrementos() {
    return( numIncrementos );
  }
}


// ============================================================================
class MiHebra extends Thread {
// ============================================================================
  int                numIters;
  CuentaIncrementosVolatile c;

  // --------------------------------------------------------------------------
  public MiHebra( int numIters, CuentaIncrementosVolatile c ) {
    this.numIters = numIters;
    this.c        = c;
  }

  // --------------------------------------------------------------------------
  public void run() {
    for( int i = 0; i < numIters; i++ ) {
      c.incrementaNumIncrementos();
    }
  }
}

// ============================================================================
class EjemploCuentaIncrementos {
// ============================================================================

  // --------------------------------------------------------------------------
  public static void main( String args[] ) {
    long    t1, t2;
    double  tt;
    int     numHebras, numIters;

    // Comprobacion y extraccion de los argumentos de entrada.
    if( args.length != 2 ) {
      System.err.println( "Uso: java programa <numHebras> <numIters>" );
      System.exit( -1 );
    }
    try {
      numHebras = Integer.parseInt( args[ 0 ] );
      numIters  = Integer.parseInt( args[ 1 ] );
    } catch( NumberFormatException ex ) {
      numHebras = -1;
      numIters  = -1;
      System.out.println( "ERROR: Argumentos numericos incorrectos." );
      System.exit( -1 );
    }

    System.out.println( "numHebras: " + numHebras );
    System.out.println( "numIters : " + numIters );
    
    System.out.println( "Creando y arrancando " + numHebras + " hebras." );
    t1 = System.nanoTime();
    MiHebraVolatile v[] = new MiHebraVolatile[ numHebras ];
    CuentaIncrementosVolatile c = new CuentaIncrementosVolatile();
    for( int i = 0; i < numHebras; i++ ) {
      v[ i ] = new MiHebraVolatile( numIters, c );
      v[ i ].start();
    }
    for( int i = 0; i < numHebras; i++ ) {
      try {
        v[ i ].join();
      } catch( InterruptedException ex ) {
        ex.printStackTrace();
      }
    }
    t2 = System.nanoTime();
    tt = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Total de incrementos: " + c.dameNumIncrementos() );
    System.out.println( "Tiempo transcurrido en segs.: " + tt );
  }
}

