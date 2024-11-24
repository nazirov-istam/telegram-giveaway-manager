package com.telegram_giveaway_manager.telegram_giveaway_manager.repository;

import com.telegram_giveaway_manager.telegram_giveaway_manager.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findById(long id);
}
