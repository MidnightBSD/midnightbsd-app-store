package org.midnightbsd.appstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
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

    @Column(name="cpe", length = 200)
    private String cpe;

    @Column(name="flavor", length=100)
    private String flavor;

    @ManyToOne
    @JoinColumn(name = "os_id")
    private OperatingSystem operatingSystem;

    @ManyToOne
    @JoinColumn(name = "architecture_id")
    private Architecture architecture;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "package_instance_license_map", joinColumns = @JoinColumn(name = "package_instance_id", referencedColumnName = "id"),
                                                      inverseJoinColumns = @JoinColumn(name = "license_id", referencedColumnName = "id"))
    private Set<License> licenses;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private Package pkg;

    @Column(name="run")
    private Integer run;

    @JsonIgnore
    @Column(name = "created")
    @Temporal(value = TemporalType.DATE)
    private Date created = new Date();

}
