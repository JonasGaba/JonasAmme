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
@Table(name="wine_review")
public class WineReview {
        @Id
        @Column(name = "id", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;
        @Column(name = "type")
        private String type;
        @Column(name = "note")
        private String note;
        @Column(name = "year")
        private String year;
        @Column(name = "price")
        private String price;

        @Column(name = "rating")
        private float rating;
        @Column(name = "date_uploaded")
        private LocalDateTime dateUploaded = DataHelper.getCurrentTimeStamp();
        @Column(name = "date_modified")
        private LocalDateTime dateModified;
        @Column(name = "profile_picture")
        private String profilePicture;

        @Transient
        private List<UploadedFile> photos;

        @Transient
        private String photosToDelete;

}
