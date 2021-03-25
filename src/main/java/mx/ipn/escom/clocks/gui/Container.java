package mx.ipn.escom.clocks.gui;

import java.awt.FlowLayout;
import java.util.Random;

import javax.swing.JFrame;

public class Container extends JFrame {
  /**
   *
   */
  private static final long serialVersionUID = 3864514692691142425L;
  TimerPanel timer1;
  TimerPanel timer2;
  TimerPanel timer3;
  TimerPanel timer4;

  public Container() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("PRÁCTICA 1");
    this.setLayout(new FlowLayout());
    this.setSize(600, 350);
    this.setResizable(false);
    this.setVisible(true);

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
  }
}
