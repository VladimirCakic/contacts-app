package client;

/*
 * Another brick in the wall.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import message.Message;
import message.Poruka;
import model.Kontakt;
import model.Korisnik;

/**
 *
 * @author Vladimir Cakic
 */
public class ImenikApp extends Application {

    ObjectOutputStream toServer;
    ObjectInputStream fromServer;

    Korisnik korisnik = null;
    ObservableList<Kontakt> listaKontakata = null;
    Kontakt selectedKontakt = null;

    TextField korisnickoImeField;
    PasswordField sifraField;
    TextField imeField;
    TextField prezimeField;
    TextField brojField;
    TextField adresaField;
    TextField tipField;

    Button prijavaButton;
    Button registracijaButton;
    Button dodajButton;
    Button izmeniButton;
    Button izbrisiButton;

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        //Gornja strana (Korisnicki podaci i dugmici za prijavu i registraciju)
        HBox topArea = new HBox();
        topArea.setPadding(new Insets(10));
        topArea.setSpacing(15);
        topArea.setAlignment(Pos.BASELINE_RIGHT);
        root.setTop(topArea);

        korisnickoImeField = new TextField();
        korisnickoImeField.setPromptText("Korisnicko ime");
        sifraField = new PasswordField();
        sifraField.setPromptText("sifra");
        prijavaButton = new Button("Prijava");
        registracijaButton = new Button("Registracija");
        topArea.getChildren().addAll(korisnickoImeField, sifraField, prijavaButton, registracijaButton);

        //leva strana (Polja za unos i dugmici)
        GridPane leftArea = new GridPane();
        leftArea.setPadding(new Insets(10));
        leftArea.setHgap(15);
        leftArea.setVgap(15);
        root.setLeft(leftArea);

        Label imeLabel = new Label("Ime:");
        imeField = new TextField();
        leftArea.add(imeLabel, 1, 1);
        leftArea.add(imeField, 2, 1);

        Label prezimeLabel = new Label("Prezime:");
        prezimeField = new TextField();
        leftArea.add(prezimeLabel, 1, 2);
        leftArea.add(prezimeField, 2, 2);

        Label brojLabel = new Label("Telefon:");
        brojField = new TextField();
        leftArea.add(brojLabel, 1, 3);
        leftArea.add(brojField, 2, 3);

        Label adresaLabel = new Label("Adresa:");
        adresaField = new TextField();
        leftArea.add(adresaLabel, 1, 4);
        leftArea.add(adresaField, 2, 4);

        Label tipLabel = new Label("Tip:");
        tipField = new TextField();
        leftArea.add(tipLabel, 1, 5);
        leftArea.add(tipField, 2, 5);

        Region emptySpace = new Region();
        emptySpace.setPrefHeight(80);
        leftArea.add(emptySpace, 1, 6);

        dodajButton = new Button("Dodaj");
        dodajButton.setPrefSize(140, 50);
        leftArea.add(dodajButton, 1, 7);

        izmeniButton = new Button("Izmeni");
        izmeniButton.setPrefSize(140, 50);
        leftArea.add(izmeniButton, 1, 8);

        izbrisiButton = new Button("Izbrisi");
        izbrisiButton.setPrefSize(140, 50);
        leftArea.add(izbrisiButton, 1, 9);

        //Desna strana (tabela sa prikazom cokolada)
        TableView table = new TableView();
        root.setCenter(table);

        TableColumn imeCol = new TableColumn("Ime");
        imeCol.setCellValueFactory(new PropertyValueFactory("ime"));
        TableColumn prezimeCol = new TableColumn("Prezime");
        prezimeCol.setCellValueFactory(new PropertyValueFactory("prezime"));
        TableColumn brojCol = new TableColumn("Telefon");
        brojCol.setCellValueFactory(new PropertyValueFactory("broj"));
        TableColumn adresaCol = new TableColumn("Adresa");
        adresaCol.setCellValueFactory(new PropertyValueFactory("adresa"));
        TableColumn tipCol = new TableColumn("Tip");
        tipCol.setCellValueFactory(new PropertyValueFactory("tip"));

        table.getColumns().setAll(imeCol, prezimeCol, brojCol, adresaCol, tipCol);
        table.setPrefWidth(450);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Dno prozora (Prikazuje razlicite poruke)
        HBox bottomArea = new HBox();
        bottomArea.setAlignment(Pos.BASELINE_RIGHT);
        bottomArea.setPadding(new Insets(10));
        Label obavestenjeLabel = new Label("Da biste mogli da koristite aplikaciju neophodno je "
                + "da se prijavite ili registrujete");
        bottomArea.getChildren().add(obavestenjeLabel);
        root.setBottom(bottomArea);

