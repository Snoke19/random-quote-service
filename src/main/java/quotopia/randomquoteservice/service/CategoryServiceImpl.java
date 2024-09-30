package quotopia.randomquoteservice.service;

import org.springframework.stereotype.Service;
import quotopia.randomquoteservice.dto.CategoryIdDto;
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
    public List<CategoryIdDto> getCategoriesByName(String name, int offset) {

        List<Category> categoryWindow = this.categoryRepository.findFirst10ByNameContainingOrderByNameAsc(name, offset);
        return categoryWindow.stream().map(mapCategoryToDto()).collect(Collectors.toList());
    }

    private Function<Category, CategoryIdDto> mapCategoryToDto() {
        return category -> new CategoryIdDto(category.getId(), category.getName());
    }
}
