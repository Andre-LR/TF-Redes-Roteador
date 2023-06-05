package roteador;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageReceiver implements Runnable{
    private TabelaRoteamento tabela;
    private Thread sender;
    
    public MessageReceiver(TabelaRoteamento t, Thread s){
        tabela = t;
        sender = s;
    }
    
    @Override
    public void run() {
        DatagramSocket serverSocket = null;
        
        try {
            
            /* Inicializa o servidor para aguardar datagramas na porta 5000 */
            serverSocket = new DatagramSocket(5000);
        } catch (SocketException ex) {
            Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        byte[] receiveData = new byte[1024];
        
        while(true){
            
            /* Cria um DatagramPacket */
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            try {
                /* Aguarda o recebimento de uma mensagem */
                serverSocket.receive(receivePacket);
            } catch (IOException ex) {
                Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }

            /* Transforma a mensagem em string */
            String tabela_string = new String( Arrays.copyOf(receivePacket.getData(),receivePacket.getLength()) , StandardCharsets.UTF_8 );
            System.out.println(LocalDateTime.now()+"|RECEIVED->"+tabela_string +"|"+receivePacket.getAddress());
            
            /* Obtem o IP de saída da mensagem */
            InetAddress IPAddress = receivePacket.getAddress();

            if(IPAddress.getHostAddress().equals("127.0.0.1")) continue;

            try {
                tabela.update_tabela(tabela_string, IPAddress);
            } catch (InterruptedException ex) {
                sender.interrupt();
            }
            


        }
    }
    
}
