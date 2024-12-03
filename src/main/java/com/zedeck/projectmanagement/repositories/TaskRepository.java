package com.zedeck.projectmanagement.repositories;

import com.zedeck.projectmanagement.dtos.DashboardDto;
import com.zedeck.projectmanagement.models.Task;
import com.zedeck.projectmanagement.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {

    Optional<Task> findFirstById(Long id);

    Page<Task> findAllByDeletedFalse(Pageable pageable);

    @Query("SELECT task FROM Task task WHERE task.project_id.id = ?1 AND task.deleted = false")
    Page<Task> findAllByProject_id(Long projectId, Pageable pageable);

    Long countAllByDeletedFalse();

    @Query("SELECT t FROM Task t WHERE t.name = :name OR t.status = :status")
    List<Task> findByNameOrStatus(@Param("name") String name, @Param("status") String status);



    @Query(value = "SELECT t.status AS status, COUNT(t.id) AS count " +
            "FROM tasks t " +
            "WHERE t.deleted = false " +
            "GROUP BY t.status", nativeQuery = true)
    List<Object[]> countAllByStatus();





}
