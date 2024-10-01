package quotopia.randomquoteservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quotopia.randomquoteservice.models.Category;
import quotopia.randomquoteservice.repository.CategoryRepository;
import quotopia.randomquoteservice.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategoriesByNameWithOffset(String categoryName, int offset) {
        if (categoryName != null) {
            categoryName = categoryName.toLowerCase();
        }
        return this.categoryRepository.findFirst10CategoriesByNameContainingOrderByNameAscWithOffset(categoryName, offset);
    }
}
