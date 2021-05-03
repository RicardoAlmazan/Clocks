package mx.ipn.escom.clocks.common.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido implements Serializable{
  private Long idLibro;
  private Integer idUsuario;
  private Integer idSesion;
  private String fecha;
  private String ip;
}
