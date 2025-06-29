package com.github.kyungmin08g.zephyro.test.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 라이브러리 테스트에 사용될 Entity
 */

@Entity
@Table(name = "test")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TestEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Comment("제목")
  @Column(nullable = false)
  public String title;

  @Comment("내용")
  @Column(nullable = false)
  public String content;
}
