# 데이터로 작업하기  

정적 웹사이트에 비해 동적 웹사이트으의 차별점은 사용자에게 보여지고 또 저장되는 데이터이다. 
많은 데이터베이스들이 출신되었지만, 여전히 관계형 데이터베이스가 최선인 선택인 경우가 많다. 
스프링에서는 이 관계형 데이터베이스를 사용하기 위해 JDBC Template와 JPA 두 가지 방안을 많이 사용한다. 

## JDBC Template  

JDBC는 자바 애플리케이션에서 데이터베이스에 접근할 수 있도록 제공하는 API다. 
자바에서 데이터베이스에 접근하는 모든 기술은 결국 내부적으로는 JDBC를 이용한다. 

흔히 JDBC라고 하면 다음과 같은 코드를 떠올리기 쉽다. 

``` java
public class JdbcIngredientRespotiroy2 implements IngredientRespository{
    public Ingredient findById(String id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            connection = dataSource.getConnection();

            statement = connection.preparedStatement(
                    "select id, name, type from Ingredient where id = ?";
            statement.setString(1, id);

            Ingredient ingredient = null;
            if(resultSet.next()){
                ingredient = new Ingredient(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        Ingredient.Type.valueOf(resultSet.getString("type")));
            }

            return ingredient;
        }
        catch(SQLException e){

        }
        finally{
            if(resultSet != null){
                try{
                    resultSet.close();
                }
                catch(SQLException e){}
            }

            if(statement != null){
                try{
                    statement.close();
                }
                catch(SQLException e){}
            }

            if(connection != null){
                try{
                    connection.close();
                }
                catch(SQLException){}
            }
        }
    }
}
```

low level의 JDBC 구현은 참 힘든 일이다. 
쿼리 한 줄 짜기 위해서는 수 많은 try-catch를 통해 모든 자원에 대한 클린업 코드를 작성해줘야 한다. 
하지만 스프링에서는 이를 좀 더 편하게 이용할 수 있는 JDBC Template를 지원한다. 

DB Connection에 관한 오픈, 예외 처리, 트랜잭션 제어, Result Set의 반복 등을 자동화함으로써 
조금 더 쿼리에 집중할 수 있는 환경을 만들어 준다는 것이다. 

``` java
/**
 * @Repository : @Controller와 같은 스테레오타입 어노테이션이며 자동으로 빈 등록이 된다.
 */
@Repository
public class JdbcIngredientRepository implements IngredientRepository {
    private JdbcTemplate jdbc;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Ingredient findById(String id) {
        return jdbc.queryForObject(
                "select id, name, type from Ingredient where id=?", this::mapRowToIngredient, id);
    }

    /**
     * RowMapper interface를 메서드
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException {
        return new Ingredient(
                rs.getString("id"),
                rs.getString("name"),
                Ingredient.Type.valueOf(rs.getString("type")));
    }
}
```

데이터를 삽입할 때는 ```SimpleJdbcInsert```를 사용할 수 있다. 
이는 데이터 추가를 좀 더 간단한 형태로 할 수 있도록 ```JdbcTemplate```를 Wrapping한 객체이다.

``` java
@Repository
public class JdbcOrderRepository implements OrderRepository {
    /**
     * SimpleJdbcInsert : 데이터 삽입을 위해 JdbcTemplate를 Wrapping한 객체
     */
    private SimpleJdbcInsert orderInserter;
    private SimpleJdbcInsert orderTacoInserter;
    private ObjectMapper objectMapper;

    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbc) {
        this.orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");

        this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order_Tacos");

        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();

        for (Taco taco : tacos) {
            saveTacoToOrder(taco, orderId);
        }

        return order;
    }

    private long saveOrderDetails(Order order) {
        @SuppressWarnings("unchecked")
        Map<String, Object> values = objectMapper.convertValue(order, Map.class);
        values.put("placedAt", order.getPlacedAt());

        long orderId = orderInserter
                        .executeAndReturnKey(values)
                        .longValue();

        return orderId;
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        orderTacoInserter.execute(values);
    }
}
```