package capp.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "connection")
public class Connection {
    @Id
    private Long idDistributor;
    private String code;
    private String username;
    private LocalDateTime expires;

    public Connection(Long idDistributor,String code, String username, LocalDateTime expires) {
        setIdDistributor(idDistributor);
        setCode(code);
        setUsername(username);
        setExpires(expires);
    }

    public Connection() {
    }

    public void setIdDistributor(Long id_distributor) {
        this.idDistributor = id_distributor;
    }
    public Long getIdDistributor() {
        return this.idDistributor;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }
    public LocalDateTime getExpires() {
        return expires;
    }
}