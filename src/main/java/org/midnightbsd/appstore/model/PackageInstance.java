package org.midnightbsd.appstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Lucas Holt
 */
@Entity
@Table(name = "package_instance")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PackageInstance implements Serializable {

    @Id
    @SequenceGenerator(name = "package_instance_id_seq",
            sequenceName = "package_instance_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "package_instance_id_seq")
    @Column(name = "id", updatable = false)
    private int id;

    @Column(name="version", nullable = false)
    private String version;

    @ManyToOne
    @JoinColumn(name = "os_id")
    private OperatingSystem operatingSystem;

    @ManyToOne
    @JoinColumn(name = "architecture_id")
    private Architecture architecture;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "package_instance_license_map", joinColumns = @JoinColumn(name = "license_id", referencedColumnName = "id"),
                                                      inverseJoinColumns = @JoinColumn(name = "package_instance_id", referencedColumnName = "id"))
    private Set<License> licenses;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package pkg;

}
