package com.example.Cooperwrite.models.data;
import com.example.Cooperwrite.models.Contribution;
import com.example.Cooperwrite.models.Story;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface StoryDao extends CrudRepository<Story, Integer> {
    Story findTopByOrderByIdDesc();
}
