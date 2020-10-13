package message;

/*
 * Another brick in the wall.
 */


import java.io.Serializable;
import java.util.List;
import model.Kontakt;
import model.Korisnik;

/**
 *
 * @author Vladimir Cakic
 */
public class Message implements Serializable{
    
    private Poruka poruka;
    private Korisnik korisnik;
    private Kontakt kontakt;
    private List<Kontakt> listaKontakata;
    
    public Message(){  
    }
    
    public Message(Poruka poruka, Korisnik korisnik){
        this.poruka = poruka;
        this.korisnik = korisnik;
    }
    
    public Message(Poruka poruka, Kontakt kontakt){
        this.poruka = poruka;
        this.kontakt = kontakt;
    }
    
    public Message(Poruka poruka, List<Kontakt> listaKontakata){
        this.poruka = poruka;
        this.listaKontakata = listaKontakata;
    }
    
    public Message(Poruka poruka, Korisnik korisnik, Kontakt kontakt, List<Kontakt> listaKontakata){
        this.poruka = poruka;
        this.korisnik = korisnik;
        this.kontakt = kontakt;
        this.listaKontakata = listaKontakata;
    }

    /**
     * @return the kontakt
     */
    public Kontakt getKontakt() {
        return kontakt;
    }

    /**
     * @param kontakt the kontakt to set
     */
    public void setKontakt(Kontakt kontakt) {
        this.kontakt = kontakt;
    }

    /**
     * @return the listaKontakata
     */
    public List<Kontakt> getListaKontakata() {
        return listaKontakata;
    }

    /**
     * @param listaKontakata the listaKontakata to set
     */
    public void setListaKontakata(List<Kontakt> listaKontakata) {
        this.listaKontakata = listaKontakata;
    }

    /**
     * @return the poruka
     */
    public Poruka getPoruka() {
        return poruka;
    }

    /**
     * @param poruka the poruka to set
     */
    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }

    /**
     * @return the korisnik
     */
    public Korisnik getKorisnik() {
        return korisnik;
    }

    /**
     * @param korisnik the korisnik to set
     */
    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
 
}
