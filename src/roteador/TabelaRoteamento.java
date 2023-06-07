package roteador;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

    public void atualizarTabela(String tabela_string, InetAddress IPAddress) throws InterruptedException{

        ElementoTabelaRoteamento roteadorEnviador = tabela.get(IPAddress.getHostAddress());
        
        if(roteadorEnviador != null){
            roteadorEnviador.setTimer(LocalDateTime.now());
        } else {
            tabela.put(IPAddress.getHostAddress(), new ElementoTabelaRoteamento(IPAddress.getHostAddress(), "1", IPAddress.getHostAddress()));
        }

        
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
    
    public String tabelaProtocolo(List<String> vizinhos){
        StringBuilder tabelaProtocolo = new StringBuilder();

        for (ElementoTabelaRoteamento elemento : tabela.values()) {
            tabelaProtocolo.append("*").append(elemento.getDestino()).append(";").append(elemento.getMetrica());
        }    

        return tabelaProtocolo.toString();
    }
    
    public String tabelaString(String tipo, String ip){

        StringBuilder tabelaString = new StringBuilder();
        
        tabelaString.append("*-----------------------------------------------*\n");
        tabelaString.append("|       "+alinharString(tipo, 8)+"        |    "+alinharString(ip, 8)+"     |\n");
        tabelaString.append("*-----------------------------------------------*\n");
        tabelaString.append("|      DESTINO     | MÉTRICA |      SAÍDA       |\n");

        for (ElementoTabelaRoteamento elemento : tabela.values()) {
            tabelaString.append("|"+alinharString(elemento.getDestino(), 18) + "|" + alinharString(elemento.getMetrica(),9) + "|" +alinharString(elemento.getSaida(),18)+"|\n");
        }

        tabelaString.append("*-----------------------------------------------*\n");

        return tabelaString.toString();

    }

    private String alinharString(String s, int size) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(' ');
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(' ');
        }
        return sb.toString();
    }
    
}
