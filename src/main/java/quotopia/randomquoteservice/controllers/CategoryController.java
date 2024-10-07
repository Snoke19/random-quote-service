package quotopia.randomquoteservice.controllers;

import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;
import quotopia.randomquoteservice.models.Category;
import quotopia.randomquoteservice.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @CrossOrigin
    @GetMapping({"/{name}", ""})
    public List<Category> getCategoriesByName(@PathVariable(value = "name", required = false) String categoryName,
                                              @Min(value = 0, message = "{category.filter.min.offset}")
                                              @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return this.categoryService.getCategoriesByNameWithOffset(categoryName, offset);
    }
}