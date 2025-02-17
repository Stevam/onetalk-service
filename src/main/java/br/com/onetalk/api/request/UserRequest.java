package br.com.onetalk.api.request;

public class UserRequest{
    private String id;
    private String name;
    private String email;
    private String password;
    private String profilePic;
    private String profilePicBase64;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getProfilePicBase64() {
        return profilePicBase64;
    }
}