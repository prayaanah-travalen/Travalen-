package com.app.travelo.repositories;

import com.app.travelo.model.entity.PriceSlabEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceSlabRepository extends JpaRepository<PriceSlabEntity, Long> {

    PriceSlabEntity findByPriceSlab(Integer priceSlab);
}
