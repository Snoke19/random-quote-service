package quotopia.randomquoteservice.repository;

import quotopia.randomquoteservice.models.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> findFirst10CategoriesByNameContainingOrderByNameAscWithOffset(String categoryName, int offset);
}
