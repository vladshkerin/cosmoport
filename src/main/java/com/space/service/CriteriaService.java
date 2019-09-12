package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.model.Ship_;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;
import java.util.Map;

@Service("criteriaService")
public class CriteriaService {

    private Map<String, String> params;
    private CriteriaBuilder builder;
    private Root<Ship> root;

    public Query createQuery(EntityManager entityManager, Map<String, String> params, boolean isPagination) {
        this.params = params;
        this.builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ship> criteria = builder.createQuery(Ship.class);
        this.root = criteria.from(Ship.class);

        criteria.select(root)
                .where(getRestrictions())
                .orderBy(getOrder());

        Query query = entityManager.createQuery(criteria);

        if (isPagination) {
            imposePagination(query);
        }

        return query;
    }

    private Predicate getRestrictions() {
        String name = params.getOrDefault("name", "");
        String planet = params.getOrDefault("planet", "");
        ShipType shipType = ShipType.valueOf(params.getOrDefault("shipType", "EMPTY"));
        long after = Long.parseLong(params.getOrDefault("after", "0"));
        long before = Long.parseLong(params.getOrDefault("before", "0"));
        String isUsedStr = params.getOrDefault("isUsed", "");
        Double minSpeed = Double.valueOf(params.getOrDefault("minSpeed", "0.0"));
        Double maxSpeed = Double.valueOf(params.getOrDefault("maxSpeed", "0.0"));
        Integer minCrewSize = Integer.valueOf(params.getOrDefault("minCrewSize", "0"));
        Integer maxCrewSize = Integer.valueOf(params.getOrDefault("maxCrewSize", "0"));
        Double minRating = Double.valueOf(params.getOrDefault("minRating", "0.0"));
        Double maxRating = Double.valueOf(params.getOrDefault("maxRating", "0.0"));

        Predicate predicate = builder.conjunction();
        predicate = like(name, Ship_.name, predicate);
        predicate = like(planet, Ship_.planet, predicate);
        predicate = equalEnum(shipType, predicate);
        predicate = between(after, before, predicate);
        predicate = equalString(isUsedStr, predicate);
        predicate = greaterThanOrEqualToDouble(minSpeed, Ship_.speed, predicate);
        predicate = lessThanOrEqualToDouble(maxSpeed, Ship_.speed, predicate);
        predicate = greaterThanOrEqualToInteger(minCrewSize, Ship_.crewSize, predicate);
        predicate = lessThanOrEqualToInteger(maxCrewSize, Ship_.crewSize, predicate);
        predicate = greaterThanOrEqualToDouble(minRating, Ship_.rating, predicate);
        predicate = lessThanOrEqualToDouble(maxRating, Ship_.rating, predicate);

        return predicate;
    }

    private Predicate like(String param, SingularAttribute<Ship, String> field, Predicate predicate) {
        if (!param.isEmpty()) {
            Predicate p = builder.like(root.get(field), "%" + param + "%");
            predicate = builder.and(predicate, p);
        }
        return predicate;
    }

    private Predicate equalEnum(ShipType shipType, Predicate predicate) {
        if (shipType != ShipType.EMPTY) {
            Predicate p = builder.equal(root.get(Ship_.shipType), shipType);
            predicate = builder.and(predicate, p);
        }
        return predicate;
    }

    private Predicate between(long after, long before, Predicate predicate) {
        if (after > 0 && before > 0) {
            Predicate p = builder.between(root.get(Ship_.prodDate), new Date(after), new Date(before));
            predicate = builder.and(predicate, p);
        }
        return predicate;
    }

    private Predicate equalString(String isUsedStr, Predicate predicate) {
        if (!isUsedStr.isEmpty()) {
            Predicate p = builder.equal(root.get(Ship_.isUsed), Boolean.valueOf(isUsedStr));
            predicate = builder.and(predicate, p);
        }
        return predicate;
    }

    private Predicate greaterThanOrEqualToDouble(Double param, SingularAttribute<Ship, Double> field, Predicate predicate) {
        if (param > 0.0) {
            Predicate p = builder.greaterThanOrEqualTo(root.get(field), param);
            predicate = builder.and(predicate, p);
        }
        return predicate;
    }

    private Predicate lessThanOrEqualToDouble(Double param, SingularAttribute<Ship, Double> field, Predicate predicate) {
        if (param > 0.0) {
            Predicate p = builder.lessThanOrEqualTo(root.get(field), param);
            predicate = builder.and(predicate, p);
        }
        return predicate;
    }

    private Predicate greaterThanOrEqualToInteger(Integer param, SingularAttribute<Ship, Integer> field, Predicate predicate) {
        if (param > 0) {
            Predicate p = builder.greaterThanOrEqualTo(root.get(field), param);
            predicate = builder.and(predicate, p);
        }
        return predicate;
    }

    private Predicate lessThanOrEqualToInteger(Integer param, SingularAttribute<Ship, Integer> field, Predicate predicate) {
        if (param > 0) {
            Predicate p = builder.lessThanOrEqualTo(root.get(field), param);
            predicate = builder.and(predicate, p);
        }
        return predicate;
    }

    private Order getOrder() {
        Order order = builder.asc(root.get(Ship_.id));
        String orderStr = String.valueOf(params.getOrDefault("order", "")).toLowerCase();

        switch (orderStr) {
            case "speed":
                order = builder.asc(root.get(Ship_.speed));
                break;
            case "date":
                order = builder.asc(root.get(Ship_.prodDate));
                break;
            case "rating":
                order = builder.asc(root.get(Ship_.rating));
                break;
        }

        return order;
    }

    private void imposePagination(Query query) {
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));

        if (pageNumber > 0) {
            int number = (pageSize == 0 ? 1 : pageSize) * pageNumber;
            query.setFirstResult(number);
        }
        if (pageSize > 0) {
            query.setMaxResults(pageSize);
        }
    }
}
