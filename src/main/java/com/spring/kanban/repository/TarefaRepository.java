package com.spring.kanban.repository;

import com.spring.kanban.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.sql.Date;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

    List<Tarefa> findByPrioridade(String prioridade);

    List<Tarefa> findByDataLimiteBefore(Date dataLimite);
}
