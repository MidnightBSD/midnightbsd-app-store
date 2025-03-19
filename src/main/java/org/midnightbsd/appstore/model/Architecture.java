package org.midnightbsd.appstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Lucas Holt
 */
@Entity
@Table(name="architecture")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Architecture implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = -8907435937263824811L;

    @Id
    @SequenceGenerator(name="architecture_id_seq",
            sequenceName="architecture_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="architecture_id_seq")
    @Column(name="id", updatable = false)
    private int id;

    @Column(name="name", nullable = false, length = 30)
    private String name;

    @Column(name="description",columnDefinition="TEXT",length = 65616)
    private String description;

    @JsonIgnore
    @Column(name = "created")
    @Temporal(value = TemporalType.DATE)
    private Date created = new Date();
}
