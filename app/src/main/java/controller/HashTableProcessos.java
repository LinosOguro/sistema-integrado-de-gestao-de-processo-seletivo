package controller;

import br.elinos.HashTable.HashTable;
import model.Entidades.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class HashTableProcessos {

    private static class Entrada {
        String chave;
        Disciplina valor;
        Entrada proximo;

        Entrada(String chave, Disciplina valor) {
            this.chave = chave;
            this.valor = valor;
        }
    }

    private final Entrada[] tabela;
    private final int size;
    private int entradas;
    private final HashTable<String> libraryHash;

    public HashTableProcessos(int capacidade) {
        this.size = Math.max(3, capacidade);
        this.tabela = new Entrada[this.size];
        this.libraryHash = new HashTable<>(this.size);
    }

    public int hashFunction(String chave) {
        return Math.abs(chave.hashCode()) % size;
    }

    public void inserir(String chave, Disciplina valor) {
        int posicao = hashFunction(chave);
        Entrada atual = tabela[posicao];
        while (atual != null) {
            if (atual.chave.equals(chave)) {
                atual.valor = valor;
                return;
            }
            atual = atual.proximo;
        }
        Entrada novo = new Entrada(chave, valor);
        novo.proximo = tabela[posicao];
        tabela[posicao] = novo;
        entradas++;
        try {
            libraryHash.add(chave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Disciplina> listarTodos() {
        List<Disciplina> result = new ArrayList<>();
        for (Entrada e : tabela) {
            Entrada atual = e;
            while (atual != null) {
                result.add(atual.valor);
                atual = atual.proximo;
            }
        }
        return result;
    }

    public String obterEstatisticas() {
        return "Tamanho=" + size + ", Entradas=" + entradas + ", Fator de carga=" + (entradas / (double) size);
    }
}
