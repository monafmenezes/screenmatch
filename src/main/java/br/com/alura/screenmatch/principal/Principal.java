package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\n Top 5 Episódios: ");

        dadosEpisodios.stream()
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .limit(5)
                .forEach(System.out::println);


        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episódios? ");

        var ano = leitura.nextInt();

        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                        "Episódio: " + e.getTitulo() +
                        "Data lançamento: " + e.getDataLancamento().format(formatter)
                        ));

/*        Codigos comentatos a nível de conhecimento
        for (DadosTemporada item: temporadas) {
                for (DadosEpisodio episodio : item.episodios()) {
                    System.out.println(episodio.titulo());
                }
        }

        List<String> nomes = Arrays.asList("Jacque", "Yasmin", "Paulo", "Rodrigo", "Nico");

        // stream - operações encadeadas
        nomes.stream()
                .sorted() //operação intermediária
                .limit(3) //operação intermediária
                .filter(n -> n.startsWith("N")) //operação intermediária
                .map(n -> n.toUpperCase()) //operação intermediária
                .forEach(System.out::println); //operação final*/

    }
}
