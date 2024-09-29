package composite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UnitaOrganizzativa implements ElementoOrganigramma, Serializable {
    private String nome;
    private List<ElementoOrganigramma> elementi;    //unità organizzativa dentro un'altra unità organizzativa
    private List<Ruolo> ruoliAmmissibili;  // Ruoli ammissibili per l'unità
    private List<Dipendente> dipendentiUnita;

    public UnitaOrganizzativa(String nome) {
        this.nome = nome;
        this.elementi = new ArrayList<>();
        this.ruoliAmmissibili = new ArrayList<>();
        this.dipendentiUnita = new ArrayList<>();
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }


    @Override
    //ad una unità posso aggiungere altre unità
    public void add(ElementoOrganigramma elemento) {
        if( !(elemento instanceof Ruolo) && !elementi.contains(elemento) && !(elemento instanceof Dipendente)) {
            elementi.add(elemento);
        }
    }

    @Override
    public void remove(ElementoOrganigramma elemento) {
            elementi.remove(elemento);
    }

    @Override
    public List<ElementoOrganigramma> getElementi() {
        return elementi;
    }

    //Aggiungere e rimuovere dipendenti
    public void aggiungiDipendente(Dipendente d) {
        if(!dipendentiUnita.contains(d)) {
            dipendentiUnita.add(d);
        }

    }

    public void rimuoviDipendente(Dipendente d) {
            dipendentiUnita.remove(d);
    }

    public boolean EsisteDipendente(Dipendente d) {
        return dipendentiUnita.contains(d);
    }


    // Aggiungere e rimuovere ruoli ammissibili

    public void aggiungiRuoloAmmissibile(Ruolo ruolo) {
        if (!ruoliAmmissibili.contains(ruolo)) {
            ruoliAmmissibili.add(ruolo);
        }
    }

    public void rimuoviRuoloAmmissibile(Ruolo ruolo) {
        ruoliAmmissibili.remove(ruolo);
    }

    public boolean isRuoloAmmissibile(Ruolo ruolo) {
        return ruoliAmmissibili.contains(ruolo);
    }

    public String toString(){
        return nome;
    }

}
