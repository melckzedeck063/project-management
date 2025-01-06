package com.zedeck.projectmanagement.repositories;

import com.zedeck.projectmanagement.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
        select t from tokens t where t.user.id = :id and (t.expired = false or t.revoked = false)
        """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}

