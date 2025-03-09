package com.github.lltal.observer.output;

import com.github.lltal.observer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByTgId(String tgId);

    void deleteByTgId(String tgId);

    @Query(value = "select usr.tg_id from usr", nativeQuery = true)
    Collection<String> findAllTgId();

    boolean existsByTgId(String tgId);
}
