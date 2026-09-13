package capp.domain.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "credential")
public class Credential {
    @Id
    private String credential_id;
    private String id;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;

    public Credential(String id, Role role, String password) {
        this.credential_id = UUID.randomUUID().toString();
        setId(id);
        setRole(role);
        if(role == Role.distributor){
            setPassword("Dpassword");
        } else {
            setPassword(password);
        }
    }
    public Credential() {
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

