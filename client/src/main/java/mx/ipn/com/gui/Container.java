package mx.ipn.com.gui;

import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

public class Container extends JFrame {

  Socket cliente;
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
    Thread th = new Thread(timerLabel);
    this.add(timerLabel);
    th.start();

    try {
      // Cliente
      String HOST = "127.0.0.1";
      int PORT = 2405;
      cliente = new Socket(HOST, PORT);
      DataInputStream dis = new DataInputStream(cliente.getInputStream());
      while (true) {
        String time = dis.readUTF();
        String[] dataTime = time.split(":");

        timerLabel.setTime(Integer.parseInt(dataTime[0]), Integer.parseInt(dataTime[1]), Integer.parseInt(dataTime[2]));
      }

      // OutputStream os = cliente.getOutputStream();
      // String bytes = "HOLA";
      // os.write(bytes.getBytes());
      // os.close();

    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (IOException ex) {
      Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
