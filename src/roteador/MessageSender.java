package roteador;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSender implements Runnable{
    TabelaRoteamento tabela; /*Tabela de roteamento */
    ArrayList<String> vizinhos; /* Lista de IPs dos roteadores vizinhos */
    
    public MessageSender(TabelaRoteamento t, ArrayList<String> v){
        tabela = t;
        vizinhos = v;
    }
    
    @Override
    public void run() {
        DatagramSocket clientSocket = null;
        byte[] sendData;
        InetAddress IPAddress = null;
        
        /* Cria socket para envio de mensagem */
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        Boolean first = true;
        while(true){
            
            /* Pega a tabela de roteamento no formato string, conforme especificado pelo protocolo. */
            String tabela_string = tabela.tabelaProtocolo(vizinhos);
            if(first){
                tabela_string = "!";
                first = false;
            }

            /* Converte string para array de bytes para envio pelo socket. */
            sendData = tabela_string.getBytes(StandardCharsets.UTF_8);
            
            /* Anuncia a tabela de roteamento para cada um dos vizinhos */
            for (String ip : vizinhos){
                /* Converte string com o IP do vizinho para formato InetAddress */
                try {
                    IPAddress = InetAddress.getByName(ip);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }
                
                /* Configura pacote para envio da menssagem para o roteador vizinho na porta 5000*/
                System.out.println(tabela.tabelaString("ENVIAR",IPAddress.getHostAddress()));
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);         
                
                /* Realiza envio da mensagem. */
                try {
                    clientSocket.send(sendPacket);
                } catch (IOException ex) {
                    Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            vizinhos.clear();
            for (ElementoTabelaRoteamento elemento : tabela.getTabela().values()) {
                if(elemento.getMetrica().equals("1")){
                    vizinhos.add(elemento.getDestino());
                }
            }
            
            /* Espera 10 segundos antes de realizar o próximo envio. CONTUDO, caso
             * a tabela de roteamento sofra uma alteração, ela deve ser reenvida aos
             * vizinho imediatamente.
             */

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                System.out.println("A neighbor router has entered the network!");
                continue;
            }

        }
        
    }
    
}
