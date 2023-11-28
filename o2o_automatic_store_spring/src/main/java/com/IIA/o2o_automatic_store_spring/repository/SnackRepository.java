package com.IIA.o2o_automatic_store_spring.repository;

import com.IIA.o2o_automatic_store_spring.entity.Snack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnackRepository extends JpaRepository<Snack, Long> {
}
