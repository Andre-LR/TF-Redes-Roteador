package roteador;

public class ElementoTabelaRoteamento {
    public String destino;
    public String metrica;
    public String saida;

    public ElementoTabelaRoteamento(String destino, String metrica, String saida){
        this.destino = destino;
        this.metrica = metrica;
        this.saida = saida;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getMetrica() {
        return metrica;
    }

    public void setMetrica(String metrica) {
        this.metrica = metrica;
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(String saida) {
        this.saida = saida;
    }

    @Override
    public String toString(){
        return this.destino + "|" + this.metrica + "|" +this.saida;
    }

}