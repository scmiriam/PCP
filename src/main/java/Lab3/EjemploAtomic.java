package Lab3;

import java.util.concurrent.atomic.AtomicInteger;

// ============================================================================
class MiHebraAtomic extends Thread {
// ============================================================================
  int                numIters;
  AtomicInteger      numIncrementos;

  // --------------------------------------------------------------------------
  public MiHebraAtomic(int numIters,  AtomicInteger numIncrementos ) {
    this.numIters      = numIters;
    this.numIncrementos= numIncrementos;
  }

  // --------------------------------------------------------------------------
  public void run() {
    for( int i = 0; i < numIters; i++ ) {
      numIncrementos.getAndIncrement();
    }
  }
}

// ============================================================================
class EjemploCuentaIncrementosAtomic {
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
    MiHebraAtomic v[] = new MiHebraAtomic[numHebras];
    AtomicInteger numInc = new AtomicInteger(0);
    for( int i = 0; i < numHebras; i++ ) {
      v[ i ] = new MiHebraAtomic( numIters, numInc );
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
    System.out.println( "Total de incrementos: " + numInc );
    System.out.println( "Tiempo transcurrido en segs.: " + tt );
  }
}

