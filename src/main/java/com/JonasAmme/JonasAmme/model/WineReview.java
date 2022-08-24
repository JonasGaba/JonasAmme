package com.JonasAmme.JonasAmme.model;


import com.JonasAmme.JonasAmme.repository.UploadedFilesRepository;
import com.JonasAmme.JonasAmme.utils.DataHelper;
import com.JonasAmme.JonasAmme.utils.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="WINE_REVIEWS")
public class WineReview {
        @Id
        @Column(name = "ID", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long ID;

        @Column(name = "NAME")
        private String NAME;
        @Column(name = "TYPE")
        private String TYPE;
        @Column(name = "NOTE")
        private String NOTE;
        @Column(name = "YEAR")
        private String YEAR;
        @Column(name = "PRICE")
        private String PRICE;
        @Column(name = "UPLOADED_FILES_IDS")
        private String UPLOADED_FILES_IDS;
        @Column(name = "RATING")
        private float RATING;
        @Column(name = "DATE_UPLOADED")
        private LocalDateTime DATE_UPLOADED = DataHelper.getCurrentTimeStamp();
        @Column(name = "PROFILE_PICTURE")
        private String PROFILE_PICTURE;

        @Transient
        private List<String> photosImagePath;

        @Transient
        private List<UploadedFiles> photos;

        /*public void setPhotosImagePath() {
                List<String> fileNames = null;
                if (UPLOADED_FILES_IDS == null || ID == null) photosImagePath = null;
                else
                        fileNames = FileUpload.getFileNamesInDir(System.getProperty("user.dir") + "\\wine_review_photos\\" + ID);
                assert fileNames != null;
                photosImagePath = fileNames.stream().map(f-> "/wine_review_photos/" + getID() + "/"+ f).collect(Collectors.toList());
                System.out.println(photosImagePath);


        }*/
}
