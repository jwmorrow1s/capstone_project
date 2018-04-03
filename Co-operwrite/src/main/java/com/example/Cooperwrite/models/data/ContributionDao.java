package com.example.Cooperwrite.models.data;

import com.example.Cooperwrite.models.Contribution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ContributionDao extends CrudRepository<Contribution, Integer> {
    Contribution findTopByOrderByIdDesc();
    Contribution findByStoryIdAndUserId(int storyId, int userId);
    List<Contribution> findAllByStoryIdOrderByCardinality(int storyId);
    @Override
    List<Contribution> findAll();
}
