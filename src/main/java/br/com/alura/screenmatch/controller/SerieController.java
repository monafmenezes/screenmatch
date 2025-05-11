package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    private SerieService service;

    @GetMapping()
    public List<SerieDTO> obterSeries() {
        return service.obterTodasAsSeries();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos() {
        return service.obterLancamentos();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5() {
        return service.obterTop5();
    }

    @GetMapping("/{id}")
    public SerieDTO obterDetalhes(@PathVariable Long id) {
        return service.obterSerie(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return service.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporada(@PathVariable Long id, @PathVariable Long numero){
        return service.obterTemporadasPorNumero(id, numero);
    }

    @GetMapping("/categoria/{categoria}")
    public List<SerieDTO> obterSeriesPorCategoria(@PathVariable String categoria){
        return service.obterSeriesPorCategoria(categoria);
    }





}
