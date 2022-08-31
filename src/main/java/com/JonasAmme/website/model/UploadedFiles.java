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
@Table(name = "UPLOADED_FILES")
public class UploadedFiles {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;


    @Column(name = "UPLOADED_BY")
    private String UPLOADED_BY;

    @Column(name = "IMG_FILENAME")
    private String IMG_FILENAME;

    @Column(name = "IMG_SIZE")
    private float IMG_SIZE;

    @Column(name = "DATE_UPLOADED")
    private LocalDateTime DATE_UPLOADED = DataHelper.getCurrentTimeStamp();

    @Column(name = "PARENT_TYPE")
    private String parentType;

    @Column(name = "PARENT_ID")
    private Long parentId;
}
