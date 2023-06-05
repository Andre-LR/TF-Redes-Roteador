package roteador;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabelaRoteamento {
    /*Implemente uma estrutura de dados para manter a tabela de roteamento. 
     * A tabela deve possuir: IP Destino, Métrica e IP de Saída.
    */

    private Map<String,ElementoTabelaRoteamento> tabela;
    
    public Map<String, ElementoTabelaRoteamento> getTabela() {
        return tabela;
    }

    public TabelaRoteamento(List<String> listaVizinhos){
        tabela = new HashMap<>();

        //Adiciona vizinhos pré-configurados
        for (String vizinho : listaVizinhos) {
            tabela.put(vizinho,new ElementoTabelaRoteamento(vizinho, "1", vizinho));
        }

    }

    public void update_tabela(String tabela_string, InetAddress IPAddress) throws InterruptedException{
        
        if(tabela_string.equals("!")){
            tabela.put(IPAddress.getHostAddress(),new ElementoTabelaRoteamento(IPAddress.getHostAddress(), "1", IPAddress.getHostAddress()));
            System.out.println(tabela.toString());
            throw new InterruptedException(IPAddress.getHostAddress());  
        } 

        String[] elementos = Arrays.copyOfRange(tabela_string.split("\\*"), 1, tabela_string.split("\\*").length);

        for (String elemento : elementos) {
            String[] campos = elemento.split(";");

            ElementoTabelaRoteamento novoElemento = new ElementoTabelaRoteamento(campos[0], Integer.toString((Integer.parseInt(campos[1]) + 1)), IPAddress.getHostAddress());
            ElementoTabelaRoteamento elementoAtual = tabela.get(novoElemento.destino);

            if(novoElemento.destino.equals(Roteador.IP)) continue;     

            if (elementoAtual != null){
                
                if(Integer.parseInt(elementoAtual.metrica) >= Integer.parseInt(novoElemento.metrica)){
                    tabela.replace(novoElemento.destino, elementoAtual, novoElemento);
                }

            } else {
                tabela.put(novoElemento.destino, novoElemento);    
            }
            
        }

        System.out.println(tabela.toString());
    
    }
    
    public String get_tabela_string(List<String> vizinhos){
        StringBuilder tabela_string = new StringBuilder();

        if(tabela.isEmpty()){
            tabela_string.append("!");
        } else {
            for (ElementoTabelaRoteamento elemento : tabela.values()) {
                tabela_string.append("*").append(elemento.destino).append(";").append(elemento.metrica);
            }    
        }

        return tabela_string.toString();
    }
    

    
}
