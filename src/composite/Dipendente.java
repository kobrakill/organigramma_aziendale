package composite;



import java.io.Serializable;
import java.util.*;


public class Dipendente implements ElementoOrganigramma, Serializable {
    //HO COSCIENZA DEL RUOLO CHE IL DIPENDENTE RICOPRE NELLE VARIE UNITA DOVE SI TROVA,
    //STO FACENDO IN MODO CHE IN UNA SINGOLA UNITA IL DIPENDENTE POSSA RICOPRIRE UN SOLO RUOLO
    protected HashMap<UnitaOrganizzativa,Ruolo> ruoli; // Ruoli assegnati per unità


    private String nome,cognome,citta,indirizzo;
    private int eta;
    private static final long serialVersionUID = 1L;

    public Dipendente(String nome, String cognome, String citta, String indirizzo, int eta){
        this.nome = nome;
        this.cognome = cognome;
        this.citta = citta;
        this.indirizzo = indirizzo;
        this.eta = eta;
        this.ruoli=new HashMap<>();
    }

    // Assegnare ruolo se l'unità lo ammette
    public void addRuolo(UnitaOrganizzativa unita, Ruolo ruolo) {
        if (unita.isRuoloAmmissibile(ruolo) && unita.EsisteDipendente(this)) {
            ruoli.put(unita, ruolo);
        } else {
            System.out.println("Il ruolo " + ruolo + " non è ammissibile per l'unità " + unita.getNome());
        }
    }

    public void removeRuolo(UnitaOrganizzativa unita, Ruolo ruolo) {
        ruoli.remove(unita, ruolo);
    }



    @Override
    public void add(ElementoOrganigramma ruolo) {
        throw new UnsupportedOperationException("Un dipendente necessita anche dell'unità organizzativa per assegnare un ruolo");
    }

    @Override
    public void remove(ElementoOrganigramma ruolo) {
        throw new UnsupportedOperationException("Un dipendente necessita anche dell'unità organizzativa per rimuovere un ruolo");
    }

    @Override
    public List<ElementoOrganigramma> getElementi() {
        return List.of();
    }


    // Getter e Setter
    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public Set<UnitaOrganizzativa> getUnitaDiCuiFaParte() {
        return ruoli.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dipendente dipendente = (Dipendente) o;
        return eta==dipendente.eta &&
                nome.equals(dipendente.nome) &&
                cognome.equals(dipendente.cognome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, cognome, eta, citta);
    }

    @Override
    public String toString() {
        return  "nome='" + nome + ' ' +
                ", cognome='" + cognome + ' ' +
                ", citta='" + citta + ' ' +
                ", indirizzo='" + indirizzo + ' ' +
                ", eta=" + eta;
    }

}
