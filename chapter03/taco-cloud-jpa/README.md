## Spring Data

스프링 데이터 프로젝트는 여러 개의 하위 프로젝트로 구성되어 있다.
그리고 이를 사용하여 여러 데이터베이스를 사용할 수 있는 퍼시스턴스를 제공한다.

|Category|Description|
|:---|:---|
|Spring Data JPA|관계형 데이터베이스의 JPA 퍼시스턴스|
|Spring Data MongoDB|몽고 문서형 데이터베이스의 퍼시스턴스|
|Spring Data Neo4|Neo4 그래프 데이터베이스의 퍼시스턴스|
|Spring Data Redis|레디스 key-value 스토어의 퍼시스턴스|
|Spring Data Cassandra|카산드라 데이터베이스의 퍼시스턴스|

JPA에서 특정 클래스를 Entity로 사용하기 위해 ```@Entity``` 애노테이션을 설정한다. 
그리고 이 클래스는 반드시 파라미터가 없는 생성자를 가져야 한다. 

``` java
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED, force=true)
@Entity
public class Ingredient {

    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

}
```

그리고 JPA Entity를 사용하는 JPA Repository를 선언하면 된다. 

``` java
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
```

인터페이스에 ```CrudRepositry```를 상속하여 CRUD에 해당하는 메서드를 생성한다. 
이렇게 상속을 하는 것만으로도 명시적으로 메서드들을 구현할 필요 없이 자동으로 생성해준다.
그리고 CRUD 외에 특정 쿼리를 생성해야할 필요가 있다. 
예를 들어, 특정 우편번호 기반으로 모든 주문 데이터를 가져와야 한다고 가정해보자. 
그 때는 인터페이스에 메서드를 선언해주면 된다. 

``` java
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByDeliveryZip(String deliveryZip);
}
```

이 또한 구현체를 정의할 필요가 없다. 
CrudRepository를 상속했을 때 자동으로 CRUD가 가능한 것 처럼 정의된 메서드의 이름 규칙을 따라 쿼리를 생성해주기 때문이다.