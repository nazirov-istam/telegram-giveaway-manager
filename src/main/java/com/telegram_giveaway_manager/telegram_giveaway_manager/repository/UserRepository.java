package com.telegram_giveaway_manager.telegram_giveaway_manager.repository;

import com.telegram_giveaway_manager.telegram_giveaway_manager.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
