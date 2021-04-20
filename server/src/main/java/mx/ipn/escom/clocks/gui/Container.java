package mx.ipn.escom.clocks.gui;

import java.awt.FlowLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

public class Container extends JFrame {
  /**
   *
   */
  private static final long serialVersionUID = 3864514692691142425L;
  private TimerPanel timer1;
  private TimerPanel timer2;
  private TimerPanel timer3;
  private TimerPanel timer4;

  private ServerSocket server;

  public Container() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("PRÁCTICA 2");
    this.setLayout(new FlowLayout());
    this.setSize(600, 350);
    this.setResizable(false);
    this.setVisible(true);

    init();
  }

  private void init() {
    Random rand = new Random();

    this.timer1 = new TimerPanel();
    Thread t1 = new Thread(timer1);
    this.add(timer1);

    this.timer2 = new TimerPanel(rand.nextInt(24) + 1, rand.nextInt(60) + 1, rand.nextInt(60) + 1);
    Thread t2 = new Thread(timer2);
    this.add(timer2);

    this.timer3 = new TimerPanel(rand.nextInt(24) + 1, rand.nextInt(60) + 1, rand.nextInt(60) + 1);
    Thread t3 = new Thread(timer3);
    this.add(timer3);

    this.timer4 = new TimerPanel(rand.nextInt(24) + 1, rand.nextInt(60) + 1, rand.nextInt(60) + 1);
    Thread t4 = new Thread(timer4);
    this.add(timer4);

    t1.start();
    t2.start();
    t3.start();
    t4.start();
    startServer();
  }

  private void startServer() {
    try {
      server = new ServerSocket(2405);
      Integer count = 1;
      while (true) {
        System.out.println("Esperando cliente... ");

        Socket cliente = server.accept();
        System.out.println("Conexion establecida desde " + cliente.getInetAddress() + ":" + cliente.getPort());
        if (count == 1) {
          timer2.setCliente(cliente);
        } else if (count == 2) {
          timer3.setCliente(cliente);
        } else if (count == 3) {
          timer3.setCliente(cliente);
        } else {
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, e);
    }
  }
}
