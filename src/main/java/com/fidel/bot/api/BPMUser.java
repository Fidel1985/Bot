package com.fidel.bot.api;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

public class BPMUser {

    private Long id;

    @NotBlank(message = "Please, enter your first name")
    @Length(max = 49)
    private String firstName;

    @NotBlank(message = "Please, enter your last name")
    @Length(max = 49)
    private String lastName;

    @NotBlank(message = "Please, enter your username")
    @Length(max = 49)
    private String username;

    @NotBlank(message = "Please, enter your password")
    @Length(min = 5, max = 49, message = "Password length must be between {min} and {max}")
    private String password;

    @NotBlank(message = "Please, enter your email")
    @Email
    @Length(max = 49)
    private String email;

    @NotEmpty(message = "User should have at least one role")
    private Set<Authority> authorities;

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public static class Builder {
        private BPMUser users = new BPMUser();

        public Builder withId(Long id) {
            users.setId(id);
            return this;
        }

        public Builder withFirstName(String firstName) {
            users.setFirstName(firstName);
            return this;
        }

        public Builder withLastName(String lastName) {
            users.setLastName(lastName);
            return this;
        }

        public BPMUser build() {
            return users;
        }
    }
}
