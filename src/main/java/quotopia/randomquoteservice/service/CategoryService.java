package quotopia.randomquoteservice.service;

import quotopia.randomquoteservice.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getScrolledCategoriesByName(String name, int offset);
}
