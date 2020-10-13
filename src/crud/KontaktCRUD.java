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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Kontakt;
import model.Korisnik;

/**
 *
 * @author Vladimir Cakic
 */
public class KontaktCRUD {

    DBConnection DB = new DBConnection();

    //Metod dodaje kontakt odredjenog korisnika u bazu
    public void insertKontakt(Kontakt kontakt, Korisnik korisnik) {
        Connection con = DB.open();
        String sql = "INSERT INTO kontakt(ime, prezime, broj, adresa, tip, id_korisnika) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertKontakt = con.prepareStatement(sql);) {
            insertKontakt.setString(1, kontakt.getIme());
            insertKontakt.setString(2, kontakt.getPrezime());
            insertKontakt.setString(3, kontakt.getBroj());
            insertKontakt.setString(4, kontakt.getAdresa());
            insertKontakt.setString(5, kontakt.getTip());
            insertKontakt.setLong(6, korisnik.getId());
            insertKontakt.executeUpdate();
            DB.close();
        } catch (SQLException ex) {
            Logger.getLogger(KontaktCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Metod iscitava iz baze sve kontakte odredjenog korisnika
    public List<Kontakt> selectAllFromKontakt(Korisnik korisnik) {
        ArrayList<Kontakt> toReturn = new ArrayList<>();
        Connection con = DB.open();
        long id_korisnika = korisnik.getId();
        String sql = "SELECT * FROM kontakt WHERE id_korisnika = " + id_korisnika;
        try (Statement stm = con.createStatement();) {
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                long id = rs.getLong(1);
                String ime = rs.getString(2);
                String prezime = rs.getString(3);
                String broj = rs.getString(4);
                String adresa = rs.getString(5);
                String tip = rs.getString(6);
                long idKorisnika = rs.getLong(7);
                Kontakt kontakt = new Kontakt(id, ime, prezime, broj, adresa, tip, idKorisnika);
                toReturn.add(kontakt);
            }
            DB.close();
        } catch (SQLException ex) {
            Logger.getLogger(KontaktCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toReturn;
    }

    public void deleteKontakt(Kontakt kontakt) {
        Connection con = DB.open();
        long id = kontakt.getId();
        String sql = "DELETE FROM kontakt WHERE id = " + id;
        try (Statement stm = con.createStatement();) {
            stm.executeUpdate(sql);
            DB.close();
        } catch (SQLException ex) {
            Logger.getLogger(KontaktCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateKontakt(Kontakt kontakt) {
        Connection con = DB.open();
        long id = kontakt.getId();
        String ime = kontakt.getIme();
        String prezime = kontakt.getPrezime();
        String broj = kontakt.getBroj();
        String adresa = kontakt.getAdresa();
        String tip = kontakt.getTip();
        String sql = "UPDATE kontakt SET ime = '" + ime + "' , prezime = '" + prezime + "' , broj = '" + broj
                + "' , adresa = '" + adresa + "' , tip = '" + tip + "' WHERE id = " + id;
        try (Statement stm = con.createStatement();) {
            stm.executeUpdate(sql);
            DB.close();
        } catch (SQLException ex) {
            Logger.getLogger(KontaktCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