        // Postavljanje scene i prikaz pozornice
        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setTitle("Imenik");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Kontrola akcija
        table.getSelectionModel().selectedIndexProperty().addListener(
                (ov, oldVal, newVal) -> {
                    int index = newVal.intValue();
                    if (listaKontakata == null || index < 0 || index >= listaKontakata.size()) {
                        return;
                    }
                    selectedKontakt = listaKontakata.get(index);
                    imeField.setText(selectedKontakt.getIme());
                    prezimeField.setText(selectedKontakt.getPrezime());
                    brojField.setText(selectedKontakt.getBroj());
                    adresaField.setText(selectedKontakt.getAdresa());
                    tipField.setText(selectedKontakt.getTip());
                });

        dodajButton.setOnAction(e -> {
            if (korisnik == null) {
                return;
            }
            String ime = imeField.getText();
            String prezime = prezimeField.getText();
            String broj = brojField.getText();
            String adresa = adresaField.getText();
            String tip = tipField.getText();
            Kontakt kontaktToAdd = new Kontakt(ime, prezime, broj, adresa, tip, korisnik.getId());
            try {
                toServer.writeObject(new Message(Poruka.DODAJ, kontaktToAdd));
                Message messageFromServer = (Message) fromServer.readObject();
                listaKontakata = FXCollections.observableList(messageFromServer.getListaKontakata());
                table.setItems(listaKontakata);
                obavestenjeLabel.setText("Uspesno ste dodali novi kontakt");

            } catch (IOException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            emptyFields();
        });

        izbrisiButton.setOnAction(e -> {
            if (selectedKontakt == null || korisnik == null) {
                return;
            }
            try {
                toServer.writeObject(new Message(Poruka.IZBRISI, selectedKontakt));
                Message messageFromServer = (Message) fromServer.readObject();
                listaKontakata = FXCollections.observableList(messageFromServer.getListaKontakata());
                table.setItems(listaKontakata);
                obavestenjeLabel.setText("Uspesno ste izbrisali jedan kontakt");
            } catch (IOException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            emptyFields();
        });

        izmeniButton.setOnAction(e -> {
            if (selectedKontakt == null || korisnik == null) {
                return;
            }
            String ime = imeField.getText();
            String prezime = prezimeField.getText();
            String broj = brojField.getText();
            String adresa = adresaField.getText();
            String tip = tipField.getText();
            Kontakt kontaktToUpdate = new Kontakt(selectedKontakt.getId(), ime, prezime, broj, adresa, tip, korisnik.getId());
            try {
                toServer.writeObject(new Message(Poruka.IZMENI, kontaktToUpdate));
                Message messageFromServer = (Message) fromServer.readObject();
                listaKontakata = FXCollections.observableList(messageFromServer.getListaKontakata());
                table.setItems(listaKontakata);
                obavestenjeLabel.setText("Uspesno ste izmenili jedan kontakt");
            } catch (IOException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            emptyFields();
        });

        prijavaButton.setOnAction(e -> {
            String korisnickoIme = korisnickoImeField.getText();
            String lozinka = sifraField.getText();
            try {
                toServer.writeObject(new Message(Poruka.PRIJAVA, new Korisnik(korisnickoIme, lozinka)));
                toServer.flush();
                Message messageFromServer = (Message)fromServer.readObject();
                if (messageFromServer.getListaKontakata() != null) {
                    korisnik = new Korisnik(korisnickoIme, lozinka);
                    listaKontakata = FXCollections.observableList(messageFromServer.getListaKontakata());
                    table.setItems(listaKontakata);
                    obavestenjeLabel.setText("Uspesno ste se prijavili na Vas nalog");
                } else {
                    obavestenjeLabel.setText("Korisnicko ime ili lozinka nisu ispravni, probajte ponovo da se prijavite");
                }
            } catch (IOException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        registracijaButton.setOnAction(e -> {
            String korisnickoIme = korisnickoImeField.getText();
            String lozinka = sifraField.getText();
            try {
                toServer.writeObject(new Message(Poruka.REGISTRACIJA, new Korisnik(korisnickoIme, lozinka)));
                toServer.flush();
                Message messageFromServer = (Message) fromServer.readObject();
                if (messageFromServer.getListaKontakata() != null) {
                    korisnik = new Korisnik(korisnickoIme, lozinka);
                    listaKontakata = FXCollections.observableList(messageFromServer.getListaKontakata());
                    table.setItems(listaKontakata);
                    obavestenjeLabel.setText("Uspesno ste se registrovali");
                } else {
                    obavestenjeLabel.setText("Korisnicko ime je vec u upotrebi, probajte ponovo sa drugim");
                }
            } catch (IOException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ImenikApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        try {
            //kreiranje utičnice na klijet strani
            Socket uticnica = new Socket("localhost", 8000);

            //izlazi tok za slanje ka serveru
            toServer = new ObjectOutputStream(uticnica.getOutputStream());

            //ulazni tok za prijem sa servera 
            fromServer = new ObjectInputStream(uticnica.getInputStream());
        } catch (IOException ex) {
            System.err.println(ex);

        }
    }

    private void emptyFields() {
        imeField.setText("");
        prezimeField.setText("");
        brojField.setText("");
        adresaField.setText("");
        tipField.setText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
