package org.midnightbsd.appstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Lucas Holt
 */
@Entity
@Table(name = "rating")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Rating {

    @Id
    @SequenceGenerator(name = "rating_id_seq",
            sequenceName = "rating_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "rating_id_seq")
    @Column(name = "id", updatable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package pkg;

    @Column(name="author", nullable = false)
    private String author;

    @Column(name="comment", nullable = false, length = 500)
    private String comment;

    @JsonIgnore
    @Column(name = "created")
    @Temporal(value = TemporalType.DATE)
    private Date created = new Date();

    /**
     * range 1-5 (best)
     */
    @Column(name="score", nullable = false)
    private int score;
}
