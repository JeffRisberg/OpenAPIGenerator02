package com.company.controller;

import com.company.dto.Message;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/")
public class MessageController {

  // The Jersey counterpart of this would be:
  // @GET
  // @Path("/chat")
  // public Message chat(@QueryParam("source") String source) {

  @GET
  @Path("/chat")
  @Produces(MediaType.APPLICATION_JSON)
  public Message chat(@QueryParam("source") String source) {

    log.info("running chat");
    Message message = new Message();
    message.setSource(source);
    message.setContent("hello to you");

    return message;
  }
}