package com.IIA.o2o_automatic_store_springmvc_java.repository.snack;

import com.IIA.o2o_automatic_store_springmvc_java.entity.snack.Snack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnackRepository extends JpaRepository<Snack, Long> {
}
