package pt.escnaval.exercicios;
import java.util.Objects;
import java.util.regex.Pattern;
public final class Iban {
 private static final Pattern IBAN_PT =
 Pattern.compile("^PT\\w{23}$"); // simplificação didática
 private final String codigo;
 public Iban(String codigo) {
 if (codigo == null) {
 this.codigo = null;
 } else {
 String c = codigo.trim().toUpperCase();
 if (IBAN_PT.matcher(c).matches()) {
 this.codigo = c;
 } else {
 this.codigo = null;
 }
 }
 }
 public boolean isValid() {
 return codigo != null;
 }
 public String codigo() {
 return codigo;
 }
 @Override
 public String toString() {
 return isValid() ? codigo : "INVALID";
 }
 @Override
 public boolean equals(Object o) {
 if (this == o) return true;
 if (!(o instanceof Iban)) return false;
 Iban other = (Iban) o;
 return Objects.equals(codigo, other.codigo);
 }
 @Override
 public int hashCode() {
 return codigo != null ? codigo.hashCode() : 0;
 }
}
