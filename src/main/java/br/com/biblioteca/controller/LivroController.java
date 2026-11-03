package br.com.biblioteca.controller;

import br.com.biblioteca.model.Livro;
import br.com.biblioteca.repository.LivroRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroRepository repository;

    public LivroController(LivroRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Livro> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Livro buscarPorId(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> livroNaoEncontrado(id));
    }

    @PostMapping
    public ResponseEntity<Livro> criar(@Valid @RequestBody Livro livro) {
        if (repository.existsByIsbn(livro.getIsbn())) {
            throw isbnDuplicado(livro.getIsbn());
        }
        livro.setId(null);
        Livro salvo = repository.save(livro);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public Livro atualizar(@PathVariable Long id, @Valid @RequestBody Livro dados) {
        Livro livro = repository.findById(id).orElseThrow(() -> livroNaoEncontrado(id));

        if (repository.existsByIsbnAndIdNot(dados.getIsbn(), id)) {
            throw isbnDuplicado(dados.getIsbn());
        }

        livro.setTitulo(dados.getTitulo());
        livro.setAutor(dados.getAutor());
        livro.setIsbn(dados.getIsbn());
        livro.setAnoPublicacao(dados.getAnoPublicacao());
        return repository.save(livro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw livroNaoEncontrado(id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseStatusException livroNaoEncontrado(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro " + id + " nao encontrado");
    }

    private ResponseStatusException isbnDuplicado(String isbn) {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe um livro com o ISBN " + isbn);
    }
}
