package mx.ipn.escom.clocks.common.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Libro implements Serializable {
  private Long ISBN;
  private String nombre;
  private String autor;
  private String editorial;
  private Double precio;
  private String portada;
}
