package com.entity;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "comments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date_create")
    private LocalDate dateCreate;



    @OneToMany(cascade={CascadeType.REFRESH,CascadeType.REMOVE,CascadeType.PERSIST} ,orphanRemoval=true )
    @JoinColumn(name = "child_comment")
    @JsonIgnoreProperties({"parentComment"})
    private List<Comments> childComment;

    @ManyToOne(fetch= FetchType.EAGER,cascade={CascadeType.REFRESH,CascadeType.PERSIST}  )
    @JoinColumn(name = "child_comment")
    @JsonIgnoreProperties({"childComment"})
    private Comments parentComment;


    @ManyToOne( cascade = CascadeType.REFRESH)
    @JoinColumn(name = "owneruser")
    @JsonIgnoreProperties({"listPost", "listComment"})
    private Users owner;

    @ManyToOne( cascade = CascadeType.REFRESH)
    @JoinColumn(name = "post")
    @JsonIgnoreProperties({"listComments"})
    private Posts post;




}
