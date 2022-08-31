package com.JonasAmme.website.model;

import com.JonasAmme.website.utils.DataHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="RECIPES")
public class Recipes {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "NAME")
    private String NAME;
    @Column(name = "INGREDIENTS")
    private String INGREDIENTS;
    @Column(name = "INSTRUCTIONS")
    private String INSTRUCTIONS;
    @Column(name = "RATING")
    private float RATING;
    @Column(name = "DATE_UPLOADED")
    private LocalDateTime DATE_UPLOADED = DataHelper.getCurrentTimeStamp();
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime DATE_MODIFIED;
    @Column(name = "PROFILE_PICTURE")
    private String PROFILE_PICTURE;

    @Transient
    private List<UploadedFiles> photos;

    @Transient
    private String photosToDelete;

}
