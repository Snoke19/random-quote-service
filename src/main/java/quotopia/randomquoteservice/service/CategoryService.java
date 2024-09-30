package quotopia.randomquoteservice.service;

import quotopia.randomquoteservice.models.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getCategoriesByNameWithOffset(String categoryName, int offset);
}