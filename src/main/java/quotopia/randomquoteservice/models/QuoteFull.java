package quotopia.randomquoteservice.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuoteFull extends Quote {

    private List<Category> categories = new ArrayList<>();

    public QuoteFull(Integer id, String quoteText, Author author) {
        super(id, quoteText, author);
    }
}
