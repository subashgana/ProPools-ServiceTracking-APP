package com.servicetracker.model;

import java.util.List;

/**
 * Created by pushpender.singh on 3/12/15.
 */
public class ServiceResponse {
    public String responseCode;
    public String responseMessage;
    public String userID;
    public String nodeID;
    public List<UserListModel> users;
    public List<UserListModel> userList;
    public List<UserListModel> customer;
}
