package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Categoria;

public record SerieDTO(
        Long id,
        String titulo,
        Integer totalTemporadas,
        Categoria genero,
        String atores,
        String poster,
        String sinopse,
        Double avaliacao) {
}
