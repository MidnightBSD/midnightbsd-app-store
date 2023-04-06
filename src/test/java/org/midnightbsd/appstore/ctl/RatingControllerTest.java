package org.midnightbsd.appstore.ctl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.midnightbsd.appstore.ctl.api.RatingController;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.model.Rating;
import org.midnightbsd.appstore.services.RatingService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Lucas Holt
 */
@ExtendWith(MockitoExtension.class)
class RatingControllerTest {
    @Mock
    private RatingService service;

    @InjectMocks
    private RatingController controller;

    private Rating rating;

    @BeforeEach
    public void setup() {
        rating = new Rating();
        rating.setId(1);
        rating.setScore(3);
        rating.setPkg(new Package());
        rating.setCreated(Calendar.getInstance().getTime());

        when(service.list()).thenReturn(Collections.singletonList(rating));
        when(service.get(1)).thenReturn(rating);
    }

    @Test
    void testList() {
        final ResponseEntity<List<Rating>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
    }

    @Test
    void testGet() {
        final ResponseEntity<Rating> result = controller.get(1);
        assertNotNull(result);
        assertEquals(3, Objects.requireNonNull(result.getBody()).getScore());
        assertEquals(1, result.getBody().getId());
    }
}
