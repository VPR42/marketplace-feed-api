package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.FavouriteJobEntity;
import com.vpr42.marketplacefeedapi.model.entity.keys.FavouriteKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteJobRepository extends JpaRepository<FavouriteJobEntity, FavouriteKey> {
}
