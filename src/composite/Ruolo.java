package composite;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

//foglia della classe ElementoOrganigramma (leaf di composite), non può avere figli
public class Ruolo implements ElementoOrganigramma,Serializable {

    //il nome del ruolo è la chiave
    private String nome,descrizione,requisiti;
    private Number stipendio;
    private static final long serialVersionUID = 1L;

    public Ruolo(String nome, String descrizione,String requisiti, Number stipendio) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.requisiti = requisiti;
        this.stipendio = stipendio;
    }
    @Override
    public void add(ElementoOrganigramma elemento) {
        throw new UnsupportedOperationException("Un ruolo non può contenere altri elementi.");
    }

    @Override
    public void remove(ElementoOrganigramma elemento) {
        throw new UnsupportedOperationException("Un ruolo non può contenere altri elementi.");
    }

    @Override
    public List<ElementoOrganigramma> getElementi() {
        throw new UnsupportedOperationException("Un ruolo non contiene altri elementi.");
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getRequisiti() {
        return requisiti;
    }

    public void setRequisiti(String requisiti) {
        this.requisiti = requisiti;
    }

    public Number getStipendio() {
        return stipendio;
    }

    public void setStipendio(Number stipendio) {
        this.stipendio = stipendio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruolo ruolo = (Ruolo) o;
        return Objects.equals(nome, ruolo.nome) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, descrizione, requisiti, stipendio);
    }

    @Override
    public String toString() {
        return   nome +": "+ "\n" +
                " descrizione=" + descrizione +" "+ "\n" +
                ", requisiti=" + requisiti +" "+ "\n" +
                ", stipendio=" + stipendio ;
    }


}
