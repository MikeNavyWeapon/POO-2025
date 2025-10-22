package pt.escnaval.exercicios;

public class Endereco {
    private String rua;
    private int nrPorta;
    private String codPostal;

    public Endereco(String aRua, int aNrPorta, String aCodPostal) {
        rua = aRua;
        nrPorta = aNrPorta;
        codPostal = aCodPostal;
    }

    public String mostrar() {
        return "Rua: " + rua + "; Nr. Porta: " + nrPorta + "; Cod. Postal: " + codPostal;
    }
}
