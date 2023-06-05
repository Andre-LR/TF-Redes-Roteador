package roteador;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Timer implements Runnable {
    
    TabelaRoteamento tabela;

    public Timer(TabelaRoteamento t){
        tabela = t;
    }

    @Override
    public void run(){

        while (true) {
            for (ElementoTabelaRoteamento elemento : tabela.getTabela().values()) {
                if(ChronoUnit.SECONDS.between(elemento.getTimer(), LocalDateTime.now())>30){
                    System.out.println("A neighbor router has left the network!");
                    tabela.getTabela().remove(elemento.getDestino());  
                } 
            }
        }
    }
}
