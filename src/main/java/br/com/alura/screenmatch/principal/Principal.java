package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=%s".formatted(System.getenv("API_KEY"));
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    private  List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {

        var opcao = -1;

        while (opcao != 0) {

            var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                4 - Buscar série por titulo
                5 - Buscar séries por ator
                6 - Top 5 séries
                7 - Buscar série por categoria
                8 - Buscar série por número de temporadas
                9 - Buscar episódio por trecho
                10 - Top 5 episódios por série
                11 - Buscar eposódios a partir de uma data
            
                0 - Sair
                """;

            System.out.println(menu);

            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorNumeroTemporadas();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    buscarTop5EpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosPorData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }

    }

    private void buscarEpisodiosPorData() {
        buscarSeriePorTitulo();

        if (serieBusca.isPresent()) {
            System.out.println("Digite o ano limite de lançamento: ");
            var ano = leitura.nextInt();
            leitura.nextLine();

            Serie serie = serieBusca.get();
            List<Episodio> episodiosAno = repository.episodiosPorData(serie, ano);

            episodiosAno.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumero(), e.getTitulo()));
        }
    }

    private void buscarTop5EpisodiosPorSerie() {
        buscarSeriePorTitulo();

        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);

            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumero(), e.getTitulo()));
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio para busca?");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodiosPorTrecho(trechoEpisodio);

        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumero(), e.getTitulo()));
    }

    private void buscarSeriesPorNumeroTemporadas() {
        System.out.println("Qual o número máximo de temporadas?");
        var numeroTemporadas = leitura.nextInt();
        System.out.println("Digite uma avaliação mínima: ");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriePorNumTemporadas = repository.seriesPorTemporadaEAValiacao(numeroTemporadas, avaliacao);

        seriePorNumTemporadas.forEach(s ->
                System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao() + " Número de temporadas" + s.getTotalTemporadas()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar por qual categoria/gênero? ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);

        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);

        seriesPorCategoria.forEach(s ->
                System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao() + " Gênero:" + s.getGenero()));
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop = repository.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s ->
                System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite um ator para localizar a série: ");
        var ator = leitura.nextLine();
        System.out.println("Digite uma avaliação mínima: ");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(ator, avaliacao);

        System.out.println("Séries em que " + ator + " trabalhou: ");

        seriesEncontradas.forEach(s ->
                        System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite uma palavra ou um título para localizar a série: ");
        var titulo = leitura.nextLine();
        this.serieBusca = repository.findByTituloContainingIgnoreCase(titulo);

        if (serieBusca.isPresent()) {
            System.out.println(serieBusca);
        } else {
            System.out.println("Série não localizada!");
        }
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        this.repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            List<DadosTemporada> temporadas = new ArrayList<>();
            var serieEncontrada = serie.get();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios =  temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .toList();

            serieEncontrada.setEpisodios(episodios);

            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }

    }
}