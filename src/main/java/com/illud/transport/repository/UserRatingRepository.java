package com.illud.transport.repository;

import com.illud.transport.domain.UserRating;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserRating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

}
