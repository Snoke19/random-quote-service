package quotopia.randomquoteservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {

    private Integer id;
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
