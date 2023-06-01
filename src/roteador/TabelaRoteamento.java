package roteador;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabelaRoteamento {
    /*Implemente uma estrutura de dados para manter a tabela de roteamento. 
     * A tabela deve possuir: IP Destino, Métrica e IP de Saída.
    */

    private List<ElementoTabela> tabela;
    
    public TabelaRoteamento(List<String> listaVizinhos){
        tabela = new ArrayList<>();

        for (String vizinho : listaVizinhos) {
            tabela.add(new ElementoTabela(vizinho, "0", vizinho));
        }

    }

    public class ElementoTabela {
        public String destino;
        public String metrica;
        public String saida;

        public ElementoTabela(String destino, String metrica, String saida){
            this.destino = destino;
            this.metrica = metrica;
            this.saida = saida;
        }

    }
    
    
    public void update_tabela(String tabela_string, InetAddress IPAddress){
        if(tabela_string == "!") return;

        String[] elementos = Arrays.copyOfRange(tabela_string.split("\\*"), 1, tabela_string.split("\\*").length);
        //List<ElementoTabela> novaTabela = new ArrayList<>();

        for (String elemento : elementos) {
            String[] campos = elemento.split(";");

            ElementoTabela novoElemento = new ElementoTabela(campos[0], campos[1], IPAddress.getHostAddress());

            if (tabela.stream().anyMatch( e -> e.destino.equals(novoElemento.destino))){
                
                ElementoTabela elementoAtual = tabela.stream().filter(e -> e.destino.equals(novoElemento.destino)).findFirst().get();
                
                if(Integer.parseInt(elementoAtual.metrica) >= Integer.parseInt(novoElemento.metrica)){
                    tabela.remove(elementoAtual);
                    tabela.add(novoElemento);
                }

            } else {
                novoElemento.metrica = Integer.toString((Integer.parseInt(campos[1]) + 1));
                tabela.add(novoElemento);    
            }
            
        }

        //this.tabela = novaTabela;
        System.out.println(get_tabela_string());
    
    }
    
    public String get_tabela_string(){
        StringBuilder tabela_string = new StringBuilder();

        if(tabela.isEmpty()){
            tabela_string.append("!");
        } else {
            for (ElementoTabela elemento : tabela) {
                tabela_string.append("*").append(elemento.destino).append(";").append(elemento.metrica);
            }    
        }

        return tabela_string.toString();
    }
    

    
}
