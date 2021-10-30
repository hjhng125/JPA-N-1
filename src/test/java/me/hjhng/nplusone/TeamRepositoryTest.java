package me.hjhng.nplusone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teams;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        List<Member> memberList = new ArrayList<>();
        for (int i = 1; i <= 10; ++i) {
            memberList.add(Member.builder()
                    .name("member" + i)
                    .build());
        }

        List<Team> teamList = new ArrayList<>();
        for (int i = 1; i <= 10; ++i) {
            Team team = Team.builder()
                    .name("team" + i)
                    .members(Collections.singletonList(memberList.get(i-1)))
                    .build();
            team.registerMember(memberList.get(i-1));
            teamList.add(team);
        }

        teams.saveAll(teamList);
        em.flush();
        em.clear();
    }

    @Test
    void 일대다_관계에서_EAGER_조회_시_N플러스1_문제가_발생한다() {
        List<Team> all = teams.findAll();

        assertThat(all).extracting("name").containsExactly("team1", "team2", "team3", "team4", "team5", "team6", "team7", "team8", "team9", "team10");
    }

    @Test
    void 패치_조인을_하면_N플러스1_문제가_발생하지_않는다() {
        List<Team> allFetchJoin = teams.findAllFetchJoin();

        assertThat(allFetchJoin).extracting("name").containsExactly("team1", "team2", "team3", "team4", "team5", "team6", "team7", "team8", "team9", "team10");
    }

    @Test
    void 일반_조인을_하면_연관관계_엔터티를_가져오지못한다() {
        List<Team> allLeftJoin = teams.findAllLeftJoin();

        assertThat(allLeftJoin).extracting("name").containsExactly("team1", "team2", "team3", "team4", "team5", "team6", "team7", "team8", "team9", "team10");
    }

    @Test
    void 엔터티_그래프를_사용하면_N플러스1이_발생하지_않는다() {
        List<Team> allEntityGraph = teams.findAllEntityGraph();
        assertThat(allEntityGraph).extracting("name").containsExactly("team1", "team2", "team3", "team4", "team5", "team6", "team7", "team8", "team9", "team10");
    }
}