package mx.ipn.escom.clocks.client.gui;

import java.awt.FlowLayout;
// import java.io.DataInputStream;
import java.io.IOException;
// import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
// import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

public class Container extends JFrame {

  // Socket cliente;
  DatagramSocket cliente;
  TimerLabel timerLabel;

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
    this.timerLabel = new TimerLabel();
    timerLabel.setBounds(0, 200, 100, 100);
    Thread th = new Thread(timerLabel);
    this.add(timerLabel);
    th.start();

    try {
      String HOST = "127.0.0.1";
      int PORT_CLIENTE = 2406;
      int PORT = 2405;

      cliente = new DatagramSocket(PORT_CLIENTE, InetAddress.getByName(HOST));
      String dato = "HERE";
      byte[] datoByte = dato.getBytes();
      // cliente = new Socket(HOST, PORT);
      // DataInputStream dis = new DataInputStream(cliente.getInputStream());
      // while (true) {
      // String time = dis.readUTF();
      // String[] dataTime = time.split(":");

      // timerLabel.setTime(Integer.parseInt(dataTime[0]),
      // Integer.parseInt(dataTime[1]), Integer.parseInt(dataTime[2]));
      // }

      DatagramPacket datoDatagram = new DatagramPacket(datoByte, datoByte.length, InetAddress.getByName(HOST), PORT);
      cliente.send(datoDatagram);

      while (true) {
        DatagramPacket horaDG = new DatagramPacket(new byte[100], 100);
        cliente.receive(horaDG);
        String time = new String(horaDG.getData()).trim();
        System.out.println("Recibido: " + time + " de " + horaDG.getAddress() + ":" + horaDG.getPort());
        String[] dataTime = time.split(":");

        timerLabel.setTime(Integer.parseInt(dataTime[0]), Integer.parseInt(dataTime[1]), Integer.parseInt(dataTime[2]));
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (IOException ex) {
      Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
