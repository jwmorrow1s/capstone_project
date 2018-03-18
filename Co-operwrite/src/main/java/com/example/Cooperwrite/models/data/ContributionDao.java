package com.example.Cooperwrite.models.data;

import com.example.Cooperwrite.models.Contribution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
@Transactional
public interface ContributionDao extends CrudRepository<Contribution, Integer> {
}
