package roteador;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TabelaRoteamento {
    /*Implemente uma estrutura de dados para manter a tabela de roteamento. 
     * A tabela deve possuir: IP Destino, Métrica e IP de Saída.
    */

    private ConcurrentMap<String,ElementoTabelaRoteamento> tabela;
    
    public ConcurrentMap<String, ElementoTabelaRoteamento> getTabela() {
        return tabela;
    }

    public TabelaRoteamento(List<String> listaVizinhos){
        tabela = new ConcurrentHashMap<>();

        //Adiciona vizinhos pré-configurados
        for (String vizinho : listaVizinhos) {
            tabela.put(vizinho,new ElementoTabelaRoteamento(vizinho, "1", vizinho));
        }

    }

    public void update_tabela(String tabela_string, InetAddress IPAddress) throws InterruptedException{
        
        if(tabela_string.equals("!")){
            tabela.put(IPAddress.getHostAddress(),new ElementoTabelaRoteamento(IPAddress.getHostAddress(), "1", IPAddress.getHostAddress()));
            throw new InterruptedException(IPAddress.getHostAddress());  
        } 

        String[] elementos = Arrays.copyOfRange(tabela_string.split("\\*"), 1, tabela_string.split("\\*").length);

        for (String elemento : elementos) {
            String[] campos = elemento.split(";");

            ElementoTabelaRoteamento novoElemento = new ElementoTabelaRoteamento(campos[0], Integer.toString((Integer.parseInt(campos[1]) + 1)), IPAddress.getHostAddress());
            ElementoTabelaRoteamento elementoAtual = tabela.get(novoElemento.getDestino());

            if(novoElemento.getDestino().equals(Roteador.IP)) continue;     

            if (elementoAtual != null){
                if(Integer.parseInt(elementoAtual.getMetrica()) >= Integer.parseInt(novoElemento.getMetrica())){
                    tabela.replace(novoElemento.getDestino(), elementoAtual, novoElemento);
                } else {
                    
                }

            } else {
                tabela.put(novoElemento.getDestino(), novoElemento);    
            }
            
        }

    }
    
    public String get_tabela_string(List<String> vizinhos){
        StringBuilder tabela_string = new StringBuilder();

        for (ElementoTabelaRoteamento elemento : tabela.values()) {
            tabela_string.append("*").append(elemento.getDestino()).append(";").append(elemento.getMetrica());
        }    

        return tabela_string.toString();
    }
    

    
}
