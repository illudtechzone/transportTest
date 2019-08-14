package com.illud.transport.repository;

import com.illud.transport.domain.Driver;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Driver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

	Optional<Driver> findByiDPcode(String iDPcode);

}
