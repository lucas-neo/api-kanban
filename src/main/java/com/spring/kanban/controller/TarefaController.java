package com.spring.kanban.controller;

import com.spring.kanban.model.Tarefa;
import com.spring.kanban.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kanban")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa) {
        return tarefaService.criarTarefa(tarefa);
    }

    @GetMapping
    public Map<String, List<Tarefa>> listarPorColuna() {
        return tarefaService.listarPorColuna();
    }

    @GetMapping("/filtrar")
    public List<Tarefa> filtrarTarefas(@RequestParam(required = false) String prioridade,
                                       @RequestParam(required = false) Date dataLimite) {
        return tarefaService.filtrarTarefas(prioridade, dataLimite);
    }

    @PutMapping("/{id}/mover")
    public Tarefa moverTarefa(@PathVariable int id) {
        return tarefaService.moverTarefa(id);
    }

    @PutMapping("/{id}/editar")
    public Tarefa editar(@PathVariable int id, @RequestBody Tarefa tarefa) {
        // Garante que o ID do path seja usado
        return tarefaService.editarTarefa(id, tarefa);
    }


    @GetMapping("/{id}")
    public Tarefa listarTarefaPorId(@PathVariable int id) {
        return tarefaService.buscarTarefaPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarTarefaPorId(@PathVariable int id) {
        tarefaService.excluirTarefa(id);
        return ResponseEntity.ok("Deletado com sucesso");
    }

    @GetMapping("/report")
    public Map<String, Object> gerarRelatorio() {
        return tarefaService.gerarRelatorio();
    }
}
