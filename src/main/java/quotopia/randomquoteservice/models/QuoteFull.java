package quotopia.randomquoteservice.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuoteFull extends Quote {

    public QuoteFull(Integer id, String quoteText, Author author, Category category) {
        super(id, quoteText, author);
        this.category = category;
    }

    private Category category;
}
