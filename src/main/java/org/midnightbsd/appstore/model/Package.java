package org.midnightbsd.appstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Lucas Holt
 */
@Entity
@Table(name = "package")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Package implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = -1378527327884598865L;

    @Id
       @SequenceGenerator(name = "package_id_seq",
               sequenceName = "package_id_seq",
               allocationSize = 1)
       @GeneratedValue(strategy = GenerationType.SEQUENCE,
               generator = "package_id_seq")
       @Column(name = "id", updatable = false)
       private int id;

    private String name;

    private String description;

    private String url;

    private OperatingSystem operatingSystem;

    private Architecture architecture;

    private Set<Category> categories;

    private Set<License> license;

    private Set<Rating> ratings;
}
