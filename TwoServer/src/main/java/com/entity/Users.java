package com.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "users")


public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToOne( cascade = CascadeType.REFRESH)
    @JoinColumn(name = "position_id")
    private com.entity.Position position;

    public LocalDate getLastdate() {
        return lastdate;
    }

    public void setLastdate(LocalDate lastdate) {
        this.lastdate = lastdate;
    }

    @Column(name = "lastdate")
    private LocalDate lastdate;

    @ElementCollection
    @CollectionTable(name = "likes_dislikes",
            joinColumns = @JoinColumn(name= "id_user"))
    @Column(name = "action")
    @MapKeyJoinColumn(name = "id_post")
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private Map<Posts,Boolean> listlike=new HashMap<>();

    @OneToMany(cascade=CascadeType.REFRESH,orphanRemoval=true,mappedBy = "owner",fetch = FetchType.EAGER)
    @JsonIgnoreProperties("listComments")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Posts> listPost;

    @OneToMany(cascade=CascadeType.REFRESH,orphanRemoval=true,mappedBy = "owner" )
    @JsonIgnoreProperties("childComment")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Comments> listComment;

    public Users(Long id, String login, String password, com.entity.Position position, Map<Posts, Boolean> listlike, List<Posts> listPost, List<Comments> listComment) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.position = position;
        this.listlike = listlike;
        this.listPost = listPost;
        this.listComment = listComment;
    }

    public Users()
    {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public com.entity.Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Map<Posts, Boolean> getListlike() {
        return listlike;
    }

    public void setListlike(Map<Posts, Boolean> listlike) {
        this.listlike = listlike;
    }

    public List<Posts> getListPost() {
        return listPost;
    }

    public void setListPost(List<Posts> listPost) {
        this.listPost = listPost;
    }

    public List<Comments> getListComment() {
        return listComment;
    }

    public void setListComment(List<Comments> listComment) {
        this.listComment = listComment;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users)) return false;

        Users users = (Users) o;

        if (!Objects.equals(id, users.id)) return false;
        if (!Objects.equals(login, users.login)) return false;
        if (!Objects.equals(password, users.password)) return false;
        if (!Objects.equals(position, users.position)) return false;
        if (!Objects.equals(listPost, users.listPost)) return false;
        return Objects.equals(listComment, users.listComment);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (listPost != null ? listPost.hashCode() : 0);
        result = 31 * result + (listComment != null ? listComment.hashCode() : 0);
        return result;
    }



}
