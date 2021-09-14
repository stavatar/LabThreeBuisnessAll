package com.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "posts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Posts implements Serializable {



    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "count_like")
    private Long countLike;



    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true,mappedBy = "post" )
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnoreProperties("childComment")
    private List<Comments> listComments;

    @ManyToMany(cascade={CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinTable(name = "likes_dislikes",
            joinColumns = @JoinColumn(name= "id_post"),
            inverseJoinColumns =  @JoinColumn(name= "id_user"))
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnoreProperties({"listPost", "listComment","listlike"})
    @JsonIgnore
    private List<com.entity.Users> listusersliked;

    /*@ManyToOne(fetch= FetchType.EAGER,cascade=CascadeType.REFRESH)
    @JoinTable(name = "Uusers_posts",
            joinColumns = @JoinColumn(name= "post_id"),
            inverseJoinColumns =  @JoinColumn(name= "user_id"))*/
    @ManyToOne( cascade = CascadeType.REFRESH)
    @JoinColumn(name = "owner")
    @JsonIgnoreProperties({"listPost", "listComment","listlike"})
    private Users owner;



}
