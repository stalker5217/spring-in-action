package tacos;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Date createdAt;

    @NotNull
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;

    /**
     * @ManyToMany
     * Taco는 여러 개의 Ingredient를 가질 수 있고,
     * Ingredient는 여러 Taco 객체에 포함될 수 있다.
     */
    @ManyToMany(targetEntity=Ingredient.class)
    @Size(min=1, message="You must choose at least 1 ingredient")
    private List<Ingredient> ingredients;

    /**
     * 객체를 저장하기 전에 수행하는 선행 작업
     * 여기서는 생성 날짜를 지정한다.
     */
    @PrePersist
    void createAt(){
        this.createdAt = new Date();
    }
}