 package unical.enterprise.jokibackend.Utility.CustomContextManager;

 import lombok.Data;

 @Data
 public class UserContext {
     private String scope;
     private String sid;
     private Boolean emailVerified;
     private String name;
     private String preferredUsername;
     private String givenName;
     private String familyName;
     private String email;

     public String toShortString() {
         return "username: " + preferredUsername + ", name: " + name + ", email: " + email;
     }
 }