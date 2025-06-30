# Zephyro
### Spring AOP 어노테이션 기반 로깅 라이브러리

[![Spring Boot](https://img.shields.io/badge/Gradle-8+-02303A?style=flat&logo=Gradle&logoColor=white)](https://spring.io/projects/spring-boot) [![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white)](https://spring.io/projects/spring-boot) [![Spring Boot](https://img.shields.io/badge/JitPack-000000?style=flat&logo=JitPack&logoColor=white)](https://spring.io/projects/spring-boot) [![Spring Boot](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=hibernate&logoColor=white)](https://spring.io/projects/spring-boot) [![Spring Boot](https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white)](https://spring.io/projects/spring-boot)
> AOP와 어노테이션을 활용한 경량 로깅 라이브러리

### Features
- 라이브러리 전용 로거(ZephyroLogger)
- 쿼리 로그
- 메서드 실행 시간 측정
- 메서드 성능 측정
</br>
커스텀 로거(ZephyroLogger)를 만들면서 System.out.println();문이랑 </br>
ZephyroLogger의 성능 차이를 비교했는데 ZephyroLogger가 압도적으로 빨랐습니다. 👏</br>
또, 시스템 성능 측면에서 생각했을때 ZephyroLoggerFactory를 추가하여 클래스별로 Logger가 동작할 수 있도록 제작했습니다.</br>
기본 로그 형식과 커스텀으로 포맷된 로그 형식이 있습니다. (로그 생성할때 정의할 수 있습니다.)</br></br>
쿼리 로그는 Hibernate가 생성한 SQL문을 사용자가 읽기 쉽게 가공했습니다.</br>
(Hibernate가 자동으로 컬럼명에 m1_0., te1_0.를 만들어주는데 읽기 힘들것 같아 제거 & 특정 SQL 키워드를 대문자로 변환 등)</br></br>
메서드 관련 AOP는 실행된 시간 측정과 성능 측정이 있습니다.</br>
성능 측정 로그에는 나노초(ns)/초(ns), OS 정보, 패키지 경로, 컨텍스트 정보, JVM 관련 정보, 로드된 클래스 수, 가비지 컬렉션(GC) 등이 포함되어 있습니다.</br></br>

## Build
Zephyro 라이브러리를 사용하기 위해서는 빌드(Gradle, Maven) 설정이 필요합니다. (Gradle 기준 ⤵️)

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

## 사용 방법
</br></br>
#### ZephyroLogger
사용자가 라이브러리 커스텀 로거(ZephyroLogger)를 사용할 수 있도록 구현했습니다. 원래는 라이브러리 내부에서만 동작하도록 하려고 했지만 ZephyroLogger의 성능이 너무 좋아서 풀어줬습니다. 🤣</br>
먼저 ZephyroLogger를 사용하려면 클래스 내부에 ZephyroLoggerFactory 필드를 선언 해줘야 합니다.
```java
public class XXXXClass {
  private static final ZephyroLogger log = ZephyroLoggerFactory.getLogger(XXXXClass.class);
 }
```
그러고 아래와 같이 설정 해주면 끝~!
```java
log.info(message);
```
또, 파라미터로 포맷된 로그 형식 사용 여부와 로그 색상이 있습니다. 아래와 같은 메서드의 파라미터는 info(), warn(), error(), debug() 모두 있습니다. 
```java
public void info(Object message) {
}

public void info(Object message, boolean isFormat) {
}

public void info(Object message, boolean isFormat, Color defaultColor) {
}
```
포맷된 로그 형식은 아래와 같습니다.
```sh
2025-06-30 16:13:26.094+09:00  INFO 4723 --- [multi-thread-2] c.g.kyungmin08g.zephyro.test.service.TestService : message
```
로그 기본 형식은 아래와 같습니다.
```sh
[INFO] message
```
기본 형식은 로그 레벨만 포맷되고 나머지 공간은 사용자 메시지로 남겼습니다. 로그를 커스텀해서 사용하지 않는 이상 포맷된 로그 형식을 사용하는 것을 추천합니다.</br></br>

#### System.out.println(); vs ZephyroLogger
System.out.println();문이랑 ZephyroLogger랑 성능 분석을 했습니다. 그 결과 압도적으로 ZephyroLogger가 빨랐습니다.
<img width="1211" alt="Screenshot 2025-06-25 at 3 00 04 PM" src="https://github.com/user-attachments/assets/b0ecc7a3-601f-4228-8433-df57c9f41e33" /></br></br>

#### Query Log
쿼리 로그를 사용하기 위해서는 반드시 @EnableZephyroQueryLogger 어노테이션을 프로젝트 애플리케이션에 선언해줘야 합니다.
```java
@SpringBootApplication
@EnableZephyroQueryLogger
public class XXXXApplication {
  public static void main(String[] args) {
    SpringApplication.run(XXXXApplication.class, args);
  }
}
```
그 다음 아래와 같이 @QueryLogTarget 어노테이션을 선언하여 해당 클래스에서는 쿼리 로그가 날라가도록 설정해주세요. (@QueryLogTarget == 쿼리 로그를 출력할 대상)</br>
@QueryLogTarget 어노테이션을 선언해주지 않으면 쿼리 로그가 출력되지 않습니다. (❗️주의: Bean 등록이 되어있는 클래스에 선언해주세요!!)
```java
@QueryLogTarget
public class Controller {
}
```
그러면 아래 예시와 같이 쿼리 로그가 출력되는 것을 확인하실 수 있습니다.
```sh
[INFO] [2025-06-30 15:56:11.879] [127.0.0.1] [PC] [k.k.domain.member.controller.MemberController#getMember()] SELECT member_id, age, created_at, gender, introduction, nick_name, password, profile_url, role, updated_at FROM member WHERE member_id = ?;
```
쿼리 로그에는 로그 레벨, 실행된 시간, 클라이언트 IP, 사용자 환경(PC, Mobile, API 등), 호출된 메서드 경로, 쿼리(SQL) 등이 있습니다.</br></br>

#### Time Log
메서드 실행 시간을 측정하기 위해서는 @MethodTimeTracker 어노테이션을 메서드 레벨에 선언해줘야 합니다.
```java
@MethodTimeTracker
public void runTimer() {
    try {
        Thread.sleep(2000);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```
해당 메서드를 호출하면 아래와 같이 메서드의 실행 시간을 측정한 로그가 출력됩니다.
```sh
[INFO] [2025-06-30 16:06:04.355] TestService#testRunTimer() 메서드 실행 시간: 2.003초
```
실행 시간 측정 로그에는 로그 레벨, 실행된 시간, 호출된 메서드 경로, 실행된 시간(단위: 초(s)) 등이 있습니다.</br></br>

#### Performance Log
메서드의 성능을 측정하고 싶다면 @PerformanceTracker 어노테이션을 메서드 레벨에 선언해줘야 합니다.
```java
@PerformanceTracker
public void runTimer() {
    try {
        Thread.sleep(2000);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```
해당 메서드를 호출하면 아래와 같이 메서드의 성능을 측정한 로그가 출력됩니다.
```sh
[INFO] [2025-06-30 16:13:26.094] TestService#testRunTimer() 메서드에 대한 성능 측정 결과입니다.
----------------------------------------------------------------------------
실행 컨텍스트: thread=http-nio-8080-exec-1 | pip=4723
위치: com.github.kyungmin08g.zephyro.test.service.TestService#testRunTimer()
힙 메모리: 최대=2.0GB / 총=80MB / 사용=55MB
JVM 가동 시간: 6.201s
경과 시간: 2.007s (2,007,350,750ns)
가비지 컬렉션: 횟수=12, 누적 시간=33ms | 로드된 클래스 수: 14,850
시스템: Mac OS X 15.5 (aarch64) | 자바 버전: 17.0.12
----------------------------------------------------------------------------
```
성능 측정 로그에는 로그 레벨, 실행된 시간, 호출된 메서드 경로, 나노초(ns)/초(ns), OS 정보, 컨텍스트 정보, JVM 관련 정보, 로드된 클래스 수, 가비지 컬렉션(GC) 등이 있습니다.</br></br>
사용하면서 개선해야할 점이나 코드를 직접 수정하고 싶으신 분들은 편하게 메일 주세요~! 😊😎
