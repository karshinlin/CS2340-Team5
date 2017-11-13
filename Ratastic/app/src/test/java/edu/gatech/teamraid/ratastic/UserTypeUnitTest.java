package edu.gatech.teamraid.ratastic;

import junit.framework.Assert;

import org.junit.Test;

import edu.gatech.teamraid.ratastic.Model.User;

public class UserTypeUnitTest {
    @Test
    public void testUserStringToUserType() {
        String test1 = "user";
        Assert.assertEquals(User.UserType.USER, User.UserType.getUserType(test1));
        String test2 = "USER";
        Assert.assertEquals(User.UserType.USER, User.UserType.getUserType(test2));
        String test3 = "Usr";
        Assert.assertNotSame(User.UserType.USER, User.UserType.getUserType(test3));
    }

    @Test
    public void testAdminStringToUserType() {
        String test1 = "admin";
        Assert.assertEquals(User.UserType.ADMIN, User.UserType.getUserType(test1));
        String test2 = "ADMIN";
        Assert.assertEquals(User.UserType.ADMIN, User.UserType.getUserType(test2));
        String test3 = "Admn";
        Assert.assertNotSame(User.UserType.ADMIN, User.UserType.getUserType(test3));
    }

    @Test
    public void testOtherStrings() {
        String test1 = "adsf";
        Assert.assertNotSame(User.UserType.USER, User.UserType.getUserType(test1));
        Assert.assertNotSame(User.UserType.ADMIN, User.UserType.getUserType(test1));
        String test2 = "12345";
        Assert.assertNotSame(User.UserType.USER, User.UserType.getUserType(test2));
        Assert.assertNotSame(User.UserType.ADMIN, User.UserType.getUserType(test2));
        String test3 = "ADSFE";
        Assert.assertNotSame(User.UserType.USER, User.UserType.getUserType(test3));
        Assert.assertNotSame(User.UserType.ADMIN, User.UserType.getUserType(test3));

    }
}



