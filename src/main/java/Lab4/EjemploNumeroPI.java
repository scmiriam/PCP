package Lab4;

import java.util.concurrent.atomic.DoubleAdder;

// ===========================================================================
class Acumula {
// ===========================================================================
  double  suma;
  // -------------------------------------------------------------------------
  Acumula() {
    // ...
      suma=0;
  }
  // -------------------------------------------------------------------------
  synchronized void acumulaDato( double dato ) {
    // ...
      suma+=dato;
  }
  // -------------------------------------------------------------------------
  synchronized double dameDato() {
    // ...
      return suma;
  }
}

// ===========================================================================
class MiHebraMultAcumulaciones extends Thread {
// ===========================================================================
  int      miId, numHebras;
  long     numRectangulos;
  Acumula  a;

  // -------------------------------------------------------------------------
  MiHebraMultAcumulaciones( int miId, int numHebras, long numRectangulos,
                              Acumula a ) {
    // ...
      this.miId = miId;
      this.numHebras = numHebras;
      this.numRectangulos = numRectangulos;
      this.a = a;
  }

  // -------------------------------------------------------------------------
  public void run() {
    // ...
      double x, baseRectangulo = 1.0 / ((double)numRectangulos);
      for (long i = miId; i<numRectangulos; i+=numHebras){
          x = baseRectangulo * (((double)i)+0.5);
          a.acumulaDato(EjemploNumeroPI.f(x));
      }
  }
}

// ===========================================================================
class MiHebraUnaAcumulacion extends Thread {
// ===========================================================================
int      miId, numHebras;
    long     numRectangulos;
    Acumula  a;
    // -------------------------------------------------------------------------
    MiHebraUnaAcumulacion( int miId, int numHebras, long numRectangulos,
                              Acumula a ) {
        this.miId = miId;
        this.numHebras = numHebras;
        this.numRectangulos = numRectangulos;
        this.a = a;
    }
    // -------------------------------------------------------------------------
    public void run() {
        // ...
        double x, baseRectangulo = 1.0 / ((double) numRectangulos);
        // usamos una variable local para no tener que llamar al método acumulaDato synchronized en cada iteración, pues aumenta el t de ejcucion
        double sumaLocal = 0;
        for (long i = miId; i < numRectangulos; i += numHebras) {
            x = baseRectangulo * (((double) i) + 0.5);
            sumaLocal += EjemploNumeroPI.f(x);
        }
        a.acumulaDato(sumaLocal);
    }
}

// ===========================================================================
class MiHebraMultAcumulacionAtomica extends Thread {
// ===========================================================================
    int miId, numHebras;
    long numRectangulos;
    DoubleAdder da;

    MiHebraMultAcumulacionAtomica(int miId, int numHebras, long numRectangulos, DoubleAdder da){
        this.miId = miId;
        this.numHebras = numHebras;
        this.numRectangulos = numRectangulos;
        this.da = da;
    }

    public void run(){
        double x, baseRectangulo = 1.0 / ((double)numRectangulos);
        for (long i = miId; i<numRectangulos; i+=numHebras){
            x = baseRectangulo * (((double)i)+0.5);
            da.add(EjemploNumeroPI.f(x));
        }
    }
}

// ===========================================================================
class MiHebraUnaAcumulacionAtomica extends Thread {
// ===========================================================================
    int miId, numHebras;
    long numRectangulos;
    DoubleAdder da;

    MiHebraUnaAcumulacionAtomica(int miId, int numHebras, long numRectangulos, DoubleAdder da){
        this.miId = miId;
        this.numHebras = numHebras;
        this.numRectangulos = numRectangulos;
        this.da = da;
    }

    public void run(){
        double x, sumaLocal = 0.0, baseRectangulo = 1.0 / ((double)numRectangulos);
        for (long i = miId; i<numRectangulos; i+=numHebras){
            x = baseRectangulo * (((double)i)+0.5);
            sumaLocal += EjemploNumeroPI.f(x);
        }
        da.add(sumaLocal);
    }
}



// ===========================================================================
class EjemploNumeroPI {
// ===========================================================================

