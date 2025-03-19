package org.midnightbsd.appstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "package")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
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

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description",columnDefinition="TEXT",length = 65616)
    private String description;

    @Column(name="url")
    private String url;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "package_category_map", joinColumns = @JoinColumn(name = "package_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pkg", fetch = FetchType.LAZY)
    private Set<Rating> ratings;

    @JsonManagedReference
    @JsonProperty("instances")
    @OneToMany(cascade = { CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, mappedBy = "pkg", fetch = FetchType.EAGER)
    private Set<PackageInstance> instances;

    @JsonIgnore
    @Column(name = "created")
    @Temporal(value = TemporalType.DATE)
    private Date created = new Date();
}
