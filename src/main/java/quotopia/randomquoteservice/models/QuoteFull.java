package quotopia.randomquoteservice.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QuoteFull extends Quote {

    private List<Category> categories = new ArrayList<>();

    public QuoteFull(Integer id, String quoteText, Author author, List<Category> categories) {
        super(id, quoteText, author);
        this.categories = categories;
    }
}
