## [스프링이 지원하는 기술들]

### 1. IoC (Inversion of Control)

- 객체의 생명 주기와 의존성 관리를 스프링 컨테이너 대신 수행하는 기술
- 직접 객체를 생성하는 것이 아니라, 객체의 제어권을 스프링에 넘겨 코드 간의 결합도를 낮추고 유지 보수를 용이하게 한다.

### 2. DI (Dependency Injection)

- IoC의 구체적인 구현 방식으로, 객체 간의 의존성을 외부에서 주입 받는 기술
- DI를 사용하면, 의존 객체를 코드 내에서 직접 생성하는 대신, 설정을 통해 외부에서 주입받게 된다.
- @Autowired

### 3. AOP (Aspect-Oriented Programming)

- 로깅, 트렌젝션, 보안 등과 같은 부가 기능을 비즈니스 로직과 분리하여 용이하게 한다.

#### 정리

- 클래스는 스프링 컨테이너 위에서 오브젝트로 만들어져 동작한다.
- 스프링의 프로그래밍 모델에 따라 작성한다.
- 엔터프라이즈 기술 활용 시 스프링 API 와 서비스를 활용한다.

## [Spring Bean 이 무엇이고, Bean 의 라이프사이클은 어떻게 되는지 조사해요]

### 1. Spring Bean 이란?

- 스프링 IoC 컨테이너가 생성 및 관리하는 자바 객체
- 과거에는 개발자가 `new`연산자로 객체를 생성하고 생명 주기를 관리
- IoC 기술을 통해 객체 생성과 관리를 스프링이 대신
- **스프링 컨테이너에서 관리되는 객체** = **Bean**

### 2. Bean 등록 방법

- @Component, @Controller, @RestCotroller, @Service, @Repository 등이 존재 (클래스 단위로 등록)
- @Bean (메소드 단위로 등록)

### 3. 라이프 사이클

- 스프링 IoC 컨테이너 생성
- 스프링 Bean 생성
- 의존 관계 주입
- 초기화 콜백 메소드 호출 (빈의 초기화 작업)
- 로직 수행 및 빈 사용
- 소멸 전 콜백 메소드 호출
- 스프링 종료

## [스프링 어노테이션을 심층 분석해요]

### 1. 어노테이션이란?

- 자바 소스 코드에 추가하는 메타 데이터
- 실제 실행에는 영향을 주지 않으나 코드의 동작 방식을 설정하거나 특정 동작을 수행하게 만듦

### 2. 빈 등록 시 일어나는 과정 분석

- Component Scan을 통해 어노테이션이 붙은 클래스를 탐색
- 빈 정보를 등록
- 이를 바탕으로 빈 객체를 생성하고, 생성된 빈 사이의 의존성 주입
- 빈 객체를 IoC 컨테이너에서 관리

### 3. @ComponentScan

- 위 어노테이션을 통해 class path를 탐색하여 자동으로 빈 등록
- 스프링은 Application 실행 시 @ComponentScan을 기반으로 지정된 패키지 내에서 어노테이션이 부착된 클래스를 탐색 (명시하지 않으면 @ComponentScan을 선언한 클래스의 패키지가 기준)

## **[단위 테스트와 통합 테스트 탐구]**

### 1. 단위 테스트

- 하나의 코드 단위 (메서드 또는 클래스) 가 독립적으로 정상 동작하는지 확인하는 테스트
- 테스트 수행 시간이 빠르고 자주 수행 가능 → 최소한의 요소만 가져와서 수행 가능하다.
- 코드 내부 로직에 집중
- Mock 객체를 사용하여서 외부 요소를 격리 가능!!

```jsx
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {
    @InjectMocks // 모든 Mock 들이 InjectionMock 로 주입
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Spy // 진짜 객체를 InjectMocks 에 주입
    private ObjectMapper om;

    @Test
    public void 계좌등록_test() throws Exception {
        // given
        Long userId = 1L;

        AccountReqDto.AccountSaveReqDto accountSaveReqDto = new AccountReqDto.AccountSaveReqDto();
        accountSaveReqDto.setNumber(1111L);
        accountSaveReqDto.setPassword(1234L);

        // stub 1
        User ssar = newMockUser(userId, "ssar", "쌀");
        when(userRepository.findById(any())).thenReturn(Optional.of(ssar));

        // stub 2
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

        // stub 3
        Account ssarAccount = newMockAccount(1L,1111L, 1000L, ssar);
        when(accountRepository.save(any())).thenReturn(ssarAccount);

        // when
        AccountResDto.AccountSaveResDto accountSaveResDto = accountService.계좌등록(accountSaveReqDto, userId);
        String responseBody = om.writeValueAsString(accountSaveResDto);
        System.out.println("테스트: " + responseBody);

        // then
        assertThat(accountSaveResDto.getNumber()).isEqualTo(1111L);

    }

}
```

- Mockito란?
    - 자바 오픈 소스 테스트 프레임 워크
- @Mock
    - 특정 개체를 test 내에서 어노테이션을 통해 mock 객체로 바인딩 한다. (가짜 객체)
    - mock은 개발자가 지저한 stub 환경 외의 기능은 동작하지 않는다.
    - stub : mock 객체 생성의 동작을 지정하는 것, 테스트의 결과를 설정, 특정 매개변수를 받았을 때 특정 값을 return 또는 예외를 던질 수 있음
- @Spy
    - mock 객체는 개발자가 지정한 Stub 외의 기능은 동작하지 않는데, 만약 stub를 제외한 나머지를 그대로 사용하고 싶으면 ? → spy 사용
    - 실제 객체를 생성하고 메서드를 감시한다.
- @InjectMocks
    - @Mock, @Spy 로 지정된 mock 객체들 중 필요한 객체를 주입시킨다.

### 2. 통합 테스트

- 통합 테스트 (integration Test)
    - 모듈 또는 두 개 이상의 클래스가 함께 상호 작용하여 정상적으로 동작하는지 검증
    - 즉, 서로 다른 클래스가 함께 있을 때 문제를 발견하기 위함
    - 테스트 속도는 더 느림

    ```java
    // UserService와 UserRepository를 함께 테스트
    @SpringBootTest
    class UserIntegrationTest {
    
        @Autowired
        // 차이점
        UserService userService;
    
        @Test
        void testUserRegistration() {
            User user = new User("John");
            userService.registerUser(user);
            User result = userService.findUser("John");
            
            assertNotNull(result);
            assertEquals("John", result.getName());
        }
    }
    
    ```

- @Mockbean, @MockSpy
    - mock과 비슷하게 spring context에 mock으로 변한 bean에 등록되게 된다.
    - 대신 @InjectMocks가 아닌 @Autowired를 사용
    - 스프링 컨텍스트에 존재하는 기존 빈을 대체하여 등록
    - 