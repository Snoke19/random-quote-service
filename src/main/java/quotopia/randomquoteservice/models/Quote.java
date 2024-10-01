package quotopia.randomquoteservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    private Integer id;
    private String quoteText;
    private Author author;
}
