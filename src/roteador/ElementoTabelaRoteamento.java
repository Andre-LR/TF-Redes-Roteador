package roteador;

import java.time.LocalDateTime;

public class ElementoTabelaRoteamento {
    private String destino;
    private String metrica;
    private String saida;
    private LocalDateTime timer;

    public ElementoTabelaRoteamento(String destino, String metrica, String saida){
        this.destino = destino;
        this.metrica = metrica;
        this.saida = saida;
        this.timer = LocalDateTime.now();
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

    public LocalDateTime getTimer() {
        return timer;
    }

    public void setTimer(LocalDateTime timer) {
        this.timer = timer;
    }


    @Override
    public String toString(){
        return this.destino + " | " + this.metrica + " | " +this.saida +" | " +this.timer;
    }

}