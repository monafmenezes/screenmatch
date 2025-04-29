package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumoApi = new ConsumoApi();

    private  ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=%s".formatted(System.getenv("API_KEY"));

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca:");

        var nomeSerie = leitura.nextLine().replace(" ", "+");
        var url = ENDERECO + nomeSerie + API_KEY;
        var json = consumoApi.obterDados(url);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas() ; i++) {
            url = ENDERECO + nomeSerie + "&season=" + i + API_KEY;
            json = consumoApi.obterDados(url);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

//      Achei que eu tinha simplificado aqui kkkkk
//        for (DadosTemporada item: temporadas) {
//                for (DadosEpisodio episodio : item.episodios()) {
//                    System.out.println(episodio.titulo());
//                }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }
}
