/*
 * Another brick in the wall.
 */
package crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Kontakt;
import model.Korisnik;

/**
 *
 * @author Vladimir Cakic
 */
public class KorisnikCRUD {

    DBConnection DB = new DBConnection();

    public void insertKorisnik(Korisnik korisnik) {
        Connection con = DB.open();
        String sql = "INSERT INTO korisnik(korisnicko_ime, sifra) VALUES (?, ?)";
        try (PreparedStatement insertKorisnik = con.prepareStatement(sql);) {
            insertKorisnik.setString(1, korisnik.getKorisnickoIme());
            insertKorisnik.setString(2, korisnik.getLozinka());
            insertKorisnik.executeUpdate();
            DB.close();
        } catch (SQLException ex) {
            Logger.getLogger(KorisnikCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Vraca iz baze korisnika ukoliko postoji takav sa odgovarajucim korisnickim imenom
    //i sifrom, u suprotnom vraca null vrednost
    public Korisnik selectKorisnik(Korisnik korisnik) {
        Korisnik toReturn = null;
        String korisnickoIme = korisnik.getKorisnickoIme();
        String sifra = korisnik.getLozinka();
        Connection con = DB.open();
        String sql = "SELECT * FROM korisnik WHERE korisnicko_ime = '" + korisnickoIme
                + "' AND sifra = '" + sifra + "'";
        try (Statement stm = con.createStatement();) {
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()) {
                long id = rs.getLong(1);
                String korisnicko = rs.getString(2);
                String lozinka = rs.getString(3);
                toReturn = new Korisnik(id, korisnicko, lozinka);
            }

            DB.close();
        } catch (SQLException ex) {
            Logger.getLogger(KontaktCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toReturn;
    }

    public boolean doesExistKorisnikoIme(Korisnik korisnik) {
        String korisnickoIme = korisnik.getKorisnickoIme();
        Connection con = DB.open();
        String sql = "SELECT * FROM korisnik WHERE korisnicko_ime = '" + korisnickoIme + "'";
        try (Statement stm = con.createStatement();) {
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()) {
                return true;
            }
            DB.close();
        } catch (SQLException ex) {
            Logger.getLogger(KontaktCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
