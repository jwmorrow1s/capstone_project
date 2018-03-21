package com.example.Cooperwrite.models.data;

import com.example.Cooperwrite.models.Contribution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ContributionDao extends CrudRepository<Contribution, Integer> {
    /*TODO add an orderBy once ordering is figured out for contributions*/
    List<Contribution> findByStoryId(int storyId);
    List<Contribution> findByStoryIdOrderByCardinalityAsc(int storyId);
    List<Contribution> findByUserId(int userId);
}
