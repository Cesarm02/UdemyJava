package com.example.best_travel;

import com.example.best_travel.domain.entities.*;
import com.example.best_travel.domain.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
@Slf4j
public class BestTravelApplication implements CommandLineRunner {

	private final HotelRepository hotelRepository;
	private final FlyRepository flyRepository;
	private final TourRepository tourRepository;
	private final TicketRepository ticketRepository;
	private final ReservationRepository reservationRepository;
	private final CustomerRepository customerRepository;

	public BestTravelApplication(HotelRepository hotelRepository, FlyRepository flyRepository, TourRepository tourRepository, TicketRepository ticketRepository, ReservationRepository reservationRepository, CustomerRepository customerRepository) {
		this.hotelRepository = hotelRepository;
		this.flyRepository = flyRepository;
		this.tourRepository = tourRepository;
		this.ticketRepository = ticketRepository;
		this.reservationRepository = reservationRepository;
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*
		FlyEntity fly = flyRepository.findById(15L).get();
		HotelEntity hotel = hotelRepository.findById(7L).get();
		TicketEntity entity = ticketRepository.findById(UUID.fromString("42345678-1234-5678-5233-567812345678")).get();
		ReservationEntity reservation = reservationRepository.findById(UUID.fromString("32345678-1234-5678-1234-567812345678")).get();
		CustomerEntity customer = customerRepository.findById("BBMB771012HMCRR022").get();

		log.info(String.valueOf(fly));
		log.info(String.valueOf(hotel));
		log.info(String.valueOf(entity));
		log.info(String.valueOf(reservation));
		log.info(String.valueOf(customer));

		this.flyRepository.selectLessPrice(BigDecimal.valueOf(20)).forEach(
				f -> System.out.println(f)
		);

		this.flyRepository.selectBetweenPrice(BigDecimal.valueOf(10), BigDecimal.valueOf(15)).forEach(
				f -> System.out.println(f)
		);


		//FlyEntity fly = flyRepository.findById(1L).get();
		//fly.getTickets().forEach(ticket -> System.out.println(ticket));
		FlyEntity fly2 = flyRepository.findByTicketId(UUID.fromString("42345678-1234-5678-5233-567812345678")).get();

		hotelRepository.findByPriceLessThan(BigDecimal.valueOf(100)).forEach(
				System.out::println
		);
		hotelRepository.findByPriceBetween(BigDecimal.valueOf(100), BigDecimal.valueOf(200)).forEach(
				System.out::println
		);

		hotelRepository.findByRatingGreaterThan(3).forEach(
				System.out::println
		);

		HotelEntity hotel = hotelRepository.findByReservationId(UUID.fromString("12345678-1234-5678-1234-567812345678")).get();
		System.out.println(hotel);
		*/

		CustomerEntity customer = customerRepository.findById("GOTW771012HMRGR087").get();
		log.info("Clien name: " + customer.getFullName());

		TourEntity tour = TourEntity.builder()
				.customer(customer).build();

		FlyEntity fly = flyRepository.findById(11L).get();
		log.info("Vuelo: "+ fly.getOriginName() + " - " + fly.getDestinyName());
		HotelEntity hotel = hotelRepository.findById(3L).get();
		log.info("Hotel: " + hotel.getName());
		TicketEntity ticket = TicketEntity.builder()
				.id(UUID.randomUUID())
				.price(fly.getPrice().multiply(BigDecimal.TEN))
				.arrivalDate(LocalDate.now())
				.departureDate(LocalDate.now())
				.purchaseDate(LocalDate.now())
				.customer(customer)
				.tour(tour)
				.fly(fly)
				.build();

		ReservationEntity reservation = ReservationEntity.builder()
				.id(UUID.randomUUID())
				.dateTimeReservation(LocalDateTime.now())
				.dateEnd(LocalDate.now().plusDays(2))
				.dateStart(LocalDate.now().plusDays(1))
				.hotel(hotel)
				.customer(customer)
				.tour(tour)
				.totalDays(1)
				.price(hotel.getPrice().multiply(BigDecimal.TEN))
				.build();

		tour.addTicket(ticket);
		tour.updateTickets();

		tour.addReservations(reservation);
		tour.updateReservation();

		TourEntity tourSave = this.tourRepository.save(tour);

		this.tourRepository.deleteById(tourSave.getId());


	}
}
