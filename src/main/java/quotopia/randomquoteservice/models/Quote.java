package quotopia.randomquoteservice.models;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    private Integer id;
    private String quote;
    private Author author;
}
