package quotopia.randomquoteservice.service;

import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;
import quotopia.randomquoteservice.dto.CategoryDto;
import quotopia.randomquoteservice.models.Category;
import quotopia.randomquoteservice.repository.CategoryRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> getScrolledCategoriesByName(String name, long offset) {

        Window<Category> categoryWindow = this.categoryRepository.findFirst10ByNameContaining(name, ScrollPosition.offset(offset));

        return categoryWindow.getContent().stream().map(mapCategoryToDto()).collect(Collectors.toList());
    }

    private Function<Category, CategoryDto> mapCategoryToDto() {
        return category -> new CategoryDto(category.getId(), category.getName());
    }
}
