package com.zedeck.projectmanagement.repositories;

import com.zedeck.projectmanagement.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long> {


    Optional<Project> findFirstById(Long aLong);

    Page<Project>  findAllByDeletedFalse(Pageable pageable);

    Long countAllByDeletedFalse();

}
