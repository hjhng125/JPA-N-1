package me.hjhng.nplusone;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.members m")
    List<Team> findAllFetchJoin();

    @Query("SELECT t FROM Team t LEFT JOIN t.members m")
    List<Team> findAllLeftJoin();

    @EntityGraph(attributePaths = "members")
    @Query("SELECT t FROM Team t")
    List<Team> findAllEntityGraph();
}
