package com.github.dapeng.dms.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:43 PM
 */
@Entity
@Table(name = "video")
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_name")
    private String videoName;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "key")
    private String key;


    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Video(String videoName, String videoUrl) {
        this.videoName = videoName;
        this.videoUrl = videoUrl;
    }
}
