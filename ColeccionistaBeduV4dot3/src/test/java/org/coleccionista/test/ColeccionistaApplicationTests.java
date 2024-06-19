package org.coleccionista.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.coleccionista.test.controller.ProductController;
import org.coleccionista.test.entity.Product;
import org.coleccionista.test.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ColeccionistaApplicationTests {

	@Autowired
	private ProductController productController;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TestRestTemplate restTemplate;

	//Verifica que se conecte
	@Test
	public void contextLoads() {
		assertThat(productController).isNotNull();
	}

	//Prueba la creación de un producto.
	@Test
	public void testCreateProduct() {
		// Limpia el db
		productRepository.deleteAll(); //limpio db para asegurar que la base viene limpia
		Product product = new Product();
		product.setName("Producto de prueba");
		product.setDescription("Descripcion de prueba");
		product.setPrice(100.0);

		productRepository.save(product);

		List<Product> products = productRepository.findByName("Producto de prueba");
		assertThat(products).hasSize(1);
		assertThat(products.get(0).getDescription()).isEqualTo("Descripcion de prueba");
	}

	//Prueba hacer get a productos
	@Test
	public void testGetProducts() {
		ResponseEntity<String> response = restTemplate.getForEntity("/products", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Lista de Productos");
	}

	//Prueba de contar
	@Test
	public void testCountByName() {
		// Limpia el db
		productRepository.deleteAll();

		Product product1 = new Product();
		product1.setName("Producto");
		product1.setDescription("Descripcion1");
		product1.setPrice(10.0);
		productRepository.save(product1);

		Product product2 = new Product();
		product2.setName("Producto2");
		product2.setDescription("Descripcion2");
		product2.setPrice(20.0);
		productRepository.save(product2);

		Product product3 = new Product();
		product3.setName("Producto3");
		product3.setDescription("Descripcion3");
		product3.setPrice(100.0);
		productRepository.save(product3);

		// Conteo
		ResponseEntity<String> response = restTemplate.getForEntity("/products/countByName?name=Producto", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("<p>Numero de productos con el nombre \"<span>Producto</span>\": <span>3</span></p>");
	}
}
