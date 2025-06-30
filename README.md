# Zephyro
### Spring AOP 어노테이션 기반 로깅 라이브러리

[![Spring Boot](https://img.shields.io/badge/Gradle-8+-02303A?style=flat&logo=Gradle&logoColor=white)](https://spring.io/projects/spring-boot) [![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white)](https://spring.io/projects/spring-boot) [![Spring Boot](https://img.shields.io/badge/JitPack-000000?style=flat&logo=JitPack&logoColor=white)](https://spring.io/projects/spring-boot) [![Spring Boot](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=hibernate&logoColor=white)](https://spring.io/projects/spring-boot) [![Spring Boot](https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white)](https://spring.io/projects/spring-boot)
> AOP와 어노테이션을 활용한 경량 로깅 라이브러리

### Features
- 라이브러리 전용 커스텀 로거
- 쿼리 로그
- 메서드 실행 시간 측정
- 메서드 성능 측정

커스텀 로거(ZephyroLogger)를 만들면서 System.out.println();문이랑 
ZephyroLogger의 성능 차이를 비교했는데 ZephyroLogger가 압도적으로 빨랐습니다. 👏
또, 시스템 성능 측면에서 생각했을때 ZephyroLoggerFactory를 추가하여 클래스별로 Logger가 동작할 수 있도록 제작했습니다.
기본 로그 형식과 커스텀으로 포맷된 로그 형식이 있습니다. (로그 생성할때 정의할 수 있습니다.)

쿼리 로그는 Hibernate가 생성한 SQL문을 사용자가 읽기 쉽게 가공했습니다.
(Hibernate가 자동으로 컬럼명에 m1_0., te1_0.를 만들어주는데 읽기 힘들것 같아 제거 & 특정 SQL 키워드를 대문자로 변환 등)

메서드 관련 AOP는 실행된 시간 측정과 성능 측정이 있습니다.
성능 측정 로그에는 나노초(ns)/초(ns), OS 정보, 패키지 경로, 컨텍스트 정보, JVM 관련 정보, 로드된 클래스 수, 가비지 컬렉션(GC) 등이 포함되어 있습니다.

## Build
Zephyro 라이브러리를 사용하기 위해서는 빌드(Gradle) 설정이 필요합니다. (Gradle 기준 ⤵️)

```gradle
repositories {
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}
```
```gradle
dependencies {
    implementation 'com.github.kyungmin08g:zephyro:1.0.0'
}
```
