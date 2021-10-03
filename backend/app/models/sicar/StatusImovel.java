package models.sicar;

import java.util.Arrays;

//Indica o status do imóvel (AT - Ativo; PE - Pendente; CA - Cancelado; SU - Suspenso).
public enum StatusImovel {

    AT("Ativo"),
    PE("Pendente"),
    CA("Cancelado"),
    SU("Suspenso");

    public String nome;

    StatusImovel(String nome) {
        this.nome = nome;
    }

    public static StatusImovel getInstance(String nome){
        return Arrays.stream(StatusImovel.values())
                .filter(s -> s.nome.equals(nome))
                .findFirst().orElse(null);
    }
}
