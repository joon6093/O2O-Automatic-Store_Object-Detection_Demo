package com.iia.store.repository.store;

import com.iia.store.entity.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>{

    List<Store> findByMemberId(Long memberId);
    Optional<Store> findByIdAndMemberId(Long storeId, Long memberId);
    @Query("SELECT s FROM Store s JOIN FETCH s.member LEFT JOIN FETCH s.image WHERE s.id = :id")
    Optional<Store> findByIdWithMemberAndImage(Long id);

}
