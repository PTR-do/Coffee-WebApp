package capp.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Consumer {
    @Id
    private String username;
    private String email;
    private double credit;

    public Consumer(String username, String email, double credit) {
        setUsername(username);
        setEmail(email);
        setCredit(credit);
    }
    public Consumer() {
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public double getCredit() {
        return credit;
    }
    public void setCredit(double credit) {
        this.credit = credit;
    }
}
