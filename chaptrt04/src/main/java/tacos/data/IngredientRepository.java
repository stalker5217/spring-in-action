package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.Ingredient;

/**
 * CrudRepositry
 * 첫 번째 인자는 Repository에 저장될 Entity
 * 두 번째 인자는 ID 속성의 타입
 */
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
