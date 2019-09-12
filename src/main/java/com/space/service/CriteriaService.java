package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.model.Ship_;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.Map;

@Service("criteriaService")
public class CriteriaService {

    public Query createQuery(EntityManager entityManager, Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ship> criteria = builder.createQuery(Ship.class);
        Root<Ship> root = criteria.from(Ship.class);

        criteria.select(root)
                .where(getRestrictions(builder, root, params))
                .orderBy(getOrder(builder, root, params));

        Query query = entityManager.createQuery(criteria);
        imposePagination(query, params);

        return query;
    }

    private Predicate getRestrictions(CriteriaBuilder builder, Root<Ship> root, Map<String, String> params) {
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
        if (!name.isEmpty()) {
            Predicate p = builder.like(root.get(Ship_.name), "%" + name + "%");
            predicate = builder.and(predicate, p);
        }
        if (!planet.isEmpty()) {
            Predicate p = builder.like(root.get(Ship_.planet), "%" + planet + "%");
            predicate = builder.and(predicate, p);
        }
        if (shipType != ShipType.EMPTY) {
            Predicate p = builder.equal(root.get(Ship_.shipType), shipType);
            predicate = builder.and(predicate, p);
        }
        if (after > 0 && before > 0) {
            Predicate p = builder.between(root.get(Ship_.prodDate), new Date(after), new Date(before));
            predicate = builder.and(predicate, p);
        }
        if (!isUsedStr.isEmpty()) {
            Predicate p = builder.equal(root.get(Ship_.isUsed), Boolean.valueOf(isUsedStr));
            predicate = builder.and(predicate, p);
        }
        if (minSpeed > 0.0 || maxSpeed > 0.0) {
            Predicate p = builder.between(root.get(Ship_.speed), minSpeed, maxSpeed);
            predicate = builder.and(predicate, p);
        }
        if (minCrewSize > 0 || maxCrewSize > 0) {
            Predicate p = builder.between(root.get(Ship_.crewSize), minCrewSize, maxCrewSize);
            predicate = builder.and(predicate, p);
        }
        if (minRating > 0 || maxRating > 0) {
            Predicate p = builder.between(root.get(Ship_.rating), minRating, maxRating);
            predicate = builder.and(predicate, p);
        }

        return predicate;
    }

    private Order getOrder(CriteriaBuilder builder, Root<Ship> root, Map<String, String> params) {
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

    private void imposePagination(Query query, Map<String, String> params) {
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
