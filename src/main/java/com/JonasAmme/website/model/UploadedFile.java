package com.JonasAmme.website.model;

import com.JonasAmme.website.utils.DataHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "uploaded_file")
public class UploadedFile {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "img_filename")
    private String imgFilename;

    @Column(name = "img_size")
    private float imgSize;

    @Column(name = "date_uploaded")
    private LocalDateTime dateUploaded = DataHelper.getCurrentTimeStamp();

    @Column(name = "parent_type")
    private String parentType;

    @Column(name = "parent_id")
    private Long parentId;
}
