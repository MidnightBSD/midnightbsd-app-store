package org.midnightbsd.appstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Lucas Holt
 */
@Entity
@Table(name="category")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Category implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = -8936929013241662432L;
    
    @Id
    @SequenceGenerator(name="category_id_seq",
            sequenceName="category_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="category_id_seq")
    @Column(name="id", updatable = false)
    private int id;

    @Column(name="name", nullable = false, length = 30)
    private String name;

    @Column(name="description",columnDefinition="TEXT",length = 65616)
    private String description;
}
