package tacos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @Data: getter, setter, toString, hashcode 등을 자동으로 생성한다
 * @RequiredArgsConstructor: final 필드나 NotNull 속성을 지는 값들을 초기화하는 생성자를 만든다
 */

@Data
@RequiredArgsConstructor
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
