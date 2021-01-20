# 웹 애플리케이션 개발하기  

## Lombok

``` java
class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

    public Ingredient(String id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    
    public String getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public Type getType(){
        return type;
    }
}
```

롬복은 흔히 객체에 필요한 메소드들인 getter, setter나 생성자 처리를 자동으로 해주며 
어노테이션 기반으로 아래와 같이 간단하게 나타낼 수 있다. 

``` java
package tacos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

소스에 나타난 어노테이션은 롬복 어노테이션이다. 
자주 사용되는 롬복 어노테이션은 아래와 같다.

|annotation|description|
|:---|:---|
|```@Getter```|모든 필드에 대한 get 함수를 생성한다|
|```@Setter```|final 필드를 제외한 필드에 대한 set 함수를 생성한다|
|```@EqualsAndHashCode```|객체의 equals()와 hashCode()을 생성한다|
|```@CleanUp```|IO처리나 JDBC를 통한 처리를 할 때 자동으로 close() 함수를 호출한다|
|```@ToString```|toString()을 생성한다|
|```@NonNull```|필드가 Null 값이 아님을 보장한다|
|```@NoArgsConstructor```|파라미터가 없는 생성자를 생성한다|
|```@RequiredArgsConstructor```|필수로 값이 있어야하는 final, @NonNull 필드를 처리하기 위해 생성자를 생성한다|
|```@AllArgsConstructor```|모든 필드에 대한 생성자를 생성한다|
|```@Builder```|빌드 패턴을 위한 빌더를 생성한다|
|```@Value```|불변을 의미한다. 필드가 private과 final 처리된다|
|```@Data```|@Getter, @Setter, @toString, @EqualsAndHashCode, @RequiredArgsConstructor을 합친 것이다|


## Controller 

``` java
package tacos.web;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tacos.Order;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/current")
    public String orderForm(Model model){
        model.addAttribute("order", new Order());

        return "orderForm";
    }

    @PostMapping
    public String processOrder(Order order){
        log.info("Order submitted: " + order);

        return "redirect:/";
    }
}
```

```@Controller``` 어노테이션은 Spring MVC에서 해당 클래스가 컨트롤러라고 명시한다. 
이는 스프링의 Component Scan에 의해서 자동으로 빈에 등록된다. 
```@RequestMapping("/orders")```는 '/orders' 로 시작하는 경로를 처리함을 의미한다. 
그리고 부가적으로 @slf4j는 롬복에서 제공하며 logger를 생성한다. 

|annotation|description|
|:---|:---|
|```@RequestMapping```|다목적의 요청을 처리한다|
|```@GetMapping```|HTTP GET을 처리한다|
|```@PostMapping```|HTTP POST를 처리한다|
|```@PutMapping```|HTTP PUT을 처리한다|
|```@DeleteMapping```|HTTP DELETE를 처리한다|
|```@PatchMapping```|HTTP PATCH를 처리한다|

가급적 특정한 형태의 메소드를 처리하는 형태로 구현하는 것이 좋다. 
그리고 ```@RequestMapping(method=RequestMethod.GET)``` 처럼 특정한 형태를 지정할 수 있다. 
이는 ```@GetMapping```과 완전히 동일하다. 

## 유효성 검사  

각 종 데이터는 지켜야하는 조건 또는 포맷이 있다. 
이를 처리하기 위해서는 if-else 구문을 통해 검증할 수도 있다.
하지만 이는 일단 보기가 안 좋고 비즈니스 로직에 섞여 있고 애플리케이션 전반에 걸쳐 있기 때문에 관리가 어렵다.

자바에서는 이를 해결하기 위해 유효성 검증을 위한 API를 제공한다.
spring-boot-starter-validation에서 의존성을 추가할 수 있으며, 
이는 'Bean Validation API'와 구현체인 'Hibernate Validator'를 포함한다.

``` java
@Data
public class Taco {
	// 빈 값이 아니며 최소 5자 이상이 되어야 한다.
	@NotNull
	@Size(min=5, message="Name must be at least 5 characters long")
	private String name;
	
	@Size(min=1, message="You must choose at least 1 ingredient")
	private List<String> ingredients;
}
```

``` java
@PostMapping
public String processDesign(@Valid Taco design, Errors errors) {
    if (errors.hasErrors()) {
         return "design";
    }
    
    log.info("Processing design: " + design);
    
    return "redirect:/orders/current";
}
```

|annotation|description|지원 타입|
|:---|:---|:---|
|```@NotNull```|null이 아니여야 한다|All|
|```@NotEmpty```|null, ""가 아니여야 한다|Array, Map, Collection, CharSequence|
|```@NotBlank```|null, "", " "가 아니여야 한다|CharSequence|
|```@AssertTrue```|필드 값이 true여야 한다|boolean|
|```@AssertFalse```|필드 값이 false여야 한다|boolean|
|```@Size```|크기가 지정한 최소 값과 최대 값의 사이여야 한다|Array, Map, Collection, CharSequence|
|```@Min```|지정한 값 보다 커야 한다|기본 숫자형 또는 Wrapper, BigInteger|
|```@Max```|지정한 값 보다 작아야 한다|기본 숫자형 또는 Wrapper, BigInteger|
|```@Positive```|양수여야 한다|기본 숫자형 또는 Wrapper, BigInteger|
|```@PositiveOrZero```|0 또는 양수여야 한다|기본 숫자형 또는 Wrapper, BigInteger|
|```@Negative```|음수여야 한다|기본 숫자형 또는 Wrapper, BigInteger|
|```@NegativeOrZero```|0 또는 음수여야 한다|기본 숫자형 또는 Wrapper, BigInteger|
|```@Past```|날짜가 과거여야 한다|Date|
|```@PastOrPresent```|날짜가 과거 또는 현재여야 한다|Date|
|```@Future```|날짜가 미래여야 한다|Date|
|```@FutureOrPresent```|날짜가 미래 또는 현재여야 한다|Date|
|```@Pattern```|정규식을 통해 검증한다|CharSequence|
|```@Email```|이메일 형태의 포맷을 가져야 한다|CharSequence|