  // -------------------------------------------------------------------------
  public static void main( String args[] ) {
    long                        numRectangulos;
    double                      baseRectangulo, x, suma, pi;
    int                         numHebras;
    long                        t1, t2;
    double                      tSec, tPar;
    Acumula                     a;
    MiHebraMultAcumulaciones  vt[];

    // Comprobacion de los argumentos de entrada.
    if( args.length != 2 ) {
      System.out.println( "ERROR: numero de argumentos incorrecto.");
      System.out.println( "Uso: java programa <numHebras> <numRectangulos>" );
      System.exit( -1 );
    }
    try {
      numHebras      = Integer.parseInt( args[ 0 ] );
      numRectangulos = Long.parseLong( args[ 1 ] );
    } catch( NumberFormatException ex ) {
      numHebras      = -1;
      numRectangulos = -1;
      System.out.println( "ERROR: Numeros de entrada incorrectos." );
      System.exit( -1 );
    }

    System.out.println();
    System.out.println( "Calculo del numero PI mediante integracion." );

    //
    // Calculo del numero PI de forma secuencial.
    //
    System.out.println();
    System.out.println( "Inicio del calculo secuencial." );
    t1 = System.nanoTime();
    baseRectangulo = 1.0 / ( ( double ) numRectangulos );
    suma           = 0.0;
    for( long i = 0; i < numRectangulos; i++ ) {
      x = baseRectangulo * ( ( ( double ) i ) + 0.5 );
      suma += f( x );
    }
    pi = baseRectangulo * suma;
    t2 = System.nanoTime();
    tSec = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Version secuencial. Numero PI: " + pi );
    System.out.println( "Tiempo secuencial (s.):        " + tSec );

    //
    // Calculo del numero PI de forma paralela: 
    // Multiples acumulaciones por hebra.
    //
    System.out.println();
    System.out.print( "Inicio del calculo paralelo: " );
    System.out.println( "Multiples acumulaciones por hebra." );
    t1 = System.nanoTime();

    a = new Acumula();

    MiHebraMultAcumulaciones[] vH = new MiHebraMultAcumulaciones[numHebras];
    for (int i=0; i<numHebras; i++) {
        vH[i] = new MiHebraMultAcumulaciones(i, numHebras, numRectangulos, a);
        vH[i].start();
    }

    for (int i=0; i<numHebras; i++) {
        try {
            vH[i].join();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    pi = baseRectangulo * a.dameDato();

    t2 = System.nanoTime();
    tPar = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Calculo del numero PI:   " + pi );
    System.out.println( "Tiempo ejecucion (s.):   " + tPar );
    System.out.println( "Incremento velocidad :   " + tSec/tPar );

    //
    // Calculo del numero PI de forma paralela: 
    // Una acumulacion por hebra.
    //
    System.out.println();
    System.out.print( "Inicio del calculo paralelo: " );
    System.out.println( "Una acumulacion por hebra." );
    t1 = System.nanoTime();

    a = new Acumula();

    MiHebraUnaAcumulacion[] vH1 = new MiHebraUnaAcumulacion[numHebras];
    for (int i=0; i<numHebras; i++) {
        vH1[i] = new MiHebraUnaAcumulacion(i, numHebras, numRectangulos, a);
        vH1[i].start();
    }

    for (int i=0; i<numHebras; i++) {
        try {
            vH1[i].join();
        }catch(InterruptedException e) {
              e.printStackTrace();
        }
    }

    pi = baseRectangulo * a.dameDato();

    t2 = System.nanoTime();
    tPar = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Calculo del numero PI:   " + pi );
    System.out.println( "Tiempo ejecucion (s.):   " + tPar );
    System.out.println( "Incremento velocidad :   " + tSec/tPar );


    //
    // Calculo del numero PI de forma paralela: 
    // Multiples acumulaciones por hebra (Atomica)
    //
    System.out.println();
    System.out.print( "Inicio del calculo paralelo: " );
    System.out.println( "Multiples acumulaciones por hebra (At)." );
    t1 = System.nanoTime();

    DoubleAdder da = new DoubleAdder();

    MiHebraMultAcumulacionAtomica[] vH3 = new MiHebraMultAcumulacionAtomica[numHebras];
    for (int i=0; i<numHebras; i++) {
        vH3[i] = new MiHebraMultAcumulacionAtomica(i, numHebras, numRectangulos, da);
        vH3[i].start();
    }

    for (int i=0; i<numHebras; i++) {
        try {
            vH3[i].join();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    pi = da.doubleValue() * baseRectangulo;

    t2 = System.nanoTime();
    tPar = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Calculo del numero PI:   " + pi );
    System.out.println( "Tiempo ejecucion (s.):   " + tPar );
    System.out.println( "Incremento velocidad :   " + tSec/tPar );

    //
    // Calculo del numero PI de forma paralela: 
    // Una acumulacion por hebra (Atomica).
    //
    System.out.println();
    System.out.print( "Inicio del calculo paralelo: " );
    System.out.println( "Una acumulacion por hebra (At)." );
    t1 = System.nanoTime();

    da = new DoubleAdder();

    MiHebraUnaAcumulacionAtomica[] vH4 = new MiHebraUnaAcumulacionAtomica[numHebras];
    for (int i=0; i<numHebras; i++) {
        vH4[i] = new MiHebraUnaAcumulacionAtomica(i, numHebras, numRectangulos, da);
        vH4[i].start();
    }

    for (int i=0; i<numHebras; i++) {
        try {
            vH4[i].join();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    pi = da.doubleValue() * baseRectangulo;

    t2 = System.nanoTime();
    tPar = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Calculo del numero PI:   " + pi );
    System.out.println( "Tiempo ejecucion (s.):   " + tPar );
    System.out.println( "Incremento velocidad :   " + tSec/tPar );

    System.out.println();
    System.out.println( "Fin de programa." );
  }

  // -------------------------------------------------------------------------
  static double f( double x ) {
    return ( 4.0/( 1.0 + x*x ) );
  }
}

