package pt.escnaval.exercicios;

import java.util.Objects;

public final class Endereco {
 private final String rua;
 private final String cidade;
 private final String codigoPostal;
 private final boolean valido;
 public Endereco(String rua, String cidade, String codigoPostal) {
 // Validação sem exceções
 if (rua == null || rua.trim().isEmpty() ||
 cidade == null || cidade.trim().isEmpty() ||
 codigoPostal == null || codigoPostal.trim().isEmpty()) {
 this.rua = "";
 this.cidade = "";
 this.codigoPostal = "";
 this.valido = false;
 } else {
 this.rua = rua.trim();
 this.cidade = cidade.trim();
 this.codigoPostal = codigoPostal.trim();
 this.valido = true;
 }
 }
 public boolean isValido() { return valido; }
 public String rua() { return rua; }
public String cidade() { return cidade; }
 public String codigoPostal() { return codigoPostal; }
 @Override
 public String toString() {
 if (!valido) return "[Endereço inválido]";
 return String.format("%s, %s (%s)", rua, cidade, codigoPostal);
 }
 @Override
 public boolean equals(Object o) {
 if (this == o) return true;
 if (!(o instanceof Endereco)) return false;
 Endereco other = (Endereco) o;
 return valido == other.valido &&
 rua.equals(other.rua) &&
 cidade.equals(other.cidade) &&
 codigoPostal.equals(other.codigoPostal);
 }
 @Override
 public int hashCode() {
 return Objects.hash(rua, cidade, codigoPostal, valido);
 }
}