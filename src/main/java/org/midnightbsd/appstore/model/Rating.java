package org.midnightbsd.appstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

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

    private Package pkg;

    @Column(name="author", nullable = false)
    private String author;

    @Column(name="comment", nullable = false, length = 500)
    private String comment;

    /**
     * range 1-5 (best)
     */
    @Column(name="score", nullable = false)
    private int score;
}
