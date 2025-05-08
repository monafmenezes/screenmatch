package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,

                         @JsonAlias("totalSeasons") Integer totalTemporadas,

                         @JsonAlias("Genre") String genero,

                         @JsonAlias("Actors") String atores,

                         @JsonAlias("Poster") String poster,

                         @JsonAlias("Plot") String sinopse,

                         @JsonAlias("imdbRating") String avaliacao) {
}
