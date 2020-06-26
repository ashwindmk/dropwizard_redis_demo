package com.ashwin.java.dropwizard.resources;

import com.ashwin.java.dropwizard.model.User;
import com.ashwin.java.dropwizard.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private static Log LOG = LogFactory.getLog(UserResource.class);

    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Path("/{id}")
    public User getUser(@PathParam("id") int id) {
        return userRepository.getUser(id);
    }

    @POST
    public Response storeUser(User user) {
        LOG.info(user);
        userRepository.storeUser(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/del/{id}")
    public boolean deleteUser(@PathParam("id") int id) {
        return userRepository.deleteUser(id);
    }
}
