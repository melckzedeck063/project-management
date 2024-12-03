package com.zedeck.projectmanagement.repositories;

import com.zedeck.projectmanagement.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findFirstByUsername(String username);


    Optional<UserAccount> findFirstByUuid(String uuid);

    Long countAllByDeletedIsFalse();



}
