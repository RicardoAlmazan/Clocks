package mx.ipn.escom.clocks.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.io.DataInputStream;
import java.io.IOException;
// import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
// import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mx.ipn.escom.clocks.common.model.Libro;
import mx.ipn.escom.clocks.common.rmi.Libreria;

public class Container extends JFrame {

  // Socket cliente;
  private DatagramSocket cliente;
  private TimerLabel timerLabel;
  private JButton requestButton;
  private Libreria libreria;
  private JPanel bookPanel;

  private JLabel bookNISBNLabelValue;
  private JLabel bookNameLabelValue;
  private JLabel bookAuthorLabelValue;
  private JLabel bookEditorialLabelValue;
  private JLabel bookPriceLabelValue;

  public Container() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("PRÁCTICA 3 - Cliente");
    this.setLayout(new BorderLayout());
    this.setSize(600, 600);
    this.setResizable(false);
    this.setVisible(true);

    init();
  }

  private void init() {
    JPanel timerPanel = new JPanel();
    this.timerLabel = new TimerLabel();
    timerPanel.add(timerLabel);
    Thread th = new Thread(timerLabel);
    this.add(timerPanel, BorderLayout.NORTH);

    this.bookPanel = new JPanel();
    this.bookPanel.setSize(200, 400);
    this.bookPanel.setLayout(new GridLayout(5, 2));

    JLabel bookISBNLabel = new JLabel("ISBN: ");
    bookISBNLabel.setHorizontalAlignment(JLabel.RIGHT);
    bookNISBNLabelValue = new JLabel("");
    bookNISBNLabelValue.setHorizontalAlignment(JLabel.LEFT);

    JLabel bookNameLabel = new JLabel("Nombre del libro: ");
    bookNameLabel.setHorizontalAlignment(JLabel.RIGHT);
    bookNameLabelValue = new JLabel("");
    bookNameLabelValue.setHorizontalAlignment(JLabel.LEFT);

    JLabel bookAuthorLabel = new JLabel("Autor: ");
    bookAuthorLabel.setHorizontalAlignment(JLabel.RIGHT);
    bookAuthorLabelValue = new JLabel("");
    bookAuthorLabelValue.setHorizontalAlignment(JLabel.LEFT);

    JLabel bookEditorialLabel = new JLabel("Editorial: ");
    bookEditorialLabel.setHorizontalAlignment(JLabel.RIGHT);
    bookEditorialLabelValue = new JLabel("");
    bookEditorialLabelValue.setHorizontalAlignment(JLabel.LEFT);

    JLabel bookPriceLabel = new JLabel("Precio: ");
    bookPriceLabel.setHorizontalAlignment(JLabel.RIGHT);
    bookPriceLabelValue = new JLabel("");
    bookPriceLabelValue.setHorizontalAlignment(JLabel.LEFT);

    this.bookPanel.add(bookNameLabel);
    this.bookPanel.add(bookNameLabelValue);
    this.bookPanel.add(bookAuthorLabel);
    this.bookPanel.add(bookAuthorLabelValue);
    this.bookPanel.add(bookEditorialLabel);
    this.bookPanel.add(bookEditorialLabelValue);
    this.bookPanel.add(bookPriceLabel);
    this.bookPanel.add(bookPriceLabelValue);
    add(bookPanel, BorderLayout.CENTER);

    JPanel requestPanel = new JPanel();
    this.requestButton = new JButton("SOLICITAR");
    this.requestButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          setLibroData(libreria.solicitarLibro(timerLabel.getTime()));
        } catch (RemoteException e1) {
          e1.printStackTrace();
        }
      }
    });
    requestPanel.add(this.requestButton);
    this.add(requestPanel, BorderLayout.SOUTH);

    th.start();
    String HOST_RMI = "localhost";
    // String HOST = "3.16.67.189";
    int PORT_RMI = 2405;
    // int PORT_RMI = 2406;

    String HOST_UDP = "230.0.0.1";
    int PORT_UDP = 2406;

    try {
      Registry reg = LocateRegistry.getRegistry(HOST_RMI, PORT_RMI);
      libreria = (Libreria) reg.lookup("libreria");

      cliente = new DatagramSocket();

      MulticastSocket socket = new MulticastSocket(PORT_UDP);
      socket.joinGroup(InetAddress.getByName(HOST_UDP));
      while (true) {
        byte[] dato = new byte[1024];
        DatagramPacket packet = new DatagramPacket(dato, dato.length);
        socket.receive(packet);
        String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());

        if ("REINICIAR".equals(msg)) {
          JOptionPane.showMessageDialog(null, "Se ha reiniciado la sesión del lado del servidor.");
          restart();
        }
      }

    } catch (IOException | NotBoundException ex) {
      Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void setLibroData(Libro libro) {
    if (libro == null) {
      JOptionPane.showMessageDialog(null, "No hay más libros disponibles. :c");
      return;
    }
    this.bookNISBNLabelValue.setText(libro.getISBN() + "");
    this.bookNameLabelValue.setText(libro.getNombre());
    this.bookAuthorLabelValue.setText(libro.getAutor());
    this.bookEditorialLabelValue.setText(libro.getEditorial());
    this.bookPriceLabelValue.setText(libro.getPrecio() + "");
  }

  public void restart() {
    this.bookNISBNLabelValue.setText("");
    this.bookNameLabelValue.setText("");
    this.bookAuthorLabelValue.setText("");
    this.bookEditorialLabelValue.setText("");
    this.bookPriceLabelValue.setText("");
  }
}
