package com.illud.transport.repository;

import com.illud.transport.domain.DriverDocuments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DriverDocuments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverDocumentsRepository extends JpaRepository<DriverDocuments, Long> {

}
