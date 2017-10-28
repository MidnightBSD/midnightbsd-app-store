package org.midnightbsd.appstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Lucas Holt
 */
@Entity
@Table(name = "os")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperatingSystem implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 7457280665276450257L;

    @Id
    @SequenceGenerator(name = "os_id_seq",
            sequenceName = "os_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "os_id_seq")
    @Column(name = "id", updatable = false)
    private int id;

    @Column(name="name", nullable = false, length = 30)
    private String name;

    @Column(name="version", nullable = false, length = 10)
    private String version;
}
