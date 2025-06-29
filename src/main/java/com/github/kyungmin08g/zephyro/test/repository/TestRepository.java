package com.github.kyungmin08g.zephyro.test.repository;

import com.github.kyungmin08g.zephyro.test.domain.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 라이브러리 테스트에 사용될 Repository
 */

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
