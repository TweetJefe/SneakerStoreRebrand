package com.sneaker.store.products.repository;

import com.sneaker.store.products.dto.ProductCriteria;
import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.enums.Category;
import com.sneaker.store.products.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class ProductFinder {
    private final EntityManager em;

    public ProductFinder(EntityManager em) {
        this.em = em;
    }

    public static class CriteriaTools{
        private final CriteriaBuilder cb;
        private final Root<Product> root;

        public CriteriaTools(CriteriaBuilder cb, Root<Product> root) {
            this.cb = cb;
            this.root = root;
        }
    }

    public CompletableFuture<Page<ProductDTO>> findByCriteria (ProductCriteria criteria, Pageable pageable) {
        return CompletableFuture.supplyAsync(() -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductDTO> cq = cb.createQuery(ProductDTO.class);
            Root<Product> root = cq.from(Product.class);

            CriteriaTools tools = new CriteriaTools(cb, root);

            List<Predicate> predicates = new ArrayList<>();

            addPredicateIfNotNull(predicates, getByName(criteria.name(), tools));
            addPredicateIfNotNull(predicates, getByPrice(criteria.price(), tools));
            addPredicateIfNotNull(predicates, getByCategory(criteria.category(), tools));
            addPredicateIfNotNull(predicates, getBySize(criteria.size(), tools));
            addPredicateIfNotNull(predicates, getByBrand(criteria.brand(), tools));
            addPredicateIfNotNull(predicates, getByModel(criteria.model(), tools));


            cq.where(predicates.toArray(new Predicate[0]));

            cq.select(cb.construct(ProductDTO.class, root,
                    root.get("name"),
                    root.get("price"),
                    root.get("category"),
                    root.get("brand"),
                    root.get("size"),
                    root.get("model")));

            TypedQuery<ProductDTO> query = em.createQuery(cq);

            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<ProductDTO> products = query.getResultList();

            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Product> countProduct = countQuery.from(Product.class);
            countQuery.select(cb.count(countProduct));
            countQuery.where(predicates.toArray(new Predicate[0]));
            Long total = em.createQuery(countQuery).getSingleResult();
            return new PageImpl<>(products, pageable, total);
        });
    }

    private void addPredicateIfNotNull(List<Predicate> predicates, Predicate predicate){
        if(predicate != null){
            predicates.add(predicate);
        }
    }

    private Predicate getByName(String name, CriteriaTools tools){
        if (name != null){
            return tools.cb.like(tools.root.get("name"), "%" + name + "%");
        }
        return null;
    }

    private Predicate getByPrice(double calories, CriteriaTools tools){
        if (calories != 0){
            return tools.cb.equal(tools.root.get("price"), calories);
        }
        return null;
    }

    private Predicate getByCategory(Category category, CriteriaTools tools){
        if (category != null){
            return tools.cb.equal(tools.root.get("category"), category);
        }
        return null;
    }

    private Predicate getBySize(double calories, CriteriaTools tools){
        if (calories != 0){
            return tools.cb.equal(tools.root.get("price"), calories);
        }
        return null;
    }

    private Predicate getByBrand(String brand, CriteriaTools tools){
        if (brand != null){
            return tools.cb.like(tools.root.get("brand"), "%" + brand + "%");
        }
        return null;
    }

    private Predicate getByModel(String model, CriteriaTools tools){
        if (model != null){
            return tools.cb.like(tools.root.get("model"), "%" + model + "%");
        }
        return null;
    }
}
