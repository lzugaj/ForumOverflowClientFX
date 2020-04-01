package sample.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import sample.config.RestTemplateConfig;
import sample.model.Category;
import sample.model.User;

/**
 * Created by lzugaj on Wednesday, April 2020
 */

public class PostController {

    public static final String BASE_CATEGORY_URL = "http://localhost:8080/forum-overflow/api/category";

    @FXML
    private TextArea titleTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    public void transferUser(User loggedInUser) {
        System.out.println(loggedInUser);

        List<Category> categories = fetchAllCategories();
        List<String> categoryNames = mapCategoryNames(categories);
        categoryChoiceBox.setItems(FXCollections.observableArrayList(categoryNames));
    }

    private List<Category> fetchAllCategories() {
        RestTemplate restTemplate = restTemplateConfig();
        return findAllCategories(restTemplate);
    }

    private RestTemplate restTemplateConfig() {
        return RestTemplateConfig.config();
    }

    private List<Category> findAllCategories(RestTemplate restTemplate) {
        ResponseEntity<List<Category>> responseEntity = restTemplate.exchange(BASE_CATEGORY_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Category>>() {});
        return responseEntity.getBody();
    }
    private List<String> mapCategoryNames(List<Category> categories) {
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.getName());
        }

        return names;
    }

    @FXML
    private void savePostActionHandler() {
        String title = titleTextArea.getText();
        String description = descriptionTextArea.getText();
        String category = categoryChoiceBox.getValue();
        Category searchedCategory = fetchSearchedCategory(category);

        // TODO: Ne smiju biti prazni inače upozorenje
        // TODO: Proslijedi ova 3 atributa i usera
        // TODO: Spremi i javi obavijest bila greška ili uspjeh

        System.out.println(title);
        System.out.println(description);
        System.out.println(searchedCategory);
    }

    private Category fetchSearchedCategory(String searchedCategory) {
        List<Category> categories = fetchAllCategories();
        for (Category category : categories) {
            if (category.getName().equals(searchedCategory)) {
                return category;
            }
        }

        return null;
    }
}
