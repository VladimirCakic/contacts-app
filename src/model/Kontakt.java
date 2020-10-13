/*
 * Another brick in the wall.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Vladimir Cakic
 */
public class Kontakt implements Comparable<Kontakt>, Serializable{
    
    private long id;
    private String ime;
    private String prezime;
    private String broj;
    private String adresa;
    private String tip;
    private long korisnikId;
    
    public Kontakt(){
        
    }
    
    public Kontakt(String ime, String prezime, String broj, String adresa, String tip, long korisnikID){
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.adresa = adresa;
        this.tip = tip;
        this.korisnikId = korisnikID;
    }
    
    public Kontakt(long id, String ime, String prezime, String broj, String adresa, String tip, long korisnikID){
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.adresa = adresa;
        this.tip = tip;
        this.korisnikId = korisnikID;
    }

    /**
     * @return the ime
     */
    public String getIme() {
        return ime;
    }

    /**
     * @param ime the ime to set
     */
    public void setIme(String ime) {
        this.ime = ime;
    }

 /**
     * @return the tip
     */
    public String getTip() {
        return tip;
    }

    /**
     * @param tip the tip to set
     */
    public void setTip(String tip) {
        this.tip = tip;
    }

    /**
     * @return the broj
     */
    public String getBroj() {
        return broj;
    }

    /**
     * @param broj the broj to set
     */
    public void setBroj(String broj) {
        this.broj = broj;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the adresa
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     * @param adresa the adresa to set
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    
    /**
     * @return the prezime
     */
    public String getPrezime() {
        return prezime;
    }

    /**
     * @param prezime the prezime to set
     */
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
    
      /**
     * @return the korisnikId
     */
    public long getKorisnikId() {
        return korisnikId;
    }

    /**
     * @param korisnikId the korisnikId to set
     */
    public void setKorisnikId(long korisnikId) {
        this.korisnikId = korisnikId;
    }
    
        @Override
    public String toString(){
        return this.getIme()  + " " + this.getBroj()+ " " + this.getTip();
    }

    @Override
    public boolean equals(Object o){
         if (o == this) { 
            return true; 
        } 
        if (!(o instanceof Kontakt)) { 
            return false; 
        }   
        Kontakt k2 = (Kontakt) o;   
        return ime.equals(k2.ime) && broj.equals(k2.broj) && tip.equals(k2.tip);
    }

    @Override
    public int compareTo(Kontakt k2) {
        if (this.equals(k2)){
            return 0;
        }else if(this.ime.compareTo(k2.ime) > 0){
            return 1;
        }else{
            return -1;
        }
        
    }

}