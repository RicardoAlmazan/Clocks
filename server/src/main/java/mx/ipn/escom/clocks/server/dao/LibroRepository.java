package mx.ipn.escom.clocks.server.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.ipn.escom.clocks.common.model.Libro;
import mx.ipn.escom.clocks.common.model.Pedido;

public class LibroRepository {

  private Connection con;

  private static final String SQL_SELECT_AVAILABLE = "SELECT * FROM libro WHERE ISBN NOT IN (SELECT idLibro FROM Pedido WHERE idSesion = ?) ORDER BY RAND() LIMIT 1";
  private static final String SQL_NUEVA_SESION = "{ call spNuevaSesion(?) }";
  private static final String SQL_INSERT_LIBRO_PEDIDO = "{ call spNuevoLibroCliente(?, ?, ?, ?) }";

  private Connection getConnection() {
    String user = "root";
    String pwd = "n0m3l0s3";
    String url = "jdbc:mysql://localhost:3306/libreria?useSSL=false";
    String driver = "com.mysql.cj.jdbc.Driver";
    try {
      Class.forName(driver);
      con = DriverManager.getConnection(url, user, pwd);
    } catch (ClassNotFoundException | SQLException ex) {
      Logger.getLogger(LibroRepository.class.getName()).log(Level.SEVERE, null, ex);
    }
    return con;
  }

  public Libro getLibroDisponible(Integer idSesion) throws SQLException {
    this.getConnection();
    CallableStatement cs = null;
    ResultSet rs = null;
    try {
      cs = con.prepareCall(SQL_SELECT_AVAILABLE);
      cs.setInt(1, idSesion);
      rs = cs.executeQuery();
      if (rs.next()) {
        return new Libro(rs.getLong("ISBN"), rs.getString("nombre"), rs.getString("autor"), rs.getString("editorial"),
            rs.getDouble("precio"), rs.getString("portada"));
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (cs != null) {
        cs.close();
      }
      if (con != null) {
        con.close();
      }
    }
    return null;
  }

  public Integer getNuevaSesion(String fechaInicio) throws SQLException {
    this.getConnection();
    CallableStatement cs = null;
    ResultSet rs = null;
    try {
      cs = con.prepareCall(SQL_NUEVA_SESION);
      cs.setString(1, fechaInicio);
      rs = cs.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (cs != null) {
        cs.close();
      }
      if (con != null) {
        con.close();
      }
    }
    return null;
  }

  public void insertLibroPedido(Pedido pedido) throws SQLException {
    this.getConnection();
    CallableStatement cs = null;
    ResultSet rs = null;
    try {
      cs = con.prepareCall(SQL_INSERT_LIBRO_PEDIDO);
      cs.setString(1, pedido.getIp());
      cs.setLong(2, pedido.getIdLibro());
      cs.setInt(3, pedido.getIdSesion());
      cs.setString(4, pedido.getFecha());
      rs = cs.executeQuery();
      if (rs.next()) {
        System.out.println(rs.getInt(1));
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (cs != null) {
        cs.close();
      }
      if (con != null) {
        con.close();
      }
    }

  }
}
