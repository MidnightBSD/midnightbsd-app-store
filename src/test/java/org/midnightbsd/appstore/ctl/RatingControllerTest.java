package org.midnightbsd.appstore.ctl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.midnightbsd.appstore.ctl.api.RatingController;
import org.midnightbsd.appstore.model.Package;
import org.midnightbsd.appstore.model.Rating;
import org.midnightbsd.appstore.services.RatingService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Lucas Holt
 */
@RunWith(MockitoJUnitRunner.class)
public class RatingControllerTest {
    @Mock
    private RatingService service;

    @InjectMocks
    private RatingController controller;

    private Rating rating;

    @Before
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
    public void testList() {
        final ResponseEntity<List<Rating>> result = controller.list();
        assertNotNull(result);
        assertEquals(1, result.getBody().size());
    }

    @Test
    public void testGet() {
        final ResponseEntity<Rating> result = controller.get(1);
        assertNotNull(result);
        assertEquals(3, result.getBody().getScore());
        assertEquals(1, result.getBody().getId());
    }
}
