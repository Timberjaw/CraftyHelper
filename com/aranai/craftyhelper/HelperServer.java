package com.aranai.craftyhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class HelperServer implements Runnable
{
    private Socket cs;
    private ServerSocket socket;
    private BufferedReader reader;
    private OutputStream out;
    private CraftyHelper ch;

    public HelperServer(CraftyHelper ch) {
        this.ch = ch;
        
        try {
            System.out.println("[CraftyHelper] Opening socket...");
            socket = new ServerSocket(6789);
            System.out.println("[CraftyHelper] Listening for connection...");
            socket.setSoTimeout(5000);
            cs = socket.accept();
            System.out.println("[CraftyHelper] Established socket connection.");
            reader = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            out = cs.getOutputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            byte cmd = (byte) reader.read();
            while (cmd >= 0) {
                // Trigger actions
                String line = reader.readLine();
                
                switch(cmd) {
                    case Commands.GETPLAYERLIST:
                        out.write(this.prepOutput(ch.cmdGetPlayerList()));
                    break;
                    case Commands.GETVERSION:
                        out.write(this.prepOutput(ch.cmdGetVersion()));
                    break;
                    case Commands.GETPERFSTATS:
                        out.write(this.prepOutput(ch.cmdGetPerfStats()));
                    break;
                    case Commands.GETIP:
                        out.write(this.prepOutput(ch.cmdGetPlayerIp(line)));
                    break;
                    case Commands.GETPLAYERPOS:
                        out.write(this.prepOutput(ch.cmdGetPlayerPos(line)));
                    break;
                    case Commands.ECHO:
                    default:
                        out.write(this.prepOutput("ECHO "+line));
                    break;
                }
                
                out.flush();
                
                cmd = (byte) reader.read();
            }
        } catch (IOException e) {}
    }
    
    public byte[] prepOutput(String output)
    {
        return (output+"\n").getBytes();
    }
    
    public void stop() {
        try {
            System.out.println("[CraftyHelper] Closing streams and sockets.");
            reader.close();
            cs.getOutputStream().close();
            cs.getInputStream().close();
            cs.close();
            socket.close();
        } catch (IOException e) {}
    }
}