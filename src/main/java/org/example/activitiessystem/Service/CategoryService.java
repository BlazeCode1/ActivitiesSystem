package org.example.activitiessystem.Service;

import lombok.RequiredArgsConstructor;
import org.example.activitiessystem.Api.ApiException;
import org.example.activitiessystem.Model.Category;
import org.example.activitiessystem.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    public void updateCategory(Integer id,Category category){
        Category oldCategory = categoryRepository.findCategoryById(id);

        if(oldCategory == null)
            throw new ApiException("Category Not Found");
        oldCategory.setName(category.getName());
        oldCategory.setDescription(category.getDescription());
        oldCategory.setSort_priority(category.getSort_priority());
        categoryRepository.save(oldCategory);
    }

    public void deleteCategory(Integer id){
        Category category = categoryRepository.findCategoryById(id);
        if(category == null)
            throw new ApiException("Category Not Found");

        categoryRepository.delete(category);
    }
}
