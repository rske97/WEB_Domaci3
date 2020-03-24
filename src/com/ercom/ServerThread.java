package com.ercom;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ServerThread implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader inRest;
    private PrintWriter outRest;
    private Socket s;
    public ServerThread(Socket sock) throws IOException {
        this.client = sock;
        InetAddress addr = InetAddress.getByName("quotes.rest");
        s = new Socket(addr, 80);

        try {
            //inicijalizacija ulaznog toka
            in = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream()));

            //inicijalizacija izlaznog sistema
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    client.getOutputStream())), true);
            inRest = new BufferedReader(
                    new InputStreamReader(
                            s.getInputStream()));

            //inicijalizacija izlaznog sistema
            outRest = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    s.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {
            String komanda = in.readLine();
            outRest.println("GET /qod HTTP/1.1\n" +
                    "Host: quotes.rest\n" +
                    "Connection: keep-alive\n" +
                    "Cache-Control: max-age=0\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Accept: text/html,application/json\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-User: ?1\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Accept-Language: sr-RS,sr;q=0.9,en-US;q=0.8,en;q=0.7,bs;q=0.6,hr;q=0.5");

            outRest.println();
            String outStr;
            String sacuuvaj = "";
            while((outStr = inRest.readLine()) != null){
                if(outStr.contains("\"quote\"")){
                    sacuuvaj = outStr.split(":")[1];
                    sacuuvaj = sacuuvaj.split("\"")[1];
                }
            }

            String response = "";

            response = napraviOdogovor(komanda, sacuuvaj);

            //ovaj deo nam sluzi samo da bismo ispisali na konzoli servera ceo HTTP zahtev
            System.out.println("HTTP ZAHTEV KLIJENTA:\n");
            do {
                System.out.println(komanda);
                komanda = in.readLine();
            } while (!komanda.trim().equals(""));


            //treba odgovoriti browser-u po http protokolu:
            out.println(response);

            in.close();
            out.close();
            inRest.close();
            outRest.close();
            client.close();
            s.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String napraviOdogovor(String komanda, String mudrost) {
        String retVal = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";

        retVal += "<html><head><title>Odgovor servera</title></head>\n";
        retVal += "<body><h1>Mudrost dana: " + mudrost + "</h1>\n";
        retVal += "</body></html>";

        System.out.println("HTTP odgovor:");
        System.out.println(retVal);

        return retVal;
    }

}

