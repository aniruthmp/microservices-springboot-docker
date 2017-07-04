package com.example.reservation.db;

import com.example.reservation.ReservationServiceApplication;
import com.example.reservation.db.domain.Reservation;
import com.example.reservation.db.repository.ReservationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReservationServiceApplication.class})
@AutoConfigureMockMvc
public class ReservationRepositoryTests {
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    Reservation reservation = null;
    List<Reservation> reservations = new ArrayList<>();

    @Before
    public void setUp() {
        reservation = new Reservation(1L, "Aniruth",
                "Sarathy",
                "Aniruth.Sarathy@gmail.com",
                "Male");

        Reservation r1, r2 = null;
        r1 = new Reservation(2L, "Boris",
                "Rhony",
                "Boris.Rhony@gmail.com",
                "Male");
        r2 = new Reservation(3L, "Jennifer",
                "Anniston",
                "Jenni.Anniston@outlook.com",
                "Female");

        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.reservationRepository.deleteAll();

        this.reservation = reservationRepository.save(reservation);
        this.reservations.add(reservationRepository.save(r1));
        this.reservations.add(reservationRepository.save(r2));
    }

    private MediaType halJsonContentType = new MediaType(MediaTypes.HAL_JSON.getType(),
            MediaTypes.HAL_JSON.getSubtype(),
            Charset.forName("utf8"));
    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void findReservationsTest() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJsonContentType));
    }

    @Test
    public void findByReservationIdTest() throws Exception {
        mockMvc.perform(get("/reservations/1 "))
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJsonContentType))
//                .andDo(print());
                .andExpect(jsonPath("$.firstName", is("Aniruth")))
                .andExpect(jsonPath("$.lastName", is("Sarathy")))
                .andExpect(jsonPath("$.email", is("Aniruth.Sarathy@gmail.com")));
//                .andExpect(jsonPath("$[1].venueName", is(this.venues.get(0).getVenueName())))
//                .andExpect(jsonPath("$[1].personName", is("Michael Jackson")))
//                .andExpect(jsonPath("$[1].bookingDate", is("2/15/1723")));

    }
}
