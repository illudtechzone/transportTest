package com.illud.transport.repository;

import com.illud.transport.domain.Rider;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Rider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

	Optional<Rider> findByiDPcode(String iDPcode);

}
