package at.htl.formula1.entity;

import javax.persistence.*;

/**
 * Formula1 - Team
 * <p>
 * The id's are assigned by the database.
 */
@Entity
@Table(name = "F1_TEAM")
@NamedQueries({
        @NamedQuery(name = "Team.getTeambyName",
                    query = "select t from Team t where t.name = :NAME")
})
@NamedQuery(
        name = "Result.racesWonByTeam",
        query = "select r.race from Result r where r.position = 1 " +
                "and r.driver in (select distinct d.id from Driver d " +
                "where d.team = (select distinct t.id from Team t where t.name = :TEAM))"
)
public class Team {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    //region Constructors
    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }
    //endregion

    //region Getter and Setter

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion

    @Override
    public String toString() {
        return String.format("%d: %s", id, name);
    }
}
