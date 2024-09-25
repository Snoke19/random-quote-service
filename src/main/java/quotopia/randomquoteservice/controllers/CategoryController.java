package quotopia.randomquoteservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quotopia.randomquoteservice.dto.CategoryDto;
import quotopia.randomquoteservice.service.CategoryService;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories/{name}")
    public List<CategoryDto> getCategoriesByName(@PathVariable("name") String name,
                                                 @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return this.categoryService.getScrolledCategoriesByName(name, offset);
    }
}
