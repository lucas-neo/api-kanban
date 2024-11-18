package com.spring.kanban.service;

import com.spring.kanban.model.Tarefa;
import com.spring.kanban.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public Tarefa criarTarefa(Tarefa tarefa) {
        tarefa.setStatus("À Fazer");
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> listarTarefa() {
        return tarefaRepository.findAll();
    }

    public Tarefa editarTarefa(int id, Tarefa tarefa) {
        // Verifica se a tarefa com o ID especificado existe
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com ID: " + id));

        // Atualiza os campos da tarefa existente com os valores do JSON
        tarefaExistente.setTitulo(tarefa.getTitulo());
        tarefaExistente.setDescricao(tarefa.getDescricao());
        tarefaExistente.setStatus(tarefa.getStatus());
        tarefaExistente.setPrioridade(tarefa.getPrioridade());
        tarefaExistente.setDataLimite(tarefa.getDataLimite());

        // Salva e retorna a tarefa atualizada
        return tarefaRepository.save(tarefaExistente);
    }


    public Tarefa buscarTarefaPorId(int id) {
        return tarefaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarefa não existe"));
    }

    public void excluirTarefa(int id) {
        tarefaRepository.deleteById(id);
    }

    public Tarefa moverTarefa(int id) {
        Tarefa tarefa = buscarTarefaPorId(id);
        if (tarefa.getStatus().equals("À Fazer")) {
            tarefa.setStatus("Fazendo");
        } else if (tarefa.getStatus().equals("Fazendo")) {
            tarefa.setStatus("Feito");
        }
        return tarefaRepository.save(tarefa);
    }

    public Map<String, List<Tarefa>> listarPorColuna() {
        List<Tarefa> tarefas = listarTarefa();
        return tarefas.stream().collect(Collectors.groupingBy(Tarefa::getStatus));
    }

    public List<Tarefa> filtrarTarefas(String prioridade, Date dataLimite) {
        List<Tarefa> tarefas = tarefaRepository.findAll();
        return tarefas.stream()
                .filter(t -> (prioridade == null || t.getPrioridade().equalsIgnoreCase(prioridade)) &&
                        (dataLimite == null || !t.getDataLimite().after(dataLimite)))
                .collect(Collectors.toList());
    }

    public Map<String, Object> gerarRelatorio() {
        List<Tarefa> tarefas = listarTarefa();

        Map<String, List<Tarefa>> tarefasPorColuna = listarPorColuna();

        List<Tarefa> tarefasAtrasadas = tarefas.stream()
                .filter(t -> t.getDataLimite() != null && t.getDataLimite().before(new Date(System.currentTimeMillis()))
                        && !t.getStatus().equals("Feito"))
                .collect(Collectors.toList());

        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("tarefasPorColuna", tarefasPorColuna);
        relatorio.put("tarefasAtrasadas", tarefasAtrasadas);

        return relatorio;
    }
}
