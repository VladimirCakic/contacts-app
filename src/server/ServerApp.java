/*
 * Another brick in the wall.
 */
package server;

import crud.KontaktCRUD;
import crud.KorisnikCRUD;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import message.Message;
import message.Poruka;
import model.Kontakt;
import model.Korisnik;

/**
 *
 * @author Vladimir Cakic
 */
public class ServerApp extends Application {

    // Prostor za tekst za prikaz sadržaja
    private TextArea ta = new TextArea();
// Broj klijenata
    private int clientNo = 0;

    @Override // Redefinasnje metoda start() klase Application.
    public void start(Stage primaryStage) {
// Kreiranje scene i njeno postavljanje na pozornicu.
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Višenitni server"); // Unos naslova pozornice
        primaryStage.setScene(scene); // Postavljanje scene na pozornicu
        primaryStage.show(); // Prikaz pozornice
        new Thread(() -> {
            try {
// Kreiranje utičnice servera
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> {
                    ta.appendText("Višenitni server je pokrenut u "
                            + new Date() + '\n');
                });
                while (true) {
// Osluškivanje zahteva za novom vezom
                    Socket socket = serverSocket.accept();
// Povećanje broja klijenta
                    clientNo++;
                    Platform.runLater(() -> {
// Prikaz broja klijenta
                        ta.appendText("Pokretanje niti za klijenta " + clientNo
                                + " u " + new Date() + '\n');
// Pronalaženje naziva i IP adrese računara klijenta
                        InetAddress inetAddress = socket.getInetAddress();
                        ta.appendText("Klijent " + clientNo + " - host adresa : "
                                + inetAddress.getHostName() + "\n");
                        ta.appendText("Klijent " + clientNo + " IP Address : "
                                + inetAddress.getHostAddress() + "\n");
                    });
// Kreiranje i startovanje nove niti za vezu sa klijentom
                    new Thread(new HandleAClient(socket)).start();
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }

    // Definisanje klase niti za rad sa novom vezom.
    class HandleAClient implements Runnable {

        private Socket socket; // Povezana utičnica

        KontaktCRUD kontaktCrud = new KontaktCRUD();
        KorisnikCRUD korisnikCrud = new KorisnikCRUD();
        Korisnik korisnik = null;
        Message outputMessage = null;
        Message inputMessage = null;

        /**
         * Konstruisanje niti
         */
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        /**
         * Izvršavanje niti
         */
        @Override
        public void run() {
            try {
// Kreiranje ulaznog i izlatnog toka podataka  
                
                ObjectOutputStream outputToClient = new ObjectOutputStream(
                        socket.getOutputStream());
                ObjectInputStream inputFromClient = new ObjectInputStream(
                        socket.getInputStream());
                
// Kontinualno služenje klijenta
                while (true) {
// Dobijanje poruke od klijenta
                    inputMessage = (Message) inputFromClient.readObject();
// Obradjivanje poruke s obzirom na njenu sadrzinu
                    switch (inputMessage.getPoruka()) {
                        case DODAJ:
                            outputMessage = obradiZahtevZaDodavanjem(inputMessage);
                            break;
                        case IZBRISI:
                            outputMessage = obradiZahtevZaBrisanjem(inputMessage);
                            break;
                        case IZMENI:
                            outputMessage = obradiZahtevZaIzmenom(inputMessage);
                            break;
                        case PRIJAVA:
                            outputMessage = obradiZahtevZaPrijavom(inputMessage);
                            break;
                        case REGISTRACIJA:
                            outputMessage = obradiZahtevZaRegistracijom(inputMessage);
                            break;
                    }  
                    outputToClient.writeObject(outputMessage);
                    //outputToClient.flush();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private Message obradiZahtevZaDodavanjem(Message inputMessage) {
            kontaktCrud.insertKontakt(inputMessage.getKontakt(), korisnik);
            List<Kontakt> listaKontakata = kontaktCrud.selectAllFromKontakt(korisnik);
            return new Message(Poruka.DODAJ, listaKontakata);
        }

        private Message obradiZahtevZaBrisanjem(Message inputMessage) {
            kontaktCrud.deleteKontakt(inputMessage.getKontakt());
            List<Kontakt> listaKontakata = kontaktCrud.selectAllFromKontakt(korisnik);
            return new Message(Poruka.IZBRISI, listaKontakata);
        }

        private Message obradiZahtevZaIzmenom(Message inputMessage) {
            kontaktCrud.updateKontakt(inputMessage.getKontakt());
            List<Kontakt> listaKontakata = kontaktCrud.selectAllFromKontakt(korisnik);
            return new Message(Poruka.IZMENI, listaKontakata);
        }
        
// Metod vraca Message objek koji sadrzi ili listu kontakata 
// ili null ukoliko korisnicki podaci (ime i sifra) nisu ispravni
        private Message obradiZahtevZaPrijavom(Message inputMessage) {
            korisnik = korisnikCrud.selectKorisnik(inputMessage.getKorisnik());
            List<Kontakt> listaKontakata = null;
            if (korisnik != null){
                listaKontakata = kontaktCrud.selectAllFromKontakt(korisnik);
            }
            return new Message(Poruka.PRIJAVA, listaKontakata);
        }
        
// Metod vraca poruku koja sadrzi ili praznu listu kontakata ukoliko je registracija uspesna,
// ili listu kontakata koja ima null pointar, ukoliko vec postoji korisnik sa istim korisnickim imenom
        private Message obradiZahtevZaRegistracijom(Message inputMessage) {
            List<Kontakt> listaKontakata = null;
            if (!korisnikCrud.doesExistKorisnikoIme(inputMessage.getKorisnik())){
                korisnikCrud.insertKorisnik(inputMessage.getKorisnik());
                korisnik = korisnikCrud.selectKorisnik(inputMessage.getKorisnik());
                listaKontakata = kontaktCrud.selectAllFromKontakt(korisnik);
            }
            return new Message(Poruka.REGISTRACIJA, listaKontakata);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
