package quotopia.randomquoteservice.service;

import quotopia.randomquoteservice.dto.CategoryIdDto;

import java.util.List;

public interface CategoryService {

    List<CategoryIdDto> getCategoriesByName(String name, int offset);
}
