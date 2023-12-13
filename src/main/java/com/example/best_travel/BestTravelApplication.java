package com.example.best_travel;

import com.example.best_travel.domain.entities.*;
import com.example.best_travel.domain.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
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
		*/

		hotelRepository.findByRatingGreaterThan(3).forEach(
				System.out::println
		);


	}
}
