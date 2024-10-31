package Lab4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


// ===========================================================================
class ZonaIntercambio {
// ===========================================================================
volatile long tiempo;

  public ZonaIntercambio(){
    tiempo = 500;
  }

  public void setTiempo(long t){
    tiempo=t;
  }

  public long getTiempo(){
    return tiempo;
  }
}


// ===========================================================================

class HebraCalculadora extends Thread{
  JTextField  txfMensajes;
  volatile boolean fin;
  ZonaIntercambio zona;
  public HebraCalculadora(JTextField  txfMensajes, ZonaIntercambio zona){
    super();
    this.txfMensajes = txfMensajes;
    this.zona = zona;
    fin=false;
  }
  public void run(){
    long i=1L;
    while (! fin){
      if (GUISecuenciaPrimos.esPrimo(i)){
        final long numero=i;
        SwingUtilities.invokeLater(new Runnable(){
          public void run(){
            txfMensajes.setText(String.valueOf(numero));
          }
        });
        try{
          Thread.sleep(zona.getTiempo());
        }catch(InterruptedException e){
          e.printStackTrace();
        }
      }
      i++;
    }
  }

  public void para(){
    fin = true;
  }
}
public class GUISecuenciaPrimos {
// ===========================================================================
  JFrame      container;
  JPanel      jpanel;
  JTextField  txfMensajes;
  JButton     btnIniciaSecuencia, btnCancelaSecuencia;
  JSlider     sldEspera;
  HebraCalculadora  t; // Ejercicio 2.2
  ZonaIntercambio   z; // Ejercicio 2.3
  
  // -------------------------------------------------------------------------
  public static void main( String args[] ) {
    GUISecuenciaPrimos gui = new GUISecuenciaPrimos();
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
        gui.go();
      }
    });
  }

  // -------------------------------------------------------------------------
  public void go() {
    // Constantes.
    final int valorMaximo = 1000;
    final int valorMedio  = 500;

    // Variables.
    JPanel  tempPanel;

    // Crea el JFrame principal.
    container = new JFrame( "GUI Secuencia de Primos " );

    // Consigue el panel principal del Frame "container".
    jpanel = ( JPanel ) container.getContentPane();
    jpanel.setLayout( new GridLayout( 3, 1 ) );

    // Crea e inserta la etiqueta y el campo de texto para los mensajes.
    txfMensajes = new JTextField( 20 );
    txfMensajes.setEditable( false );
    tempPanel = new JPanel();
    tempPanel.setLayout( new FlowLayout() );
    tempPanel.add( new JLabel( "Secuencia: " ) );
    tempPanel.add( txfMensajes );
    jpanel.add( tempPanel );

    // Crea e inserta los botones de Inicia secuencia y Cancela secuencia.
    btnIniciaSecuencia = new JButton( "Inicia secuencia" );
    btnCancelaSecuencia = new JButton( "Cancela secuencia" );
    tempPanel = new JPanel();
    tempPanel.setLayout( new FlowLayout() );
    tempPanel.add( btnIniciaSecuencia );
    tempPanel.add( btnCancelaSecuencia );
    jpanel.add( tempPanel );

    // Crea e inserta el slider para controlar el tiempo de espera.
    sldEspera = new JSlider( JSlider.HORIZONTAL, 0, valorMaximo , valorMedio );
    tempPanel = new JPanel();
    tempPanel.setLayout( new BorderLayout() );
    tempPanel.add( new JLabel( "Tiempo de espera: " ) );
    tempPanel.add( sldEspera );
    jpanel.add( tempPanel );
    
    // Activa inicialmente los 2 botones.
    btnIniciaSecuencia.setEnabled( true );
    btnCancelaSecuencia.setEnabled( false );
    z = new ZonaIntercambio();
    // Anyade codigo para procesar el evento del boton de Inicia secuencia.
    btnIniciaSecuencia.addActionListener( new ActionListener() {
        public void actionPerformed( ActionEvent e ) {
          // ...
          btnIniciaSecuencia.setEnabled( false );
          btnCancelaSecuencia.setEnabled( true );

          t = new HebraCalculadora(txfMensajes, z );
          t.start();
        }
    } );

    // Anyade codigo para procesar el evento del boton de Cancela secuencia.
    btnCancelaSecuencia.addActionListener( new ActionListener() {
        public void actionPerformed( ActionEvent e ) {
          // ...
          btnIniciaSecuencia.setEnabled( true );
          btnCancelaSecuencia.setEnabled( false );

          t.para();
        }
    } );

    // Anyade codigo para procesar el evento del slider " Espera " .
    sldEspera.addChangeListener( new ChangeListener() {
      public void stateChanged( ChangeEvent e ) {
        JSlider sl = ( JSlider ) e.getSource();
        if ( ! sl.getValueIsAdjusting() ) {
          long tiempoMilisegundos = ( long ) sl.getValue();
          System.out.println( "JSlider value = " + tiempoMilisegundos );
          // ...
          z.setTiempo(tiempoMilisegundos);
        }
      }
    } );

    // Fija caracteristicas del container.
    container.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    container.pack();
    container.setResizable( false );
    container.setVisible( true );

    System.out.println( "% End of routine: go.\n" );
  }

  // -------------------------------------------------------------------------
  static boolean esPrimo( long num ) {
    boolean primo;
    if( num < 2 ) {
      primo = false;
    } else {
      primo = true;
      long i = 2;
      while( ( i < num )&&( primo ) ) {
        primo = ( num % i != 0 );
        i++;
      }
    }
    return( primo );
  }
}
