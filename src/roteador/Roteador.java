package roteador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Roteador {

    public static String IP = "192.168.201.1";

    public static void main(String[] args) throws IOException {
        /* Lista de endereço IPs dos vizinhos */
        ArrayList<String> ip_list = new ArrayList<>();

        /* Le arquivo de entrada com lista de IPs dos roteadores vizinhos. */
        try ( BufferedReader inputFile = new BufferedReader(new FileReader("IPVizinhos.txt"))) {
            String ip;
            
            while( (ip = inputFile.readLine()) != null){
                ip_list.add(ip);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Roteador.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        /* Cria instâncias da tabela de roteamento e das threads de envio e recebimento de mensagens. */
        TabelaRoteamento tabela = new TabelaRoteamento(ip_list);

        Thread timer = new Thread(new Timer(tabela));
        Thread receiver = new Thread(new MessageSender(tabela, ip_list));
        Thread sender = new Thread(new MessageReceiver(tabela, receiver));
        
        timer.start();
        sender.start();
        receiver.start();
        
    }
    
}
