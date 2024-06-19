package org.coleccionista.test.controller;

import org.coleccionista.test.entity.Product;
import org.coleccionista.test.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "productList";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        List<Product> products = productRepository.findByNameContaining(keyword);
        model.addAttribute("products", products);
        return "productList";
    }

    @GetMapping("/priceRange")
    public String getProductsByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice, Model model) {
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        model.addAttribute("products", products);
        return "productList";
    }

    @GetMapping("/countByName")
    public String countProductsByName(@RequestParam String name, Model model) {
        long count = productRepository.countByName(name);
        List<Product> products = productRepository.findByNameContaining(name);
        model.addAttribute("count", count);
        model.addAttribute("name", name);
        model.addAttribute("products", products);
        return "productCount";
    }

    @GetMapping("/{id}")
    public String getProductById(@RequestParam Integer id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "productDetail";
        } else {
            return "error";
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "createProduct";
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@RequestParam Integer id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "editProduct";
        } else {
            return "error";
        }
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@RequestParam Integer id, @ModelAttribute Product product) {
        product.setId(id);
        productRepository.save(product);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@RequestParam Integer id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }
}
