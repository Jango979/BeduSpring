package org.coleccionista.test.repository;

import org.coleccionista.test.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByName(String name);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByNameContaining(String keyword);
    // ESTE QUERY LO TUVE QUE PONER EN LENGUAJE DE SQL PORQUE NO ME ESTABA ACEPTANDO UN ACCESO DIRECTO
    //@Query("SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) = LOWER(:name)")
    //Cuenta el numero de productos cuyo nombre contiene el texto proporcionado,
    //sin importar si está en mayusculas o minusculas.
    @Query("SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    long countByName(@Param("name") String name);

}
