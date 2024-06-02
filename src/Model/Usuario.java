package src.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Usuario extends UserBase{
    private int id;
    private int mementoId;
    private String name;
    private String username;
    private String email;
    private String password;
    private JSONArray biblioteca;
    private String profilePic;

    public Usuario(String email, String password, String name, String username) {
        super(name,email, password);
        int newMementoId = 0;
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.profilePic = profilePic;
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get("src/usuarios.json")));
            JSONArray userList = new JSONArray(fileContent);
            if (!userList.isEmpty()) {
                JSONObject last = userList.getJSONObject(userList.length() - 1);
                newMementoId = last.getInt("mementoId") + 1;
            }
        } catch (IOException p) {
            p.printStackTrace();
        }
        this.mementoId = newMementoId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMementoId(int mementoId) {
        this.mementoId = mementoId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    public JSONArray getBiblioteca() {
        return biblioteca;
    }

    public int getMementoId() {
        return this.mementoId;
    }

}
