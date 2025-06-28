package com.github.kyungmin08g.zephyro.test.repository;

import com.github.kyungmin08g.zephyro.test.domain.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
