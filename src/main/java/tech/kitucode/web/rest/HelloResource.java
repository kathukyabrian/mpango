package tech.kitucode.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/api/hello")
public class HelloResource {

    private final Logger logger = Logger.getLogger("acceptor");

    ObjectMapper mapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello() {

        long startTime = System.currentTimeMillis();

        logger.info("transaction|hello|begin");

        try {

            Map<String, String> testMap = new HashMap<>();
            testMap.put("hello", "world");
            testMap.put("meliora", "tech");

            String request = mapper.writeValueAsString(testMap);

            logger.info("transaction|hello|processing_time:" + (System.currentTimeMillis() - startTime) + "|end");

            return Response.status(Response.Status.OK).entity(request).build();

        } catch (JsonProcessingException ex) {
            logger.info("transaction|get-all-plans|processing_time:" + (System.currentTimeMillis() - startTime) + "|error", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
