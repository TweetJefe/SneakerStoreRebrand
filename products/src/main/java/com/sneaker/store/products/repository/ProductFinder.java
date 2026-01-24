package com.sneaker.store.products.repository;

import com.sneaker.store.products.dto.ProductCriteria;
import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
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

            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);

            root.fetch("sizes", JoinType.LEFT);

            cq.distinct(true);

            CriteriaTools tools = new CriteriaTools(cb, root);

            List<Predicate> predicates = new ArrayList<>();

            addPredicateIfNotNull(predicates, getByName(criteria.name(), tools));
            addPredicateIfNotNull(predicates, getByPrice(criteria, tools));
            addPredicateIfNotNull(predicates, getByCategory(criteria.category(), tools));
            addPredicateIfNotNull(predicates, getBySize(criteria.size(), tools));
            addPredicateIfNotNull(predicates, getByBrand(criteria.brand(), tools));
            addPredicateIfNotNull(predicates, getByModel(criteria.model(), tools));

            cq.where(predicates.toArray(new Predicate[0]));

            cq.select(root);

            TypedQuery<Product> query = em.createQuery(cq);

            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<Product> products = query.getResultList();

            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Product> countProduct = countQuery.from(Product.class);

            countQuery.select(cb.countDistinct(countProduct));

            CriteriaTools countTools = new CriteriaTools(cb, countProduct);
            List<Predicate> countPredicates = new ArrayList<>();
            addPredicateIfNotNull(countPredicates, getByName(criteria.name(), countTools));
            addPredicateIfNotNull(countPredicates, getByPrice(criteria, countTools));
            addPredicateIfNotNull(countPredicates, getByCategory(criteria.category(), countTools));
            addPredicateIfNotNull(countPredicates, getBySize(criteria.size(), countTools));
            addPredicateIfNotNull(countPredicates, getByBrand(criteria.brand(), countTools));
            addPredicateIfNotNull(countPredicates, getByModel(criteria.model(), countTools));

            countQuery.where(countPredicates.toArray(new Predicate[0]));

            Long total = em.createQuery(countQuery).getSingleResult();

            List<ProductDTO> productDTOS = products.stream()
                    .map(product -> new ProductDTO(
                            product.getId(),
                            product.getName(),
                            product.getArticle(),
                            product.getPrice(),
                            product.isCategory(),
                            product.getBrand(),
                            product.getSizes(),
                            product.getModel(),
                            product.getImage() != null ? product.getImage().getUrl() : null
                    ))
                    .toList();

            return new PageImpl<>(productDTOS, pageable, total);
        });
    }

    private void addPredicateIfNotNull(List<Predicate> predicates, Predicate predicate){
        if(predicate != null){
            predicates.add(predicate);
        }
    }

    private Predicate getByName(String name, CriteriaTools tools){
        if (name != null && !name.isBlank()){
            return tools.cb.like(tools.cb.lower(tools.root.get("name")), "%" + name.toLowerCase() + "%");
        }
        return null;
    }

    private Predicate getByPrice(ProductCriteria criteria, CriteriaTools tools) {
        if (criteria.priceFrom() != null && criteria.priceTo() != null) {
            return tools.cb.between(tools.root.get("price"), criteria.priceFrom(), criteria.priceTo());
        } else if(criteria.priceFrom() != null){
            return tools.cb.greaterThanOrEqualTo(tools.root.get("price"), criteria.priceFrom());
        }else if(criteria.priceTo() != null){
            return tools.cb.lessThanOrEqualTo(tools.root.get("price"), criteria.priceTo());
        }
        return null;
    }

    private Predicate getByCategory(Boolean category, CriteriaTools tools) {
        if (category != null) {
            return tools.cb.equal(tools.root.get("category"), category);
        }
        return null;
    }

    private Predicate getBySize(List<Double> sizesCriteria, CriteriaTools tools) {
        if (sizesCriteria != null && !sizesCriteria.isEmpty()) {
            Join<Product, Double> sizesJoin = tools.root.join("sizes");
            return sizesJoin.in(sizesCriteria);
        }
        return null;
    }

    private Predicate getByBrand(List<String> brands, CriteriaTools tools){
        if (brands != null && !brands.isEmpty()) {
            return tools.root.get("brand").in(brands);
        }
        return null;
    }

    private Predicate getByModel(String model, CriteriaTools tools){
        if (model != null && !model.isBlank()){
            return tools.cb.like(tools.cb.lower(tools.root.get("model")), "%" + model.toLowerCase() + "%");
        }
        return null;
    }
}