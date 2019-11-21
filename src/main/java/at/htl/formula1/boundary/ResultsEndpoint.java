package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("results")
public class ResultsEndpoint {

    @PersistenceContext
    EntityManager em;


    /**
     * @param name als QueryParam einzulesen
     * @return JsonObject
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getPointsSumOfDriver(
            @QueryParam("name") String name
    ) {
        Long points = em
                .createNamedQuery("Result.sumPointsForDriver", Long.class)
                .setParameter("NAME", name)
                .getSingleResult();

        Driver driver = em
                .createNamedQuery("Driver.findByName", Driver.class)
                .setParameter("NAME", name)
                .getSingleResult();

        return Json
                .createObjectBuilder()
                .add("driver", driver.getName())
                .add("points", points)
                .build();
    }



    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("winner/{country}")
    public Response findWinnerOfRace(@PathParam("country") String country) {
        Long IdDriver = em.createNamedQuery("Result.getWinnerOfRace", Driver.class)
                .setParameter("COUNTRY", country)
                .getSingleResult()
                .getId();
        Driver winnerOfRace = em.find(Driver.class, IdDriver);
        return Response.ok(winnerOfRace).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("raceswon")
    public List<Race> racesWonByTeam(@QueryParam("team") String team) {
        List<Race> wonRaces = em.createNamedQuery("Result.racesWonByTeam", Race.class)
                .setParameter("TEAM", team)
                .getResultList();
        return wonRaces;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public List<String[]> allDriversWithPoints() {
        List<Driver> drivers = em
                .createNamedQuery("Driver.findAll", Driver.class)
                .getResultList();
        List<String[]> driversWithPoints = new LinkedList<>();
        for (Driver driver : drivers) {
            Long points = em.createNamedQuery("Result.getPoints", Long.class)
                    .setParameter("ID", driver)
                    .getSingleResult();
            driversWithPoints.add(new String[]{driver.toString(), "" + points});
        }
        return driversWithPoints;
    }




}
