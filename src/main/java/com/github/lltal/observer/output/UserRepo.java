package com.github.lltal.observer.output;

import com.github.lltal.observer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByTgId(String tgId);

    void deleteByTgId(String tgId);
}
