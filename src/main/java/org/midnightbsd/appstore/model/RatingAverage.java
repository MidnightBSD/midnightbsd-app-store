package org.midnightbsd.appstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Lucas Holt
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class RatingAverage {
    Number average;

    String packageName;
}
