package com.mycompany.agendaeletronica;

import java.util.ArrayList;
import java.util.List;

public class Agenda {

    private List<Contato> contatos;

    public Agenda() {
        contatos = new ArrayList<>();
    }

    public void cadastrar(Contato contato) {
        contatos.add(contato);
    }

    public Contato buscarContato(String cpf) {
        for (Contato c : contatos) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }

    public void excluirContato(String cpf) {
        Contato c = buscarContato(cpf);
        if (c != null) {
            contatos.remove(c);
        }
    }

    public List<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
    }
}
